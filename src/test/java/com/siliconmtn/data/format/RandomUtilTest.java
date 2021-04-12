package com.siliconmtn.data.format;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: RandomUtilTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description:</b> Test if random string is functioning as expected
 * and number generator is functioning as expected
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 1.0
 * @since Jan 29, 2021
 * @updates:
 ****************************************************************************/

class RandomUtilTest {

	/**
	 * Test method for {@link com.smt.data.format.generateRandomNumber(int, int)}
	 */
	@Test
	void generateRandomNumberTest() {
		int number = RandomUtil.generateRandomNumber(2, 8);
		assertEquals(number, Integer.max(1, number));
		assertEquals(number, Integer.min(9, number));
	}

	/**
	 * Test method for {@link com.smt.data.format.generateExtendedRandom(int) }
	 */
	@Test
	void generateExtendedRandomTest() {
		assertEquals(true,RandomUtil.alphaNumericExtended(4).matches("[0-9a-zA-Z!*$#]{4}"));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.generateExtendedRandom(int) }
	 */
	@Test
	void alphaNumericTest() {
		assertEquals(true,RandomUtil.alphaNumeric(6).matches("[0-9A-Z]{6}"));
	}
}



