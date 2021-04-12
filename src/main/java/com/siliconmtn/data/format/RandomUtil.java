package com.siliconmtn.data.format;

import java.util.Random;

/****************************************************************************
 * <b>Title</b>RandomUtil.java
 * 
 * <b>Description: Generate random string from generic character &amp; extended character set </b>
 * 
 * <b>Copyright:</b> Copyright (c) 2021
 * 
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * 
 * @author Bala Gayatri Bugatha
 * @version 1.0
 * @since Jan 29, 2021
 ****************************************************************************/
public class RandomUtil {
	
	//Character set defined and declared
	private static final String LOWER_CASE="abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER_CASE="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBER="0123456789";
	private static final String SYMBOL="!*$#";
	
	/**
	 * Private constructor.  Static only class
	 */
	private RandomUtil() {
		super();
	}
	
	/**
	 * Generates a random string of desired length with string "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!*$#"
	 * @param len of string
	 * @return Generated random string
	 */
	public static String alphaNumericExtended(int len) {
		return generateAnyRandom(len, LOWER_CASE+UPPER_CASE+NUMBER+SYMBOL);
	}
	
	/**
	 * Generates a random string of desired length with string "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
	 * @param len of string
	 * @return Generated random string
	 */
	public static String alphaNumeric(int len) {
		return generateAnyRandom(len, UPPER_CASE+NUMBER);
	}
	
	/**
	 * Helper method for generateValidRandom(int) and generateExtendedRandom(int)
	 * @param len int and str concatenated string 
	 * @return Generated random string
	 */
	private static String generateAnyRandom(int len, String str) {

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < len; i++)
			result.append(str.charAt(new Random().nextInt(str.length())));
		
		return result.toString();
	}

	/**
	 * Generates a random number
	 * @param lowLimitInclusive value indicating lowLimit inclusive 
	 * @param highLimitExclusive Integer value indicating highLimitExclusive
	 * @return Generated Random number in set limits
	 */
	public static int generateRandomNumber(int lowLimitInclusive, int highLimitExclusive) {
		return new Random().ints(lowLimitInclusive, highLimitExclusive).findFirst().getAsInt();
	}
}
