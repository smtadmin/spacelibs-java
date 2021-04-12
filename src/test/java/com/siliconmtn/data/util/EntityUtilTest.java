package com.siliconmtn.data.util;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

// Lombok 1.18.x
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// JDK 11.x
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

//Java JPA
import javax.persistence.Entity;
import javax.persistence.EntityManager;

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
	private static Category nestedCategory;
	
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
}

@Getter
@Setter
@NoArgsConstructor
@ToString
class CategoryDTO {	
	private String code;
	private String groupCode;
	private String parentCode;
	private short depth;
	private String name;
}

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
class Category implements Serializable {
	
	private static final long serialVersionUID = 4457516413601618139L;
	private String code;
	private String groupCode;
	private Category parentCode;
	private short depth;
	private String name;
	private ZonedDateTime createdDate = ZonedDateTime.now(ZoneOffset.UTC);
}
