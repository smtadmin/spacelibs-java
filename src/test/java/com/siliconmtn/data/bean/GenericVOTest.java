package com.siliconmtn.data.bean;

// JUnit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//JDK 11.x
import java.util.ArrayList;

/****************************************************************************
 * <b>Title</b>: GenericVOTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the generic vo data bean
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
class GenericVOTest {
	
	private GenericVO gvo;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		gvo = new GenericVO("KEY", "VALUE");
	}

	/**
	 * Validates the default constructor and the 2 string constructor
	 */
	@Test
	void test() {
		assertEquals("KEY", gvo.getKey());
		assertEquals("VALUE", gvo.getValue());
		
		gvo = new GenericVO();
		assertNull(gvo.getKey());
		assertNull(gvo.getValue());
	}
	
	/**
	 * Validates the getting and setting of the key
	 * @throws Exception
	 */
	@Test
	void testGetKey() throws Exception {
		gvo.setKey("NEW_KEY");
		assertEquals("NEW_KEY", gvo.getKey());
	}
	
	/**
	 * Validates the getting and setting of the value
	 * @throws Exception
	 */
	@Test
	void testGetValue() throws Exception {
		gvo.setValue("NEW_VALUE");
		assertEquals("NEW_VALUE", gvo.getValue());
	}

	/**
	 * Validates the data in the key and value are displayed int he to string method
	 * @throws Exception
	 */
	@Test
	void testToString() throws Exception {
		assertTrue(gvo.toString().contains("KEY"));
		assertTrue(gvo.toString().contains("VALUE"));
	}

	/**
	 * Validates comparing 2 GenericVO classes
	 * @throws Exception
	 */
	@Test
	void testCompareTo() throws Exception {
		GenericVO vo = new GenericVO();
		assertEquals(0, gvo.compareTo(vo));
		assertEquals(0, gvo.compareTo(null));
		
		vo.setKey("KEY");
		assertEquals(0, gvo.compareTo(vo));
		
		vo.setKey("KEY1");
		assertEquals(-1, gvo.compareTo(vo));
		
		gvo.setKey(null);
		assertEquals(0, gvo.compareTo(vo));
	}

	/**
	 * Determines if 2 GenericVOs are equal
	 * @throws Exception
	 */
	@Test
	void testEquals() throws Exception {
		GenericVO vo = new GenericVO();
		assertNotEquals(vo, gvo);
		assertNotEquals(null, gvo);
		
		vo.setKey("KEY");
		assertEquals(vo, gvo);
		
		gvo.setKey(new ArrayList<Integer>());
		assertNotEquals(gvo, vo);
	}

	/**
	 * Ensures the hashcode returned is consistent
	 * @throws Exception
	 */
	@Test
	void testHashCode() throws Exception {
		assertEquals(233, gvo.hashCode());
		gvo.setKey("MY_NEW_KEY");
		assertNotEquals(233, gvo.hashCode());
		assertEquals(823, gvo.hashCode());
	}
}
