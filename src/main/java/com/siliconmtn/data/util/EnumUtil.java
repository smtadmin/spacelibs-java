package com.siliconmtn.data.util;

import java.util.ArrayList;
import java.util.List;

/********************************************************************
 * <b>Title: </b>EnumUtil.java
 * <b>Description: </b>Utility Class Housing Enum Utilities and time savers
 * <b>Copyright: </b>Copyright (c) 2017
 * <b>Company: </b>Silicon Mountain Technologies
 * @author James Camire
 * @version 3.x
 * @since Dec 13, 2017
 * Last Updated: 
 *******************************************************************/
public class EnumUtil {
	
	/**
	 * Can't access if all methods static
	 */
	private EnumUtil() {
		super();
	}

	/**
	 * Takes a String and converts it to an enum.  Must use the Enum CLass to make this work.  For example:
	 * EnumTest et = new EnumTest();
	 * SomeVal sv = SomeVal.val_two;
	 * SomeVal myVal = et.safeValueOf(SomeVal.class, "val_two");
	 * (SomeVal.equals(myVal)) // Evaluates to true
	 * @param <E> Type of the Enum
	 * @param enumType Enum class to pass.  For Example: MyEnum.class
	 * @param val String value to assign to the enum
	 * @return Typed Enum.  Null if val can't be converted.
	 */
	public static <E extends Enum<E>>  E safeValueOf(Class<E> enumType, String val) {
		return safeValueOf(enumType, val, null);
	}
	
	/**
	 * Takes a String and converts it to an enum.  Must use the Enum CLass to make this work.  For example:
	 * EnumTest et = new EnumTest();
	 * SomeVal sv = SomeVal.val_two;
	 * SomeVal myVal = et.safeValueOf("SomeVal", "val_two");
	 * (SomeVal.equals(myVal)) // Evaluates to true
	 * @param <E> Type of the Enum
	 * @param enumStringType String class of the enum to pass.  For Example: MyEnum.class
	 * @param val String value to assign to the enum
	 * @return Typed Enum.  Null if val can't be converted.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>>  E safeValueOf(String enumStringType, String val) {
		Class<E> enumType;
		try {
			enumType = (Class<E>)Class.forName(enumStringType);
			return safeValueOf(enumType, val, null);
		} catch (Exception e) { /* Nothing to do */  }
		
		return null;
	}
	
	/**
	 * Takes a String and converts it to an enum.  Must use the Enum CLass to make this work.  For example:
	 * EnumTest et = new EnumTest();
	 * SomeVal sv = SomeVal.val_two;
	 * SomeVal myVal = et.safeValueOf(SomeVal.class, "val_two");
	 * (SomeVal.equals(myVal)) // Evaluates to true
	 * @param <E> Type of the Enum
	 * @param enumType Enum class to pass.  For Example: MyEnum.class
	 * @param val String value to assign to the enum
	 * @param defaultVal Default value if no match
	 * @return Typed Enum.  Null if val can't be converted.
	 */
	public static <E extends Enum<E>>  E safeValueOf(Class<E> enumType, String val, E defaultVal) {
		E e = null;
		
		try {
			e = Enum.valueOf(enumType, val);
		} catch (Exception ex) {
			if (defaultVal != null) e = defaultVal;
		}
		
		return e;
	}
	
	/**
	 * Takes an array of string an converts each one using the single safeValueOf
	 * method and returns the collection
	 * @param <E> Type of the Enum
	 * @param enumType numType Enum class to pass.  For Example: MyEnum.class
	 * @param vals Array of strings to be converted.  Null if string doesn't match
	 * @return Typed Enum List.  Matches each item in the provided collection
	 */
	public static <E extends Enum<E>>  List<E> safeValuesOf(Class<E> enumType, String[] vals) {
		List<E> enums = new ArrayList<>();
		
		for (String val : vals) {
			E e = safeValueOf(enumType, val.trim(), null);
			if (e != null) enums.add(e);
		}
		
		return enums;
	}

}
