package com.siliconmtn.io.api.base;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/****************************************************************************
 * <b>Title:</b> BaseRepository.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Base Repository used by the Base Service, using JpaRepository
 * and UUIDs as the primary keys
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.x
 * @since Apr 22, 2021
 * <b>updates:</b>
 *  
 ****************************************************************************/
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
	// Intentionally blank
}
