package com.siliconmtn.data.text;

// JDK 11.x
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/****************************************************************************
 * <b>Title</b>: StringUtil.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Extends the apache commons String Utils class with some additional
 * capabilities
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 12, 2021
 * @updates:
 ****************************************************************************/
public class StringUtil {

	/**
	 * Assign private constructor as all are static
	 */
	private StringUtil() {
		super();
	}
	
	/**
	 * Determines if the provided string is empty, null or blank
	 * @param str The value to evaluate for empty
	 * @return true if empty.  False otherwise
	 */
	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.toString().trim().length() == 0);
	}
	
	
	/**
	 * <p>
	 * Counts how many times the char appears in the given string.
	 * </p>
	 *
	 * <p>
	 * A {@code null} or empty ("") String input returns {@code 0}.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.countMatches(null, *)       	= 0
	 * StringUtil.countMatches("", *)         	= 0
	 * StringUtil.countMatches("abba", 0)  		= 0
	 * StringUtil.countMatches("abba", 'a')   	= 2
	 * StringUtil.countMatches("abba", 'b')  	= 2
	 * StringUtil.countMatches("abba", 'x')	 	= 0
	 * </pre>
	 *
	 * @param str the CharSequence to check, may be null
	 * @param ch  the char to count
	 * @return the number of occurrences, 0 if the CharSequence is {@code null}
	 */
	public static int countMatches(final CharSequence str, final char ch) {
		if (isEmpty(str))  return 0;
		
		return countMatches(str.toString(), String.valueOf(ch));
	}

	/**
	 * <p>
	 * Counts how many times the substring appears in the larger string. Note that
	 * the code only counts non-overlapping matches.
	 * </p>
	 *
	 * <p>
	 * A {@code null} or empty ("") String input returns {@code 0}.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.countMatches(null, *)       = 0
	 * StringUtil.countMatches("", *)         = 0
	 * StringUtil.countMatches("abba", null)  = 0
	 * StringUtil.countMatches("abba", "")    = 0
	 * StringUtil.countMatches("abba", "a")   = 2
	 * StringUtil.countMatches("abba", "ab")  = 1
	 * StringUtil.countMatches("abba", "xxx") = 0
	 * StringUtil.countMatches("ababa", "aba") = 1
	 * </pre>
	 *
	 * @param str the CharSequence to check, may be null
	 * @param sub the substring to count, may be null
	 * @return the number of occurrences, 0 if either CharSequence is {@code null}
	 */
	public static int countMatches(final String str, final String sub) {
        if (isEmpty(str) || isEmpty(sub)) return 0;

        int count = 0;
        int len = Math.floorDiv(str.length(), sub.length());
        for (int i = 0; i < len; i++ ) {
           int end = (i + 1) * sub.length();
           int start = i * sub.length();
           String val = str.substring(start, end);
           if (sub.equals(val)) count++;
        }
        
        return count;
	}

	
	/**
	 * Returns the provided string if it is not null, blank or has whitespace.
	 * Otherwise, we return the defaultVal
	 * @param val String value to evaluate
	 * @param defaultVal Value to be returned if empty
	 * @return trimmed string
	 */
	public static String defaultString(String val, String defaultVal) {
		if (val == null || val.isBlank()) return defaultVal == null ? "" : defaultVal;
		else return val.trim();
	}
	
	/**
	 * Returns the provided string if it is not null, blank or has whitespace.
	 * @param val value to check.
	 * @return trimmed string
	 */
	public static String defaultString(String val) {
		if (val == null || val.isBlank()) return "";
		else return val.trim();
	}
	
	/**
	 * Returns an array of the index for each of the matching 
	 * @param str String to be indexed
	 * @param searchStr Value to find inthe provided string
	 * @return Arrays of indexes for the matches
	 */
	public static int[] everyIndexOf(String str, String searchStr) {
		if (isEmpty(str) || isEmpty(searchStr)) return new int[0];
		
		int[] items = new int[countMatches(str, searchStr)];
		int loc = -1;
		
		for (int i=0; i < items.length; i++) {
			
			loc = str.indexOf(searchStr, loc + 1);
			items[i] = loc;
		}

		return items;
	}
	
	/**
	 * Deletes all of the non-alpha (Aa-Zz) and NoN-Numeric (0-9) characters in the data
	 * @param data String to be parsed
	 * @return parsed data.
	 */
	public static String removeNonAlphaNumeric(String data) {
		return removeNonAlphaNumeric(data, true);
	}

	/**
	 * Deletes all of the non-alpha (Aa-Zz) and NoN-Numeric (0-9) characters in the data
	 * @param data String to remove characters form
	 * @param removeSpace Provides an option to remove or leave spaces.  Default is to remove
	 * @return parsed data.
	 */
	public static String removeNonAlphaNumeric(String data, boolean removeSpace) {
		if (data == null || data.length() < 1) return data;

		StringBuilder newVal = new StringBuilder();
		for (char a : data.toCharArray()) {
			if (Character.isWhitespace(a) || Character.isDigit(a) || Character.isLetter(a)) {
				if (Character.isWhitespace(a) && removeSpace) continue;
				newVal.append(a);
			}
		}
		
		return newVal.toString();
	}
	
	/**
	 * returns an obfuscated version of the provided email address.
	 * e.g.: j*****n@siliconmtn.com
	 * @param email Email address to be obfuscated
	 * @return Obfuscated email address
	 */
	public static String obfuscateEmail(String email) {
		if (isEmpty(email)) return email;

		StringBuilder sb = new StringBuilder(email.length());
		for (int x=0; x < email.length(); x++ ) {
			sb.append(x > 0 && x < email.indexOf('@') ? '*' : email.charAt(x));
		}
		return sb.toString();
	}
	
	/**
	 * Deletes all of the non-numeric (0-9) characters in the data
	 * @param data String to be parsed
	 * @return parsed data.
	 */
	public static String removeNonNumeric(String data) {
		if (isEmpty(data)) return null;

		StringBuilder newVal = new StringBuilder();
		for (char a : data.toCharArray()) {
			if (Character.isDigit(a) || (a == '.')) newVal.append(a);
		}
		
		return newVal.toString();
	}
	
	/**
	 * Uses reflection to find all fields in the class and 
	 * output the data in the following format:<br>
	 * fieldName:value|fieldName:value....|fieldName:value
	 * @param o Object to get values for.
	 * @return formatted string of variables
	 */
	public static String getToString(Object o) {
		return getToString(o, ":");
	}
	
	/**
	 * Uses Jackson JSON parser to convert the supplied pojo into a JSON object
	 * @param o POJO to convert to JSON
	 * @return Json Object.  Delimited string if JSON conversion fails
	 */
	public static String getJsonString(Object o) {

		try {
			ObjectMapper om = new ObjectMapper();
			return om.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
	
	/**
	 * Uses reflection to find all fields in the class and 
	 * output the data in the following format:<br>
	 * fieldName:value|fieldName:value....|fieldName:value
	 * @param o Object to get values for.
	 * @param sep separator between values
	 * @return formatted string of variables
	 */
	public static String getToString(Object o, String sep) {
		if (o == null) return "";
		
		if (o.getClass().isArray()) {
			return getToString((Object[])o, sep);
		}
		
		Class<?> c = o.getClass();
		Method[] methods = c.getMethods();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < methods.length; i++) {
			String name = methods[i].getName();
			if (methods[i].getParameterTypes().length > 0 || methods[i].getModifiers() != Modifier.PUBLIC) continue;

			try {
				if ((name.startsWith("get") || name.startsWith("is"))) {
					sb.append(name).append(":");
					sb.append(methods[i].invoke(o)).append(sep);
				}
			} catch (Exception e) { /* Nothing to do here */ }
			
		}

		return sb.toString();
	}
	
	/**
	 * Formats an array into a delimited String
	 * @param arr Array to iterate.  
	 * @param delim Character to use as the delimiter
	 * @return Delimited String. "" if array is empty
	 */
	private static String getToString(Object[] arr, String delim) {
		if (arr.length == 0) return ""; 
		
		StringBuilder sb = new StringBuilder("[");
		
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) sb.append(delim);
			sb.append(arr[i] == null ? "" : arr[i]);
		}

		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 * Check if a given string is a valid UUID and return it as a UUID or null
	 * @param potentialUUID the string to test
	 * @return the UUID if valid, else null
	 */
	public static UUID getUUID(String potentialUUID) {
		if (potentialUUID == null || potentialUUID.length() != 36)
			return null;		
		
		try {
			return UUID.fromString(potentialUUID);
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Pads the supplied character to fill in the empty spaces.  For example, 
	 * to create a string with a length of 5, and fill blanks with 0 ... to the 
	 * left or right
	 * <p>padLeft("d", "0", 5); </p>
	 * <p>This will return "0000d"</p>
	 * <p> If the length of the src String is greater than the length, the
	 * returned string will be truncated</p>
	 * @param src Source String to pad
	 * @param fill Character to use to pad the string
	 * @param length final length of the string.  All characters less than the length 
	 * will be padded with the fill character
	 * @return Left padded string
	 */
	public static String padLeft(String src, char fill, int length) {
		return  pad(src, fill, length, false);
	}
	
	/**
	 * Pads the supplied character to fill in the empty spaces.  For example, 
	 * to create a string with a length of 5, and fill blanks with 0 ...
	 * This is a function that performs either padding
	 * @param src Source String to pad
	 * @param fill Character to use to pad the string
	 * @param length final length of the string.  All characters less than the length 
	 * will be padded with the fill character
	 * @param isPadRight Boolean to determine if the padding is left or right
	 * @return Padded String (left or right)
	 */
	private static String pad(String src, char fill, int length, boolean isPadRight) {
		if (src == null) src = ""; 
		StringBuilder sb = new StringBuilder();
		int startLoc = length - src.length();

		int stringLen = src.length();
		if (stringLen > length) stringLen = length;
		if (isPadRight) sb.append(src.substring(0, stringLen));
		for (int i = 0; i < startLoc; i++) {
			sb.append(fill);
		}
		
		if (! isPadRight)sb.append(src.substring(0, stringLen));

		return sb.toString();
	}

	/**
	 * Pads the supplied character to fill in the empty spaces.  For example, 
	 * to create a string with a length of 5, and fill blanks with 0 ...
	 * <p>padLeft("d", "0", 5); </p>
	 * <p>This will return "d0000"</p>
	 * <p> If the length of the src String is greater than the length, the
	 * returned string will be truncated</p>
	 * @param src String to be padded
	 * @param fill Characters to fill the padding
	 * @param length length of the returned string
	 * @return Right padded string
	 */
	public static String padRight(String src, char fill, int length) {
		return pad(src, fill, length, true);
	}
}
