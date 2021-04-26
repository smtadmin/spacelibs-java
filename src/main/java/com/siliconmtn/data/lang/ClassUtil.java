package com.siliconmtn.data.lang;

// JDK 11.3
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/****************************************************************************
 * <b>Title:</b> ClassUtil.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Utility for various reflection based tools around classes
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.x
 * @since Apr 7, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class ClassUtil {
	
	/**
	 * Can't access if all methods static
	 */
	private ClassUtil() {
		super();
	}
	
	/**
	 * Get a list of fields based on a given annotation
	 * @param clazz the class to search through
	 * @param annotation the annotation to match
	 * @return a list of fields matching the given annotation
	 */
	public static List<Field> getFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Field> fieldsWithAnnotation = new ArrayList<>();
		if (clazz == null) return fieldsWithAnnotation;
		
		for (Field field : clazz.getDeclaredFields()) 
			if (field.isAnnotationPresent(annotation))
				fieldsWithAnnotation.add(field);		
		
		return fieldsWithAnnotation;
	}
	
	/**
	 * Get a list of methods based on a given annotation
	 * @param clazz the class to search through
	 * @param annotation the annotation to match
	 * @return a list of methods matching the given annotation
	 */
	public static List<Method> getMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methodsWithAnnotation = new ArrayList<>();
		if (clazz == null) return methodsWithAnnotation;
		
		for (Method method : clazz.getDeclaredMethods()) 
			if (method.isAnnotationPresent(annotation))
				methodsWithAnnotation.add(method);		
		
		return methodsWithAnnotation;
	}
}
