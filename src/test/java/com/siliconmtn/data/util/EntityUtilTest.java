package com.siliconmtn.data.util;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

// JDK 11.x
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.asm.ex.NoSuchFieldException;

/****************************************************************************
 * <b>Title</b>: EntityUtilTest.java
 * <b>Project</b>: spaceforce-survey
 * <b>Description: </b> Used to test the EntityUtil class for converting DTOs in to Entities
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.0
 * @since Feb 4, 2021
 * @updates:
 ****************************************************************************/
class EntityUtilTest {
	
	private static CategoryDTO dto;
	private static CategoryDTO nestedDTO;
	private static Category category;
	private static Category2 category2;
	private static Category nestedCategory;
	private static Category2 nestedCategory2;
	
	@MockBean
	private static EntityManager entityManager;
	private static EntityUtil entityUtil;
	
	/**
	 * Steps to execute before each and every test
	 * Defining category dto and entity object with data
	 */
	@BeforeAll
	static void setup()
	{
		dto = new CategoryDTO();
		dto.setCode("CODE");
		dto.setGroupCode("CODE");
		dto.setParentCode(null);
		dto.setDepth((short) 0);
		dto.setName("Name");
		
		category = new Category();
		category.setCode("CODE");
		category.setGroupCode("CODE");
		category.setParentCode(null);
		category.setDepth((short) 0);
		category.setName("Name");
		
		category2 = new Category2();
		category2.setCode("CODE");
		category2.setGroupCode("CODE");
		category2.setParentCode(null);
		category2.setDepth((short) 0);
		category2.setName("Name");
		
		nestedDTO = new CategoryDTO();
		nestedDTO.setCode("CODE2");
		nestedDTO.setGroupCode("CODE");
		nestedDTO.setParentCode("CODE");
		nestedDTO.setDepth((short) 1);
		nestedDTO.setName("Name2");
		
		nestedCategory = new Category();
		nestedCategory.setCode("CODE2");
		nestedCategory.setGroupCode("CODE");
		nestedCategory.setParentCode(category);
		nestedCategory.setDepth((short) 1);
		nestedCategory.setName("Name2");
		
		nestedCategory2 = new Category2();
		nestedCategory2.setCode("CODE2");
		nestedCategory2.setGroupCode("CODE");
		nestedCategory2.setParentCode(category2);
		nestedCategory2.setDepth((short) 1);
		nestedCategory2.setName("Name2");

		entityManager = Mockito.mock(EntityManager.class);
		entityUtil = new EntityUtil(entityManager);
	}
	
	/**
	 * Test method for {@link com.smt.ezform.core.EntityUtil#dtoToEntity(Object, Class<T>, EntityManager)}
	 */
	@Test
	void testDtoToEntity() throws Exception {	
		doReturn(category).when(entityManager).getReference(ArgumentMatchers.any(), ArgumentMatchers.any());
		Category result = entityUtil.dtoToEntity(dto, Category.class);
		assertEquals(category.getCode(), result.getCode());		
		assertEquals(category.getGroupCode(), result.getGroupCode());	
		assertEquals(category.getParentCode(), result.getParentCode());	
		assertEquals(category.getDepth(), result.getDepth());	
		assertEquals(category.getName(), result.getName());	
	}
	
	/**
	 * Test method for {@link com.smt.ezform.core.EntityUtil#dtoToEntity(Object, Class<T>, EntityManager)}
	 */
	@Test
	void testDtoToEntityWithNestedType() throws Exception {		
		doReturn(category).when(entityManager).getReference(ArgumentMatchers.any(), ArgumentMatchers.any());
		Category result = entityUtil.dtoToEntity(nestedDTO, Category.class);
		assertEquals(nestedCategory.getCode(), result.getCode());		
		assertEquals(nestedCategory.getGroupCode(), result.getGroupCode());	
		assertEquals(nestedCategory.getParentCode().getCode(), result.getParentCode().getCode());	
		assertEquals(nestedCategory.getDepth(), result.getDepth());	
		assertEquals(nestedCategory.getName(), result.getName());	
	}
	
