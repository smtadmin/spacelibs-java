package com.siliconmtn.core;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;

// JDK 11.x
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(boolean)}.
	 */
	@Test
	void testHashBoolean() {
		
		assertEquals(852, HashCodeUtil.hash(true));
		assertEquals(851, HashCodeUtil.hash(false));
	}

	/**
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(char)}.
	 */
	@Test
	void testHashChar() {
		assertEquals(950,HashCodeUtil.hash('c'));
		assertEquals(888, HashCodeUtil.hash('%'));
	}

	/**
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(int)}.
	 */
	@Test
	void testHashInt() {
		assertEquals(25418, HashCodeUtil.hash(24567));
		assertEquals(851, HashCodeUtil.hash(0));
		assertEquals(723, HashCodeUtil.hash(-128));
	}

	/**
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(float)}.
	 */
	@Test
	void testHashFloat() {
		assertEquals(785, HashCodeUtil.hash(12.32f));
		assertEquals(975, HashCodeUtil.hash(-1.123f));
	}

	/**
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(double)}.
	 */
	@Test
	void testHashDouble() {
		assertEquals(1333, HashCodeUtil.hash(12.32));
		assertEquals(1393, HashCodeUtil.hash(-1.123));
	}

	/**
	 * Test method for {@link com.siliconmtn.core.HashCodeUtil#hash(int, java.lang.Object)}.
	 */
	@Test
	void testHashIntObject() {
		String[] data = new String[] {"one", "two", "three"};
		List<String> list = Arrays.asList(data);
		
		assertEquals(1204, HashCodeUtil.hash(data));
		assertEquals(1052, HashCodeUtil.hash("Hello World"));
		assertEquals(1540, HashCodeUtil.hash(list));
		assertEquals(0, HashCodeUtil.hash(null));
	}

}
