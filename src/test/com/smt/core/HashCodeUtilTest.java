package com.smt.core;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.smt.core.HashCodeUtil;

// JDK 11.x
import java.util.Arrays;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: HashCodeUtilTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> validates the HaschCodeUtil class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 12, 2021
 * @updates:
 ****************************************************************************/
class HashCodeUtilTest {

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(boolean)}.
	 */
	@Test
	void testHashBoolean() {
		assertEquals(HashCodeUtil.hash(true), 852);
		assertEquals(HashCodeUtil.hash(false), 851);
	}

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(char)}.
	 */
	@Test
	void testHashChar() {
		assertEquals(HashCodeUtil.hash('c'), 950);
		assertEquals(HashCodeUtil.hash('%'), 888);
	}

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(int)}.
	 */
	@Test
	void testHashInt() {
		assertEquals(HashCodeUtil.hash(24567), 25418);
		assertEquals(HashCodeUtil.hash(0), 851);
		assertEquals(HashCodeUtil.hash(-128), 723);
	}

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(float)}.
	 */
	@Test
	void testHashFloat() {
		assertEquals(HashCodeUtil.hash(12.32f), 785);
		assertEquals(HashCodeUtil.hash(-1.123f), 975);
	}

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(double)}.
	 */
	@Test
	void testHashDouble() {
		assertEquals(HashCodeUtil.hash(12.32), 1333);
		assertEquals(HashCodeUtil.hash(-1.123), 1393);
	}

	/**
	 * Test method for {@link com.smt.core.HashCodeUtil#hash(int, java.lang.Object)}.
	 */
	@Test
	void testHashIntObject() {
		String[] data = new String[] {"one", "two", "three"};
		List<String> list = Arrays.asList(data);
		
		assertEquals(HashCodeUtil.hash(data), 1204);
		assertEquals(HashCodeUtil.hash("Hello World"), 1052);
		assertEquals(HashCodeUtil.hash(list), 1540);
		assertEquals(HashCodeUtil.hash(null), 0);
	}

}
