package com.siliconmtn.io.api.base;

import java.util.List;
import java.util.UUID;

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
public class BaseService<T> {

	private final BaseRepository<T> repository;
	private EntityUtil entityUtil;
	
	protected BaseService(BaseRepository<T> repository, EntityUtil entityUtil) {
		this.repository = repository;
		this.entityUtil = entityUtil;
	}
	
	/**
	 * Convert a dto into an entity
	 * @param <V> the dtos type
	 * @param dto the dto to convert
	 * @param entity the resulting entity type
	 * @return an entity based on a dto
	 */
	public <V> T toEntity(V dto, Class<T> entity) {
		return entityUtil.dtoToEntity(dto, entity);
	}

	/**
	 * Convert a list of dtos into a list of entities
	 * @param <V> the dtos type
	 * @param dtos the list of dtos
	 * @param entity the resulting entity type
	 * @return a list of entities
	 */
	public <V> List<T> toEntityList(List<V> dtos, Class<T> entity) {
		return entityUtil.dtoListToEntity(dtos, entity);
	}

	/**
	 * Convert an entity into a dto
	 * @param <V> the dtos type
	 * @param entity the entity to convert
	 * @param dto the resulting dto type
	 * @return the dto based on an entity
	 */
	public <V> V toDTO(T entity, Class<V> dto) {
		return entityUtil.entityToDto(entity, dto);
	}

	/**
	 * Convert a list of entities into a list of DTOs
	 * @param <V> the dtos type
	 * @param entities the list of entities
	 * @param dto the resulting dto type
	 * @return a list of dtos converted from entities
	 */
	public <V> List<V> toDTOList(List<T> entities, Class<V> dto) {
		return entityUtil.entityListToDto(entities, dto);
	}

	/**
	 * Find an entity based on id
	 * @param id the primary key
	 * @return the entity with the given id
	 */
	public T find(UUID id) {
		return repository.findById(id).orElse(null);
	}

	/**
	 * Find an entity based on id and convert to DTO
	 * @param <V> the dtos type
	 * @param id the primary key
	 * @param dto the resulting dto type
	 * @return a dto converted from the found entity
	 */
	public <V> V findDTO(UUID id, Class<V> dto) {
		return toDTO(find(id), dto);
	}

	/**
	 * Save an entity to the repository
	 * @param entity the entity to save
	 * @return the saved entity with updated id
	 */
	public T save(T entity) {
		return repository.save(entity);
	}

	/**
	 * Save a dto to the repository, after converting into an entity
	 * @param <V> the dtos type
	 * @param dto the dto to save
	 * @param entity the resulting entity type
	 * @return an entity that was saved
	 */
	public <V> T save(V dto, Class<T> entity) {
		return save(toEntity(dto, entity));
	}

	/**
	 * Batch save a list of entities to the repository
	 * @param entities the list of entities
	 * @return the list of saved entities with updated ids
	 */
	public List<T> saveAll(List<T> entities) {
		return repository.saveAll(entities);
	}

	/**
	 * Batch save a list of dtos, converted into entities first
	 * @param <V> the dtos type
	 * @param dtos the list of dtos
	 * @param entity the resulting entity type
	 * @return the list of saved entities with updated ids
	 */
	public <V> List<T> saveAll(List<V> dtos, Class<T> entity) {
		return saveAll(toEntityList(dtos, entity));
	}

	/**
	 * Delete an entity by given id from the repository (do nothing if not found)
	 * @param id the id to delete by
	 */
	public void delete(UUID id) {
		repository.deleteById(id);
	}

	/**
	 * Delete a given entity from the repository (do nothing if not found)
	 * @param entity the entity to delete
	 */
	public void delete(T entity) {
		repository.delete(entity);
	}

	/**
	 * Batch delete a list of entities from the repository (do nothing if not found)
	 * @param entities the list of entities to delete
	 */
	public void deleteAll(List<T> entities) {
		repository.deleteInBatch(entities);
	}
}
