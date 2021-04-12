package com.siliconmtn.data.format;

import java.text.NumberFormat;
import java.text.ParseException;

import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: NumberUtil.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Number utilities utilized to convert strings into various
 * number types.  Works with mixed values and strings in conversion
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 20, 2021
 * @updates:
 ****************************************************************************/
public class NumberUtil {

	/**
	 * Not implemented as all methods are designed to be static
	 */
	private NumberUtil() {
		super();
	}
	
	/**
	 * Converts a String into an int
	 * Return an Integer with a value of 0 if the conversion fails
	 * 
	 * @param val String to be converted
	 * @return Integer of value 0 if conversion fails
	 */
	public static int toInt(String val) {
		return toInt(val, 0, true);
	}
	
	/**
	 * Converts a String into an int
	 * Return an Integer with a value of 0 if the conversion fails
	 * 
	 * @param val String to be converted
	 * @param defaultVal the value to assign if the val is empty or throws a parse error
	 * @return Integer of value 0 if conversion fails
	 */
	public static int toInt(String val, int defaultVal) {
		return toInt(val, defaultVal, true);
	}
	
	/**
	 * Converts a String to an int. If data is not a number , use the
	 * default val.  this method acts as the controller.
	 * 
	 * @param val String to be converted
	 * @param defaultVal Default value for non String or non value in string
	 * @param removeNonAlphaNum boolean to indicate whether non alphas should be 
	 * stripped beforee conversion
	 * @return Integer, default val if failed
	 */
	public static int toInt(String val, int defaultVal, boolean removeNonAlphaNum) {
		if (val == null || val.isEmpty()) return defaultVal;
		
		//for long and int remove data after the "point"
		val = makeWholeNumber(val.trim());
		Long l = Long.valueOf(processLongInt(val, checkForNegativeValue(val), removeNonAlphaNum, defaultVal));
		return (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) ? defaultVal : l.intValue();
	}

	/**
	 * Converts a String into an Long
	 * 
	 * @param val String to be converted
	 * @return Long of value 0 if conversion fails
	 */
	public static long toLong(String val) {
		return toLong(val, 0);
	}
	
	/**
	 * Converts a String into an Long
	 * 
	 * @param val String to be converted
	 * @param defaultValue Default value to assign if val is empty
	 * @return Long of value 0 if conversion fails
	 */
	public static long toLong(String val, long defaultValue) {
		return toLong(val, defaultValue, true);
	}

	/**
	 * Converts a String into a long, acts as the controller for the process
	 * 
	 * @param val Value to convert
	 * @param defaultValue Assigned value if val is empty
	 * @param removeNonAlphaNum boolean to indicate whether non alphas should be 
	 * stripped before conversion
	 * @return Lnog value represneting the passed value and metadata
	 */
	public static long toLong(String val, long defaultValue, boolean removeNonAlphaNum) {
		// Convert a String into an long
		// Return a Long of the default value if the conversion fails
		if (val == null || val.isEmpty()) return defaultValue;
		
		// Remove the long identifier if exists
		val = val.replace("l", "");
		
		//for long and int remove data after the "point"
		val = makeWholeNumber(val.trim());

		//return a processed long value
		return processLongInt(val,checkForNegativeValue(val),removeNonAlphaNum ,defaultValue  );

	}
	
	/**
	 * process the conversion from String to Long
	 * @param val Value to process
	 * @param negative is the number negative
	 * @param removeNonAlphaNum removes all non-numbers in the val
	 * @param defaultValue if empty/null/error converting, return this value
	 * @return long Converted value
	 */
	private static long processLongInt(String val,boolean negative, boolean removeNonAlphaNum, long defaultValue) {

		long convertedString = 0;

		try {	
			// Only remove alpha characters if requested.  replace the negative sign that was removed by remove non alpha
			if (removeNonAlphaNum) {
				convertedString = Long.parseLong(signedValue(StringUtil.removeNonAlphaNumeric(val), negative));
			}else {
				//else just try and parse the string as is
				convertedString = Long.parseLong(val);
			}

		} catch (Exception nfe) {
			return defaultValue;
		}
		return convertedString;
	}
	
	/**
	 * Converts a String into an Float
	 * 
	 * @param val String to be converted
	 * @return Float value 0.0 if conversion fails
	 */
	public static float toFloat(String val) {
		return toFloat(val, 0);
	}
	
	/**
	 * Converts a String into an Float
	 * @param val Value to convert
	 * @param defaultValue Default value if val is null/empty
	 * @return Float representation of the provided value
	 */
	public static float toFloat(String val, float defaultValue) {
		return toFloat(val,defaultValue, true);
	}
	
	/**
	 * converts a string into a Float, acts as the controller for the process
	 * @param val String to convert to a float
	 * @param defaultValue Value to assign if val is empty or can't be converted
	 * @param removeNonAlphaNum boolean to indicate whether non alphas should be 
	 * stripped before conversion
	 * @return Float value representing the passed data
	 */
	public static float toFloat(String val, float defaultValue, boolean removeNonAlphaNum) {
		// Convert a String into an float.
		// Return a Float of the default value if the conversion fails
		if (val == null || val.isEmpty()) return defaultValue;

		//return processed number
		return processFloat(val.trim(),checkForNegativeValue(val), removeNonAlphaNum, defaultValue);		
	}
	
