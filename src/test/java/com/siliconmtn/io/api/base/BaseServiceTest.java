package com.siliconmtn.io.api.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.siliconmtn.data.util.EntityUtil;

import lombok.Data;

/****************************************************************************
 * <b>Title:</b> BaseServiceTest.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Test for BaseService
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.x
 * @since Apr 23, 2021
 * <b>updates:</b>
 *  
 ****************************************************************************/
@ExtendWith(SpringExtension.class)
class BaseServiceTest {
	
	@Mock
	private EntityUtil entityUtil;
	
	@Mock
	private TestRepository testRepository = mock(TestRepository.class);
	
	@Spy
	@InjectMocks
	private TestService testService = new TestService(testRepository, entityUtil);
	
	private TestEntity testEntity;
	private List<TestEntity> entities;
	private TestDTO testDTO;
	private List<TestDTO> dtos;
	
	@BeforeEach
	void setUp() throws Exception {		
		testEntity = new TestEntity();
		testEntity.setId(UUID.fromString("40e6215d-b5c6-4896-987c-f30f3678f608"));
		testEntity.setName("Page 1");		
		entities = new ArrayList<>();
		entities.add(testEntity);
		
		testDTO = new TestDTO();
		testDTO.setId(UUID.fromString("40e6215d-b5c6-4896-987c-f30f3678f608"));
		testDTO.setName("Page 1");
		dtos = new ArrayList<>();
		dtos.add(testDTO);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#toEntity(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testToEntity() throws Exception {
		doReturn(testEntity).when(entityUtil).dtoToEntity(ArgumentMatchers.any(), ArgumentMatchers.any());
		assertEquals(testEntity, testService.toEntity(testDTO));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#toEntityList(java.util.List, java.lang.Class)}.
	 */
	@Test
	void testToEntityList() throws Exception {
		doReturn(entities).when(entityUtil).dtoListToEntity(ArgumentMatchers.any(), ArgumentMatchers.any());
		assertEquals(entities, testService.toEntityList(dtos));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#toDTO(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testToDTO() throws Exception {
		doReturn(testDTO).when(entityUtil).entityToDto(ArgumentMatchers.any(), ArgumentMatchers.any());
		assertEquals(testDTO, testService.toDTO(testEntity));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#toDTOList(java.util.List, java.lang.Class)}.
	 */
	@Test
	void testToDTOList() throws Exception {
		doReturn(dtos).when(entityUtil).entityListToDto(ArgumentMatchers.any(), ArgumentMatchers.any());
		assertEquals(dtos, testService.toDTOList(entities));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#find(java.util.UUID)}.
	 */
	@Test
	void testFind() throws Exception {
		doReturn(Optional.of(testEntity)).when(testRepository).findById(ArgumentMatchers.any());
		assertEquals(null, testService.find(testEntity.getId()));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#find(java.util.UUID)}.
	 */
	@Test
	void testFindNull() throws Exception {
		doReturn(Optional.ofNullable(null)).when(testRepository).findById(ArgumentMatchers.any());
		assertEquals(null, testService.find(testEntity.getId()));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#findDTO(java.util.UUID, java.lang.Class)}.
	 */
	@Test
	void testFindDTO() throws Exception {
		doReturn(testDTO).when(entityUtil).entityToDto(ArgumentMatchers.any(), ArgumentMatchers.any());
		doReturn(Optional.of(testEntity)).when(testRepository).findById(ArgumentMatchers.any());
		assertEquals(testDTO, testService.findDTO(testEntity.getId()));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#save(java.lang.Object)}.
	 */
	@Test
	void testSaveEntity() throws Exception {
		doReturn(testEntity).when(testRepository).save(ArgumentMatchers.any());
		assertEquals(null, testService.save(testEntity));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#save(java.lang.Object)}.
	 */
	@Test
	void testSaveDTO() throws Exception {
		doReturn(testDTO).when(testRepository).save(ArgumentMatchers.any());
		assertEquals(null, testService.save(testDTO));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#saveAll(java.lang.Object)}.
	 */
	@Test
	void testSaveAll() throws Exception {
		doReturn(entities).when(testRepository).saveAll(ArgumentMatchers.any());
		assertEquals(new ArrayList<>(), testService.saveAll(entities));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#saveAll(java.lang.Object)}.
	 */
	@Test
	void testSaveAllDTO() throws Exception {
		doReturn(entities).when(testRepository).saveAll(dtos);
		assertEquals(new ArrayList<>(), testService.saveAll(dtos));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#delete(java.util.UUID)}.
	 */
	@Test
	void testDeleteUUID() throws Exception {
		testService.delete(testEntity.getId());
		verify(testService).delete(testEntity.getId());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#delete(java.lang.Object)}.
	 */
	@Test
	void testDeleteEntity() throws Exception {
		testService.delete(testEntity);
		verify(testService).delete(testEntity);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.base.BaseService#deleteAll(java.util.List)}.
	 */
	@Test
	void testDeleteAll() throws Exception {
		testService.deleteAll(entities);
		verify(testService).deleteAll(entities);
	}
	
	@Data
	class TestEntity implements BaseEntity{
		private UUID id;
		private String name;
	}
	@Data
	class TestDTO implements BaseDTO{
		private UUID id;
		private String name;
	}
	interface TestRepository extends BaseRepository<TestEntity>{}
	class TestService extends BaseService<TestEntity, TestDTO> {
		protected TestService(TestRepository repository, EntityUtil entityUtil) {
			super(repository, entityUtil);
		}		
	}
	
}
