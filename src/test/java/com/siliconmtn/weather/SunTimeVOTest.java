package com.siliconmtn.weather;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

// JDK 11.x
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

// Libs 1.x
import com.siliconmtn.data.format.DateFormat;
import com.siliconmtn.data.format.DateFormat.DatePattern;

/****************************************************************************
 * <b>Title</b>: SunTimeVOTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the value object for the sun time data bean
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 28, 2021
 * @updates:
 ****************************************************************************/
class SunTimeVOTest {
	
	// Members
	private SunTimeVO sun;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		sun = new SunTimeVO();
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#SunTimeVO(java.util.Date, java.util.Date)}.
	 */
	@Test
	void testSunTimeVODateDate() {
		Date sunrise = DateFormat.formatDate(DatePattern.DATE_DASH, "2020-01-01");
		Date sunset = DateFormat.formatDate(DatePattern.DATE_DASH, "2020-01-02");
		sun = new SunTimeVO(sunrise, sunset);
		
		assertEquals(sunset, sun.getSunriseDate());
		assertEquals(sunrise, sun.getSunSetDate());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getLongitudeNumber()}.
	 */
	@Test
	void testGetLongitudeNumber() {
		assertEquals(0.0, sun.getLongitudeNumber());
		
		sun.setLongitudeNumber(-105.2345);
		assertEquals(-105.2345, sun.getLongitudeNumber());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getLatitudeNumber()}.
	 */
	@Test
	void testGetLatitudeNumber() {
		assertEquals(0.0, sun.getLatitudeNumber());
		
		sun.setLatitudeNumber(35.2345);
		assertEquals(35.2345, sun.getLatitudeNumber());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getTimeZoneName()}.
	 */
	@Test
	void testGetTimeZoneName() {
		assertNull(sun.getTimeZoneName());
		
		sun.setTimeZoneName("America/Denver");
		assertEquals("America/Denver", sun.getTimeZoneName());
	}


	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getSourceDate()}.
	 */
	@Test
	void testGetSourceDate() {
		assertNull(sun.getSourceDate());
		
		Date d = new Date();
		sun.setSourceDate(d);
		assertEquals(d, sun.getSourceDate());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getSunriseTime()}.
	 */
	@Test
	void testGetSunriseTime() {
		sun.setSunriseTime("");
		sun.setSourceDate(null);
		assertEquals("", sun.getSunriseTime());
		
		sun.setSourceDate(new Date());
		sun.setSunriseTime(null);
		assertNull(sun.getSunriseTime());
		
		sun.setSunriseTime("12:24");
		sun.setSourceDate(new Date());
		assertEquals("12:24", sun.getSunriseTime());
		
		sun.setSourceDate(null);
		sun.setSunriseTime("12:24");
		assertEquals("12:24", sun.getSunriseTime());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SunTimeVO#getSunsetTime()}.
	 */
	@Test
	void testGetSunsetTime() {
		sun.setSunsetTime("");
		sun.setSourceDate(null);
		assertEquals("", sun.getSunsetTime());
		
		sun.setSourceDate(new Date());
		sun.setSunsetTime(null);
		assertNull(sun.getSunsetTime());
		
		sun.setSunsetTime("12:24");
		sun.setSourceDate(new Date());
		assertEquals("12:24", sun.getSunsetTime());
		
		sun.setSourceDate(null);
		sun.setSunsetTime("12:24");
		assertEquals("12:24", sun.getSunsetTime());
	}

	@Test
	void testSunTimeVOHttpServletRequest() throws Exception {
		Map<String, String[]> map = new HashMap<>();
		map.put("sunsetTime", new String[] {"10:24"});
		HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		when(req.getParameterMap()).thenReturn(map);
		sun = new SunTimeVO(req);
		
		assertEquals("10:24", sun.getSunsetTime());
	}
}