	/**
	 * processes the conversion from String to Float
	 * @param value String to convert to a float
	 * @param negative Boolean to know before removing non-alphas
	 * @param indexOfDecimalPoint Need to have this assigned if parsing non alphas
	 * @param removeNonAlphaNum boolean to indicate whether non alphas should be 
	 * stripped before conversion
	 * @param defaultValue Value to assign if value is empty or can't be converted
	 * @return
	 */
	private static float processFloat(String value, boolean negative, boolean removeNonAlphaNum, float defaultValue) {
		try {
			return Float.parseFloat(formatValue(value, negative, removeNonAlphaNum));
		} catch (Exception nfe) {
			return defaultValue;
		}
	}
	
	/**
	 * Converts a String into a Double
	 * 
	 * @param val String to be converted
	 * @return Double of value 0.0 if conversion fails
	 */
	public static double toDouble(String val) {
		return toDouble(val , 0.0);
	}

	/**
	 * Converts a String into a double with default double value.
	 * @param val String to convert
	 * @param d Default value if val is empty
	 * @return Double value from meta data
	 */
	public static double toDouble(String val, double d) {
		return toDouble(val, d, true);
	}
	
	/**
	 * Converts a String into a Double, acts as the controller for the process
	 * @param val Value to convert
	 * @param defaultValue if null/empty or error, return default value
	 * @param removeNonAlphaNum removes all non-alpha characters from the value
	 * @return Double value from meta data
	 */
	public static double toDouble(String val, double defaultValue, boolean removeNonAlphaNum) {
		// Convert a String into an Double object.
		// Return a Double of the default value if the conversion fails
		if (val == null || val.isEmpty()) return defaultValue;

		//return processed value
		return processDouble(val.trim(), checkForNegativeValue(val), removeNonAlphaNum, defaultValue);
	}
	
	/**
	 * processes the conversion from String to double
	 * @param value String value to convert
	 * @param negative is a negative number
	 * @param removeNonAlphaNum removes all non-alpha characters from the value
	 * @param defaultValue if null/empty or error, return default value
	 * @return Double value from meta data
	 */
	private static double processDouble(String value, boolean negative, boolean removeNonAlphaNum, double defaultValue) {
		try {
			return Double.parseDouble(formatValue(value, negative, removeNonAlphaNum));
		} catch (Exception nfe) {
			return defaultValue;
		}
	}
	
	/**
	 * Converts the string parameters into a number parseable string
	 * @param value String value
	 * @param negative is a negative number
	 * @param removeNonAlphaNum removes all non-alphanumeric characters (such as $, ",", etc ...
	 * @return String that is number parsable
	 */
	private static String formatValue(String value, boolean negative, boolean removeNonAlphaNum) {
		if (removeNonAlphaNum) {
			int index = value.indexOf(".");

			// if there is a decimal, split on the first decimal, remove alphas and put back together
			if (index > -1) {
				String major = "";
				String minor = "";
				major = StringUtil.removeNonAlphaNumeric(value.substring(0, index));
				if (index + 1 < value.length()) minor = StringUtil.removeNonAlphaNumeric(value.substring(index + 1));

				value = (negative ? "-" : "") + major + "." + minor;
			} else {
				// If no decimals, process the request normally
				String val = StringUtil.removeNonAlphaNumeric(value);
				value = signedValue(val, negative);
			}
		}
		
		return value;
	}
	
	/**
	 * This method will re-apply he negative flag so it is not complete missed when we remove alpha
	 * @param val String value to be checked
	 * @param negativeStringFlag is the number negative?
	 * @return value with negative value applied
	 */
	private static String signedValue(String val, boolean isNegative) {
		StringBuilder sb = new StringBuilder((val.length() +1));
		sb.append(val);
		
		//replaces the dash if it used to be present.
		if (isNegative) sb.insert(0, "-");
		
		return sb.toString();
	}
	
	
	/**
	 * This method removes the point and any digits to the right
	 * @param val value to strip
	 * @return stripped value with no decimal place
	 */
	private static String makeWholeNumber(String val) {
		return val.indexOf(".") != -1 ? val.substring(0, val.indexOf(".")) : val;
	}
	
	/**
	 * this method checks the incoming string for a first char of -
	 * returns a boolean indicating the string contains a negative number
	 * @param val String value to check
	 * @return true if number is negative
	 */
	private static boolean checkForNegativeValue(String val) {
		return "-".equals(val.substring(0, 1));
	}
	
	/**
	 * Determines if the provided String is a number
	 * @param numberText String to test
	 * @return Number object.  Null if not a number
	 */
	public static Number getNumber(String numberText) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		try {
			return numberFormat.parse(numberText);
		} catch (ParseException e) {
			return null;
		}
	}
}
