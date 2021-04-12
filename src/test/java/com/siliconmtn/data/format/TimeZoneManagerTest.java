package com.siliconmtn.data.format;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// JDK 11.x
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/****************************************************************************
 * <b>Title</b>: TimeZoneManagerTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the data and interfacing into the time zones
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
class TimeZoneManagerTest {
	
	/**
	 * Tests the private constructor
	 * @throws Exception
	 */
	@Test
	void test() throws Exception {
		Constructor<TimeZoneManager> tzmc = TimeZoneManager.class.getDeclaredConstructor();
		tzmc.setAccessible(true);
		assertTrue(Modifier.isPrivate(tzmc.getModifiers()));
		
		// Count the number of time zones
		assertEquals(97, TimeZoneManager.TIME_ZONES.size());
	}

	/**
	 * Tests a genericvo with all time zones
	 * @throws Exception
	 */
	@Test
	void testGetTimeZoneList() throws Exception {
		assertEquals(97, TimeZoneManager.getTimeZoneList().size());
		
		for (TimeZoneVO tvo : TimeZoneManager.TIME_ZONES.values()) {
			assertTrue(tvo.getId().contains("/"));
		}
	}

	/**
	 * Validates getting a time zone vo of the passed key
	 * @throws Exception
	 */
	@Test
	void testGetTimeZone() throws Exception {
		assertNull(TimeZoneManager.getTimeZone(null));
		assertEquals("America/Chicago", TimeZoneManager.getTimeZone("America/Chicago").getId());
	}

	/**
	 * Validates that a given time zone id will return a collection of matching time zones
	 * @throws Exception
	 */
	@Test
	void testGetTimeZoneByIsoCode() throws Exception {
		assertEquals(0, TimeZoneManager.getTimeZoneByIsoCode(null).size());
		assertEquals(3, TimeZoneManager.getTimeZoneByIsoCode("MST").size());
	}

}
