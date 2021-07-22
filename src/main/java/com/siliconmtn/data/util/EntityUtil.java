package com.siliconmtn.data.util;

// JDK 11.3
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

// JPA
import javax.persistence.EntityManager;
import javax.persistence.Id;

// Spring 5.3.x
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// SpaceLibs 1.x
import com.siliconmtn.data.lang.ClassUtil;
import com.siliconmtn.io.api.EndpointRequestException;
import com.siliconmtn.io.api.base.BaseDTO;
import com.siliconmtn.io.api.base.BaseEntity;

import lombok.extern.log4j.Log4j2;

/****************************************************************************
 * <b>Title</b>: EntityUtil.java
 * <b>Project</b>: spaceforce-survey
 * <b>Description: </b> Class to map a Data Transfer Object DTO to Entity object.
 * Uses reflection with PropertyDescriptors to instantiate and set fields in a target class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 3.0
 * @since Feb 3, 2021
 * <b>updates:</b> 
 * 2/10/2020 - James Camire - Autowiring the Entity Manager
 ****************************************************************************/
@Component
@Log4j2
public class EntityUtil {
	
	private EntityManager entityManager;

	/**
	 * Creating bean for JPA Entity Manager by autowiring
	 * @param entityManager the JPA entity manager
	 */
	@Autowired
	public EntityUtil(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * To map any given DTO object to Entity object
	 * @param <T> Type of Entity object being returned
	 * @param dto Data Transfer Object that needs to be mapped
	 * @param entity Object to which the dto has to be mapped
	 * @return an entity that was mapped by a dto
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T dtoToEntity(BaseDTO dto, Class<T> entity) {
		if (dto == null || entity == null) return null;
		T entityInstance = null;

		try {
			entityInstance = entity.getConstructor().newInstance();
			
		} catch (Exception e) {
			log.error("unable to create an entity from given class", e);
			return null;
		}
		
		return (T) dtoToEntity(dto, entityInstance);
	}
	
	/**
	 * Load a dtos data into an existing entity
	 * 
	 * @param dto the dto to load data from
	 * @param entity the entity to update
	 * @return the newly updated entity
	 */
	public BaseEntity dtoToEntity(BaseDTO dto, BaseEntity entity) {
		if (dto == null || entity == null) return null;
		
		try {
			for (var dtoField : dto.getClass().getDeclaredFields()) {
				// Check for constants and continue
				if (Modifier.isFinal(dtoField.getModifiers())) continue;
				
				var value = getValueFromInstance(dtoField.getName(), dto);
				var entityField = entity.getClass().getDeclaredField(dtoField.getName());
				
				if (entityField.getType() != dtoField.getType() && value != null) {
					value = entityManager.getReference(entityField.getType(), value);
					if (value == null) 
						throw new EndpointRequestException(
								"dto conversion failed: " + entity.getClass().getSimpleName() + " not found within " + dto.getClass().getSimpleName(), 
								HttpStatus.NOT_FOUND);
				}
				
				setValueIntoInstance(dtoField.getName(), entity, value);
			}
			
		} catch (Exception e) {
			log.error("unable to convert dto to entity", e);
			return null;
		}
		
		return entity;
	}
	
	/**
	 * To map any given entity object into its respective dto object
	 * @param <T> type of dto object being returned
	 * @param entity the entity that needs to be mapped into dto
	 * @param dto the dto type to map the entity into
	 * @return a dto that was mapped by an entity
	 */
	public <T extends BaseDTO> T entityToDto(BaseEntity entity, Class<T> dto) {
		if (entity == null || dto == null) return null;
		T dtoInstance = null;

		try {
			dtoInstance = dto.getDeclaredConstructor().newInstance();

			for (Field dtoField : dto.getDeclaredFields()) {
				// Check for constants and continue
				if (Modifier.isFinal(dtoField.getModifiers())) continue;
				
				Object value = getValueFromInstance(dtoField.getName(), entity);
				var entityField = entity.getClass().getDeclaredField(dtoField.getName());
				
				if (entityField.getType() != dtoField.getType() && value != null) {
					List<Field> fieldsWithId = ClassUtil.getFieldsByAnnotation(entityField.getType(), Id.class);
					if (fieldsWithId.isEmpty()) {
						value = null;
					} else {
						String fieldName = fieldsWithId.get(0).getName();
						value = getValueFromInstance(fieldName, value);
					}

				}

				setValueIntoInstance(dtoField.getName(), dtoInstance, value);
			}

		} catch (Exception ex) {
			log.error("unable to convert to dto", ex);
			return null;
		}

		return dtoInstance;
	}
	
	/**
	 * Converts a List of DTO objects into a list of their corresponding entity
	 * @param <T> Type of Entity object being returned
	 * @param dtos List of DTO objects
	 * @param entity Entity to convert the DTOs
	 * @return Collection of entities
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> dtoListToEntity(List<?> dtos, Class<T> entity) {
		List<T> entities = new ArrayList<>();
		
		for (BaseDTO dto : (List<BaseDTO>)dtos) {
			entities.add(dtoToEntity(dto, entity));
		}
		
		return entities;
	}
	
	/**
	 * Coverts a List of DTO objects into a list of their corresponding dto
	 * @param <T> Type of DTO object being returned
	 * @param entities List of Entity objects
	 * @param dto DTO to convert the entities
	 * @return Collection of DTOs
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDTO> List<T> entityListToDto(List<?> entities, Class<T> dto) {
		List<T> dtos = new ArrayList<>();

		for (BaseEntity entity : (List<BaseEntity>)entities) {
			dtos.add(entityToDto(entity, dto));
		}

		return dtos;
	}
	
	/**
	 * Helper function that wraps Property descriptor in order to call the read method on a given class with a given field 
	 * @param fieldName the field to call a getter on
	 * @param classInstance the class the getter is inside of
	 * @return the object that was read from the class
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	private Object getValueFromInstance(String fieldName, Object classInstance) 
			throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		return new PropertyDescriptor(fieldName, classInstance.getClass()).getReadMethod().invoke(classInstance);
	}
	
	/**
	 * Helper function that wraps Property description to write field object from dto to entity
	 * @param fieldName the field to call a setter on
	 * @param classInstance the class the setter is inside of
	 * @param valueToSet the value of the data being set
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	private void setValueIntoInstance(String fieldName, Object classInstance, Object valueToSet) 
			throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		new PropertyDescriptor(fieldName, classInstance.getClass()).getWriteMethod().invoke(classInstance, valueToSet);
	}
}
