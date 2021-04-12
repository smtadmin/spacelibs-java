package com.siliconmtn.data.format;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: TimeZoneVOTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> CHANGE ME!!
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
class TimeZoneVOTest {
	private TimeZoneVO txvo;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		txvo = new TimeZoneVO("ID", "NAME", "DESC", "MST");
	}

	/**
	 * Validates the default constructor
	 */
	@Test
	void test() {
		TimeZoneVO txvo = new TimeZoneVO();
		assertNull(txvo.getId());
		assertNull(txvo.getName());
		assertNull(txvo.getDescription());
		assertNull(txvo.getIsoCode());
	}

	/**
	 * Validates the constructor with params and validates that the values were set properly
	 * @throws Exception
	 */
	@Test
	void testTimeZoneVOStringStringStringString() throws Exception {
		assertEquals("ID", txvo.getId());
		assertEquals("NAME", txvo.getName());
		assertEquals("DESC", txvo.getDescription());
		assertEquals("MST", txvo.getIsoCode());
	}

	/**
	 * Validates the setter and getter for the ID
	 * @throws Exception
	 */
	@Test
	void testSetId() throws Exception {
		txvo.setId("MY_NEW_ID");
		assertEquals("MY_NEW_ID", txvo.getId());
	}
	
	/**
	 * Validates the setter and getter for the name
	 * @throws Exception
	 */
	@Test
	void testSetName() throws Exception {
		txvo.setName("MY_NEW_NM");
		assertEquals("MY_NEW_NM", txvo.getName());
	}
	
	/**
	 * Validates the setter and getter for the description
	 * @throws Exception
	 */
	@Test
	void testSetDescription() throws Exception {
		txvo.setDescription("MY_NEW_DESC");
		assertEquals("MY_NEW_DESC", txvo.getDescription());
	}
	
	/**
	 * Validates the setter and getter for the iso code
	 * @throws Exception
	 */
	@Test
	void testSetIsoCode() throws Exception {
		txvo.setIsoCode("MNIC");
		assertEquals("MNIC", txvo.getIsoCode());
	}

	@Test
	void testToString() throws Exception {
		assertTrue(txvo.toString().contains("ID"));
		assertTrue(txvo.toString().contains("NAME"));
		assertTrue(txvo.toString().contains("DESC"));
		assertTrue(txvo.toString().contains("MST"));
	}

}
