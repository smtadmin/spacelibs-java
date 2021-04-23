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
	
	public <V> T toEntity(V dto, Class<T> entity) {
		return entityUtil.dtoToEntity(dto, entity);
	}

	public <V> List<T> toEntityList(List<V> dtos, Class<T> entity) {
		return entityUtil.dtoListToEntity(dtos, entity);
	}

	public <V> V toDTO(T entity, Class<V> dto) {
		return entityUtil.entityToDto(entity, dto);
	}

	public <V> List<V> toDTOList(List<T> entities, Class<V> dto) {
		return entityUtil.entityListToDto(entities, dto);
	}

	public T find(UUID id) {
		return repository.findById(id).orElse(null);
	}

	public <V> V findDTO(UUID id, Class<V> dto) {
		return toDTO(find(id), dto);
	}

	public T save(T entity) {
		return repository.save(entity);
	}

	public <V> T save(V dto, Class<T> entity) {
		return save(toEntity(dto, entity));
	}

	public List<T> saveAll(List<T> entities) {
		return repository.saveAll(entities);
	}

	public <V> List<T> saveAll(List<V> dto, Class<T> entity) {
		return saveAll(toEntityList(dto, entity));
	}

	public void delete(UUID id) {
		repository.deleteById(id);
	}

	public void delete(T entity) {
		repository.delete(entity);
	}

	public void deleteAll(List<T> entities) {
		repository.deleteInBatch(entities);
	}
}
