package com.siliconmtn.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: BaseFileTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> CHANGE ME!
 * <b>Copyright:</b> Copyright (c) 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Aug 26, 2022
 * @updates:
 ****************************************************************************/
class BaseFileTest {
	
	private static final String EXAMPLE_1 = "/some/file/name.txt";
	private static final String EXAMPLE_2 = "name.txt";
	private static final String EXAMPLE_3 = "/some/file/name,txt";

	/**
	 * Assigns Example 1 and checks info
	 */
	@Test
	void testAssignFileInfo() {
		BaseFile bf = new BaseFile();
		bf.assignFileInfo(EXAMPLE_1);
		assertEquals(EXAMPLE_1, bf.getPath());
		assertEquals("name.txt", bf.getName());
		assertEquals("txt", bf.getExt());
	}
	
	/**
	 * Assigns null and checks values
	 */
	@Test
	void testAssignFileInfoNull() {
		BaseFile bf = new BaseFile();
		bf.assignFileInfo("");
		assertEquals(null, bf.getPath());
	}
	
	/**
	 * Assigns Example 2 and checks info
	 */
	@Test
	void testAssignFileInfoExample2() {
		BaseFile bf = new BaseFile();
		bf.assignFileInfo(EXAMPLE_2);
		assertEquals(EXAMPLE_2, bf.getPath());
		assertEquals("name.txt", bf.getName());
		assertEquals("txt", bf.getExt());
	}
	
	/**
	 * Assigns Example 1 and checks info
	 */
	@Test
	void testAssignFileInfoExample3() {
		BaseFile bf = new BaseFile();
		bf.assignFileInfo(EXAMPLE_3);
		assertEquals(EXAMPLE_3, bf.getPath());
		assertEquals("name,txt", bf.getName());
		assertEquals(null, bf.getExt());
	}

}
