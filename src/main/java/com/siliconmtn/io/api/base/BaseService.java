package com.siliconmtn.io.api.base;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Spacelibs 1.x
import com.siliconmtn.data.lang.ClassUtil;
import com.siliconmtn.data.util.EntityUtil;

/****************************************************************************
 * <b>Title:</b> BaseService.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Base Service that provides common operations with EntityUtil
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.x
 * @since Apr 22, 2021
 * <b>updates:</b>
 *  
 ****************************************************************************/
public class BaseService<T extends BaseEntity, V extends BaseDTO> {

	private final BaseRepository<T> repository;
	private EntityUtil entityUtil;
	private Class<T> entityClass;
	private Class<V> dtoClass;
	
	/**
	 * Initializes the service with the repository and entity util
	 * @param repository Repository to connect for crud operations
	 * @param entityUtil Util class which converts a DTO to an entity and vice-versa
	 */
	@SuppressWarnings("unchecked")
	protected BaseService(BaseRepository<T> repository, EntityUtil entityUtil) {		
		this.repository = repository;
		this.entityUtil = entityUtil;
		
		var types = ClassUtil.getInternalTypes(getClass());
		this.entityClass = (Class<T>) types[0];
		this.dtoClass = (Class<V>) types[1];		
	}
	
	/**
	 * Convert a dto into an entity
	 * @param dto the dto to convert
	 * @param entity the resulting entity type
	 * @return an entity based on a dto
	 */
	public T toEntity(V dto) {
		return entityUtil.dtoToEntity(dto, entityClass);
	}

	/**
	 * Convert a list of dtos into a list of entities
	 * @param dtos the list of dtos
	 * @param entity the resulting entity type
	 * @return a list of entities
	 */
	public List<T> toEntityList(List<V> dtos) {
		return entityUtil.dtoListToEntity(dtos, entityClass);
	}

	/**
	 * Convert an entity into a dto
	 * @param entity the entity to convert
	 * @param dto the resulting dto type
	 * @return the dto based on an entity
	 */
	public V toDTO(T entity) {
		return entityUtil.entityToDto(entity, dtoClass);
	}

	/**
	 * Convert a list of entities into a list of DTOs
	 * @param entities the list of entities
	 * @param dto the resulting dto type
	 * @return a list of dtos converted from entities
	 */
	public List<V> toDTOList(List<T> entities) {
		return entityUtil.entityListToDto(entities, dtoClass);
	}

	/**
	 * Find an entity based on id
	 * @param id the primary key
	 * @return the entity with the given id
	 */
	@TransactionInjector
	public T find(UUID id) {
		return repository.findById(id).orElse(null);
	}

	/**
	 * Find an entity based on id and convert to DTO
	 * @param id the primary key
	 * @param dto the resulting dto type
	 * @return a dto converted from the found entity
	 */
	public V findDTO(UUID id) {
		return toDTO(find(id));
	}

	/**
	 * Save an entity to the repository
	 * @param entity the entity to save
	 * @return the saved entity with updated id
	 */
	@TransactionInjector
	public T save(T entity) {
		return repository.save(entity);
	}

	/**
	 * Save a dto to the repository, after converting into an entity
	 * @param dto the dto to save
	 * @param entity the resulting entity type
	 * @return an entity that was saved
	 */
	public T save(V dto) {
		return save(toEntity(dto));
	}

	/**
	 * Batch save a list of entities or dtos to the repository
	 * @param entities the list of entities/dtos
	 * @return the list of saved entities with updated ids
	 */
	@SuppressWarnings("unchecked")
	@TransactionInjector
	public List<T> saveAll(List<?> entities) {
		if (entities == null || entities.isEmpty()) return new ArrayList<>();
		if (entities.get(0) instanceof BaseDTO)
			return repository.saveAll(toEntityList((List<V>)entities));
		return repository.saveAll((List<T>)entities);
	}

	/**
	 * Delete an entity by given id from the repository (do nothing if not found)
	 * @param id the id to delete by
	 */
	@TransactionInjector
	public void delete(UUID id) {
		repository.deleteById(id);
	}

	/**
	 * Delete a given entity from the repository (do nothing if not found)
	 * @param entity the entity to delete
	 */
	@TransactionInjector
	public void delete(T entity) {
		repository.delete(entity);
	}

	/**
	 * Batch delete a list of entities from the repository (do nothing if not found)
	 * @param entities the list of entities to delete
	 */
	@TransactionInjector
	public void deleteAll(List<T> entities) {
		repository.deleteInBatch(entities);
	}
}