	/**
	 * Test method for {@link com.smt.ezform.core.EntityUtil#dtoToEntity(Object, Class<T>, EntityManager)}
	 */
	@Test
	void testDtoToEntityThrow() throws Exception{
		doThrow(new IllegalArgumentException()).when(entityManager).getReference(ArgumentMatchers.any(), ArgumentMatchers.any());
		Category result = entityUtil.dtoToEntity(nestedDTO, Category.class);
		assertEquals(null, result);
	}

	/**
	 * Test method for {@link com.siliconmtn.data.util.EntityUtil#dtoListToEntity(java.util.List, java.lang.Class)}.
	 */
	@Test
	void testDtoListToEntity() throws Exception {
		List<CategoryDTO> dtos = new ArrayList<>();
		dtos.add(dto);
		dtos.add(nestedDTO);
		
		List<Category> categories = entityUtil.dtoListToEntity(dtos, Category.class);
		assertEquals(2, categories.size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.util.EntityUtil#entityToDto(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testEntityToDto() throws Exception {
		CategoryDTO result = entityUtil.entityToDto(category, CategoryDTO.class);
		assertEquals(category.getCode(), result.getCode());		
		assertEquals(category.getGroupCode(), result.getGroupCode());	
		assertEquals(category.getParentCode(), result.getParentCode());	
		assertEquals(category.getDepth(), result.getDepth());	
		assertEquals(category.getName(), result.getName());	
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.util.EntityUtil#entityToDto(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testEntityToDtoMissingAnnotation() throws Exception {
		CategoryDTO result = entityUtil.entityToDto(nestedCategory2, CategoryDTO.class);
		assertEquals(nestedCategory2.getCode(), result.getCode());		
		assertEquals(nestedCategory2.getGroupCode(), result.getGroupCode());	
		assertEquals(null, result.getParentCode());	
		assertEquals(nestedCategory2.getDepth(), result.getDepth());	
		assertEquals(nestedCategory2.getName(), result.getName());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.util.EntityUtil#entityToDto(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testEntityToDtoWithNestedType() throws Exception {
		CategoryDTO result = entityUtil.entityToDto(nestedCategory, CategoryDTO.class);
		assertEquals(nestedCategory.getCode(), result.getCode());		
		assertEquals(nestedCategory.getGroupCode(), result.getGroupCode());	
		assertEquals(nestedCategory.getParentCode().getCode(), result.getParentCode());	
		assertEquals(nestedCategory.getDepth(), result.getDepth());	
		assertEquals(nestedCategory.getName(), result.getName());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.util.EntityUtil#entityToDto(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testEntityToDtoThrow() throws Exception {
		doThrow(new NoSuchFieldException()).when(nestedCategory).getClass().getDeclaredField(ArgumentMatchers.any());
		CategoryDTO result = entityUtil.entityToDto(nestedCategory, CategoryDTO.class);
		assertEquals(null, result);
	}

}

@Data
@NoArgsConstructor
class CategoryDTO {	
	private String code;
	private String groupCode;
	private String parentCode;
	private short depth;
	private String name;
}

@Data
@NoArgsConstructor
class Category implements Serializable {	
	private static final long serialVersionUID = 4457516413601618139L;
	@Id
	private String code;
	private String groupCode;
	private Category parentCode;
	private short depth;
	private String name;
	private ZonedDateTime createdDate = ZonedDateTime.now(ZoneOffset.UTC);
}

@Data
@NoArgsConstructor
class Category2 implements Serializable {	
	private static final long serialVersionUID = 6215676666713039022L;
	private String code;
	private String groupCode;
	private Category2 parentCode;
	private short depth;
	private String name;
	private ZonedDateTime createdDate = ZonedDateTime.now(ZoneOffset.UTC);
}
