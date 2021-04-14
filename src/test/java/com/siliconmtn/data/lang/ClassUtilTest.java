package com.siliconmtn.data.lang;

//JDK 11.3
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//Junit 5
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title:</b> ClassUtilTest.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Test class to test ClassUtil
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.x
 * @since Apr 8, 2021
 * <b>updates:</b>
 *  
 ****************************************************************************/
class ClassUtilTest {
	@Retention(RetentionPolicy.RUNTIME)	private @interface FieldAnnotation {}
	@Retention(RetentionPolicy.RUNTIME)	private @interface MethodAnnotation {}
	
	private class AnnotatedClass {
		@FieldAnnotation
		private int field;
		
		@MethodAnnotation
		private int getField() { return field; }
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.lang.ClassUtil#getFieldsByAnnotation(java.lang.Class, java.lang.Class)}.
	 */
	@Test
	void testGetFieldsByAnnotationNullClass() throws Exception {
		var fieldList = ClassUtil.getFieldsByAnnotation(null, FieldAnnotation.class);
		Assertions.assertEquals(0, fieldList.size());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.lang.ClassUtil#getFieldsByAnnotation(java.lang.Class, java.lang.Class)}.
	 */
	@Test
	void testGetFieldsByAnnotation() throws Exception {
		var fieldList = ClassUtil.getFieldsByAnnotation(AnnotatedClass.class, FieldAnnotation.class);
		Assertions.assertEquals("field", fieldList.get(0).getName());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.lang.ClassUtil#getMethodsByAnnotation(java.lang.Class, java.lang.Class)}.
	 */
	@Test
	void testGetMethodsByAnnotationNullClass() throws Exception {
		var methodList = ClassUtil.getMethodsByAnnotation(null, MethodAnnotation.class);
		Assertions.assertEquals(0, methodList.size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.lang.ClassUtil#getMethodsByAnnotation(java.lang.Class, java.lang.Class)}.
	 */
	@Test
	void testGetMethodsByAnnotation() throws Exception {
		var methodList = ClassUtil.getMethodsByAnnotation(AnnotatedClass.class, MethodAnnotation.class);
		Assertions.assertEquals("getField", methodList.get(0).getName());
	}
	
}
