package com.siliconmtn.weather;

// JDK 11.x
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: SolarEventCalculatorTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the SolarEventCalculator
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 22, 2021
 * @updates:
 ****************************************************************************/
class SolarEventCalculatorTest {
	
	private Coordinate coord;
	private String timeZoneIdentifier;
	private SolarEventCalculator calc;
	private Calendar testDate;

	@BeforeEach
	void setUpBeforeEach() throws Exception {
		timeZoneIdentifier = "America/Denver";
		
		coord = new Coordinate(39.742043, -104.991531);
		calc = new SolarEventCalculator(coord, TimeZone.getTimeZone(timeZoneIdentifier));
		testDate = new GregorianCalendar(2021, 2, 1);
		
	}
	
	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#SolarEventCalculator(com.siliconmtn.weather.Coordinate, java.lang.String)}.
	 */
	@Test
	void testSolarEventCalculatorCoordinateString() {
		timeZoneIdentifier = "America/Denver";
		
		coord = new Coordinate(39.742043, -104.991531);
		SolarEventCalculator calc = new SolarEventCalculator(coord, timeZoneIdentifier);
		TimeZone tz = TimeZone.getTimeZone(timeZoneIdentifier);
		assertEquals(coord, calc.getLocation());
		assertEquals(tz, calc.getTimeZone());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#SolarEventCalculator(com.siliconmtn.weather.Coordinate, java.util.TimeZone)}.
	 */
	@Test
	void testSolarEventCalculatorCoordinateTimeZone() {
		TimeZone tz = TimeZone.getTimeZone("America/Denver");
		coord = new Coordinate(39.742043, -104.991531);
		SolarEventCalculator calc = new SolarEventCalculator(coord, tz);
		
		assertEquals(coord, calc.getLocation());
		assertEquals(tz, calc.getTimeZone());
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#computeSunriseTime(com.siliconmtn.weather.Zenith, java.util.Calendar)}.
	 */
	@Test
	void testComputeSunriseTime() {
		assertEquals("06:12", calc.computeSunriseTime(Zenith.OFFICIAL, testDate));
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#computeSunriseCalendar(com.siliconmtn.weather.Zenith, java.util.Calendar)}.
	 */
	@Test
	void testComputeSunriseCalendar() {
		Calendar cal = calc.computeSunriseCalendar(Zenith.OFFICIAL, testDate);
		assertEquals(60, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(2, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(6, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(12, cal.get(Calendar.MINUTE));
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#computeSunsetTime(com.siliconmtn.weather.Zenith, java.util.Calendar)}.
	 */
	@Test
	void testComputeSunsetTime() {
		assertEquals("18:12", calc.computeSunsetTime(Zenith.OFFICIAL, testDate));
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#computeSunsetCalendar(com.siliconmtn.weather.Zenith, java.util.Calendar)}.
	 */
	@Test
	void testComputeSunsetCalendar() {
		Calendar cal = calc.computeSunsetCalendar(Zenith.OFFICIAL, testDate);
		assertEquals(60, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(2, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(18, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(12, cal.get(Calendar.MINUTE));
	}

	/**
	 * Test method for {@link com.siliconmtn.weather.SolarEventCalculator#getLocalTimeAsCalendar(java.math.BigDecimal, java.util.Calendar)}.
	 */
	@Test
	void testGetLocalTimeAsCalendar() {
		assertNull(calc.getLocalTimeAsCalendar(null, testDate));
				
		Calendar cal = calc.getLocalTimeAsCalendar(BigDecimal.valueOf(6.2072), testDate);
		assertEquals(60, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(2, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(6, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(12, cal.get(Calendar.MINUTE));
		
		cal = calc.getLocalTimeAsCalendar(BigDecimal.valueOf(-1), testDate);
		assertEquals(59, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(1, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(23, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		
		cal = calc.getLocalTimeAsCalendar(BigDecimal.valueOf(1.9999), testDate);
		assertEquals(60, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(2, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(2, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		
		cal = calc.getLocalTimeAsCalendar(BigDecimal.valueOf(-.0001), testDate);
		assertEquals(59, cal.get(Calendar.DAY_OF_YEAR));
		assertEquals(1, cal.get(Calendar.MONTH));
		assertEquals(2021, cal.get(Calendar.YEAR));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
	}

	@Test
	void testComputeSolarEventTime() throws Exception {
		
		coord = new Coordinate(-39.742043, 104.991531);
		calc = new SolarEventCalculator(coord, TimeZone.getTimeZone("Africa/Nairobi"));
		testDate = new GregorianCalendar(1900, 11, 1);
		calc.computeSolarEventTime(Zenith.ASTRONOMICAL, testDate, true);
		calc.computeSolarEventTime(Zenith.ASTRONOMICAL, testDate, false);
		assertEquals(TimeZone.getTimeZone("Africa/Nairobi"), calc.getTimeZone());
	}

	@Test
	void testGetRightAscension() throws Exception {
		assertEquals(BigDecimal.valueOf(-1.5521), calc.getRightAscension(BigDecimal.valueOf(-25.1234)));
		assertEquals(BigDecimal.valueOf(-683.3873), calc.getRightAscension(BigDecimal.valueOf(-10250.0)));
		assertEquals(BigDecimal.valueOf(0.0611), calc.getRightAscension(BigDecimal.valueOf(.999100009999)));
		assertEquals(BigDecimal.valueOf(666666.6527), calc.getRightAscension(BigDecimal.valueOf(9999999.0)));
		assertEquals(BigDecimal.valueOf(24.0611), calc.getRightAscension(BigDecimal.valueOf(361.0)));
	}

	@Test
	void testAdjustForDST() throws Exception {
		Calendar cal = calc.getLocalTimeAsCalendar(BigDecimal.valueOf(6.2072), testDate);
		assertEquals(BigDecimal.valueOf(1.0), calc.adjustForDST(BigDecimal.valueOf(25.0), cal));
		assertEquals(BigDecimal.valueOf(10.0), calc.adjustForDST(BigDecimal.valueOf(10.0), cal));
		
		cal = GregorianCalendar.getInstance();
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.YEAR, 2020);
		assertEquals(BigDecimal.valueOf(11.0), calc.adjustForDST(BigDecimal.valueOf(10.0), cal));
	}

	@Test
	void testGetLocalTimeAsString() throws Exception {
		assertEquals("00:00", calc.getLocalTimeAsString(BigDecimal.ZERO));
		assertEquals("99:99", calc.getLocalTimeAsString(null));
		assertEquals("00:07", calc.getLocalTimeAsString(BigDecimal.valueOf(-25.1234)));
		assertEquals("15:30", calc.getLocalTimeAsString(BigDecimal.valueOf(15.50)));
		assertEquals("00:00", calc.getLocalTimeAsString(BigDecimal.valueOf(24.00)));
		assertEquals("11:00", calc.getLocalTimeAsString(BigDecimal.valueOf(10.999)));
	}

}
