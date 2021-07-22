package com.siliconmtn.data.format;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: NumberUtilTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the number utility class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 20, 2021
 * @updates:
 ****************************************************************************/
class NumberUtilTest {
	
	/**
	 * Tests converting a string into a number
	 * @throws Exception
	 */
	@Test
	void testToIntString() throws Exception {
		assertEquals(0, NumberUtil.toInt(null));
		assertEquals(0, NumberUtil.toInt(""));
		assertEquals(0, NumberUtil.toInt("abc"));
		assertEquals(123, NumberUtil.toInt("123"));
		assertEquals(123, NumberUtil.toInt("123.45"));
		assertEquals(-123, NumberUtil.toInt("-123.45"));
		assertEquals(-123, NumberUtil.toInt("-123.45", 1));
		
		assertEquals(1, NumberUtil.toInt("1234567890123445667", 1));
		assertEquals(1, NumberUtil.toInt("-1234567890123445667", 1));
	}

	/**
	 * Test the various patterns for converting a string to an int
	 * @throws Exception
	 */
	@Test
	void testProcessInt() throws Exception {
		assertEquals(-123, NumberUtil.toInt("-123.45", 0, false));
		assertEquals(123, NumberUtil.toInt("+123.45", 0, false));
		assertEquals(123, NumberUtil.toInt("+123^&#.45", 0, true));
		assertEquals(10, NumberUtil.toInt("+123^&#.45", 10, false));
	}

	/**
	 * Test the various patterns for converting a string to a long
	 * @throws Exception
	 */
	@Test
	void testToLongString() throws Exception {
		assertEquals(0, NumberUtil.toLong(null));
		assertEquals(0, NumberUtil.toLong(""));
		assertEquals(0, NumberUtil.toLong("abc"));
		assertEquals(-123, NumberUtil.toLong("-123.45"));
		assertEquals(-123, NumberUtil.toLong("-123.45", 0, false));
		assertEquals(1234567890123445567l, NumberUtil.toLong("1234567890123445567", 1));
		assertEquals(1234567890123445567l, NumberUtil.toLong("1234567890123445567l", 1));
	}

	/**
	 * Tests the conversion of a string to a float
	 * @throws Exception
	 */
	@Test
	void testToFloatString() throws Exception {
		assertEquals(0, NumberUtil.toFloat(null));
		assertEquals(0, NumberUtil.toFloat(""));
		assertEquals(6f, NumberUtil.toFloat("6f"));
		assertEquals(-6f, NumberUtil.toFloat("-6f"));
		assertEquals(6.543f, NumberUtil.toFloat("6.543"));
		assertEquals(-6.543f, NumberUtil.toFloat("-6.543"));
		assertEquals(0, NumberUtil.toFloat("6.54.-&^3", 0, false));
		assertEquals(6.543f, NumberUtil.toFloat("6.54.-&^3", 0, true));
		assertEquals(6.543123f, NumberUtil.toFloat("6.543.123"));
		assertEquals(6543f, NumberUtil.toFloat("6543."));
		assertEquals(-.6543123f, NumberUtil.toFloat("-.6543123"));
	}

	/**
	 * Tests the conversion of a string to a double
	 * @throws Exception
	 */
	@Test
	void testToDoubleStringDoubleBoolean() throws Exception {
		assertEquals(0, NumberUtil.toDouble(null));
		assertEquals(0, NumberUtil.toDouble(""));
		assertEquals(6.0, NumberUtil.toDouble("6"));
		assertEquals(6.56, NumberUtil.toDouble("6.56"));
		assertEquals(-6.56, NumberUtil.toDouble("-6.56"));
		assertEquals(-6.56, NumberUtil.toDouble("-6.56", 0.0, false));
		assertEquals(0, NumberUtil.toDouble("-6.54.-&^3", 0.0, false));
		assertEquals(6.0, NumberUtil.toDouble("6.", 0.0, false));
		assertEquals(6.0, NumberUtil.toDouble("6.", 0.0, true));
		assertEquals(-6.0, NumberUtil.toDouble("-6.", 0.0, true));
	}

	@Test
	void testGetNumber() throws Exception {
		assertEquals(Long.valueOf(5), NumberUtil.getNumber("5"));
		assertEquals(Double.valueOf(5.5f), NumberUtil.getNumber("5.5f"));
		assertEquals(Double.valueOf(23456.1234567), NumberUtil.getNumber("23456.1234567"));
		assertEquals(Double.valueOf(23456.1234567), NumberUtil.getNumber("23456.1234567"));
		assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), NumberUtil.getNumber("8289234523432343234322039840983409823409823049809823461234556687678923452343234323432203984098340982340982304980982346123455668767892345234323432343220398409834098234098230498098234612345566876789234523432343234322039840983409823409823049809823461234556687678923452343234323432203984098340982340982304980982346.1234567"));
		assertNull(NumberUtil.getNumber("Test"));
	}


}
