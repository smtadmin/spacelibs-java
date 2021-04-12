package com.siliconmtn.data.format;

import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: BooleanUtil.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Provides utilities to convert and manage boolean primitives
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 20, 2021
 * @updates:
 ****************************************************************************/
public class BooleanUtil {

	/**
	 * Not accessible.  Class will be only static methods
	 */
	private BooleanUtil() {
		super();
	}
	
	/**
	 * Converts an int into a Boolean
	 * 
	 * @param val Value to be converted
	 * @return Boolean of true = 1, false for all other vals
	 */
	public static boolean toBoolean(int val) {
		return  (val == 1);
	}
	
	/**
	 * Converts an int into a Boolean
	 * 
	 * @param val Value to be converted
	 * @return Boolean of true = 1, false for all other vals
	 */
	public static boolean toBoolean(long val) {
		return  (val == 1);
	}
	
	/**
	 * Converts an char into a Boolean
	 * 
	 * @param val Value to be converted
	 * @return Boolean of 'y'/'y' = 1, false for all other vals
	 */
	public static boolean toBoolean(char val) {
		return  (val == 'y' || val == 'Y' || val == '1');
	}
	
	/**
	 * Converts a String into an Boolean. Looks for the following values case
	 * insensitive:
	 * True
	 * False
	 * Yes
	 * No
	 * Y
	 * N
	 * On
	 * Off
	 * 
	 * @param obj String to be converted
	 * @return boolean value of false if conversion fails
	 */
	public static boolean toBoolean(Object obj) {
		// Make sure data is passed in.
		if (obj == null) return false;

		// If the object is a boolean, return it
		if (obj instanceof Boolean)	return ((Boolean) obj);

		//check for boxed ints
		if (obj instanceof Integer)	return toBoolean(((Integer) obj).intValue());

		//check for boxed long
		if (obj instanceof Long) return toBoolean(((Long) obj).longValue());
				
		//check for boxed chars
		if (obj instanceof Character) return toBoolean(((Character) obj).charValue());
				
		// Make sure the passed data is a String
		if (!(obj instanceof String)) return false;

		return checkStringVals(StringUtil.removeNonAlphaNumeric((String) obj));
	}
	
	/**
	 * Convert a String into an Boolean object.
	 * Return a Boolean with a value of Boolean.false if the conversion fails
	 * @param val
	 * @return
	 */
	private static boolean checkStringVals(String val) {
		switch(val.toUpperCase()) {
			case "Y" :
			case "YES" :
			case "TRUE" :
			case "ON" :
			case "1" :
				return true;
			default:
				return false;
		}
	}
}
