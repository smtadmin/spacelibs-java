package com.smt.data.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: DateFormatTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Test of the DateFormat class.  This test ensures
 * the formatting and parsing of dates/strings, different date type objects conversions,
 * retrieving various date related information like current day, date, year, month,
 * getting start, end and start of next date.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 1.0
 * @since Jan 22, 2021
 * @updates:
 ****************************************************************************/
class DateFormatTest {
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#convertZoneDateToDate(ZonedDateTime)}
	 */
	@Test
	void convertZoneDateToDate_test() throws ParseException {
		ZonedDateTime dateTime5 = ZonedDateTime.of(LocalDateTime.of(2021, 01, 22, 07, 00),
	            ZoneId.of("UTC"));
		assertEquals("Fri Jan 22 07:00:00 MST 2021",DateFormat.convertZoneDateToDate(dateTime5).toString());
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#setLocalToUTC()}
	 * Test method for {@link com.smt.data.format.DateFormat#setUTCToLocal(String)}
	 */
	@Test 
	void setLocalToUTC_test() {
		String before_tz=TimeZone.getDefault().getID();
		String default_tz=DateFormat.setLocalToUTC();
		assertEquals(before_tz,default_tz);
		DateFormat.setUTCToLocal(default_tz);
	}

	/**
	 * Test method for {@link com.smt.data.format.DateFormat#parseDateUnknownPattern(String)}
	 */
	@Test 
	void parseDateUnknownPattern_testForNull() {
		assertEquals(null,DateFormat.parseDateUnknownPattern(null));
		assertEquals(null,DateFormat.parseDateUnknownPattern("10$28$123"));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#parseDateUnknownPattern(String)}
	 */
	@Test 
	void parseDateUnknownPattern_testForLessLength() {
		assertEquals(null,DateFormat.parseDateUnknownPattern("10/"));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#parseDateUnknownPattern(String)}
	 */
	@Test 
	void parseDateUnknownPattern_testForNotNull() {
		Date date = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		assertEquals(date,DateFormat.parseDateUnknownPattern("10/28/1995"));
	}
    
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#formatDate(String, String)}
	 */
	@Test
	void formatDate_test() {
		assertEquals(null,DateFormat.formatDate("MM/DD/YYYY", null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#formatDate(Date, int, int)}
	 */
	@Test
	void formatDateAmount_test() {
		Date date_old = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		Date date_new = new GregorianCalendar(1995, Calendar.AUGUST, 28).getTime();
		assertEquals(date_new,DateFormat.formatDate(date_old, Calendar.MONTH, -2));
		assertEquals(null,DateFormat.formatDate(null, Calendar.MONTH, 5));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#isDate(String)}
	 */
	@Test
	void isDateString_test() {
		assertEquals(true,DateFormat.isDate("10/28/1995"));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#isDate(String)}
	 */
	@Test
	void isDateNullString_test() {
		assertEquals(false,DateFormat.isDate("10/"));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#isDate(Object)}
	 */
	@Test
	void isDateObject_test() {
		assertEquals(true,DateFormat.isDate((Object)("10/28/1995")));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#isDate(Object)}
	 */
	@Test
	void isDateNullObject_test() {
		assertEquals(false,DateFormat.isDate((Object)null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#formatSQLDate(java.util.Date)}
	 * Test method for {@link com.smt.data.format.DateFormat#formatSQLDate(java.util.Date, boolean)}
	 * Test method for {@link com.smt.data.format.DateFormat#formatSQLDate(String, String)}
	 * Test method for {@link com.smt.data.format.DateFormat#formatSQLDate(String)}
	 */
	@Test
	void formatSQLDate_test() {
		Date ud = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		java.sql.Date sd=new java.sql.Date(ud.getTime());    
		assertEquals(sd,DateFormat.formatSQLDate(ud));
		assertEquals(sd,DateFormat.formatSQLDate(ud, false));
		assertEquals(new java.sql.Date(new Date().getTime()),DateFormat.formatSQLDate(null, true));
		assertEquals(null,DateFormat.formatSQLDate(null, false));
		assertEquals(sd,DateFormat.formatSQLDate("MM/dd/yyyy","10/28/1995"));
		assertEquals(sd,DateFormat.formatSQLDate("10/28/1995"));
	}

	/**
	 * Test method for {@link com.smt.data.format.DateFormat#formatTimestamp(String, String)}.
	 * Test method for {@link com.smt.data.format.DateFormat#formatTimestamp(Date)}
	 */
	
	@Test
	public void formatTimestamp_test() {
		Date ud = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		java.sql.Timestamp ts =java.sql.Timestamp.valueOf(
		        java.time.LocalDate.of(1995,10,28).atStartOfDay()
		);
		assertEquals(ts,DateFormat.formatTimestamp("MM/dd/yyyy","10/28/1995"));
		assertNotEquals(ts,DateFormat.formatTimestamp("MM/dd/yyyy","10"));
		assertEquals(ts,DateFormat.formatTimestamp(ud));
		assertNotEquals(ts,DateFormat.formatTimestamp(null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#getCurrentMonth()}
	 * Test method for {@link com.smt.data.format.DateFormat#getCurrentYear()}
	 * Test method for {@link com.smt.data.format.DateFormat#getCurrentDayOfWeek()}
	 * Test method for {@link com.smt.data.format.DateFormat#getCurrentDayOfMonth()}
	 * Test method for {@link com.smt.data.format.DateFormat#getCurrentDate()}
	 */
	
	@Test
	public void getCurrent_test() {
		assertEquals(Calendar.getInstance().get(Calendar.MONTH) + 1,DateFormat.getCurrentMonth());
		assertEquals(Calendar.getInstance().get(Calendar.YEAR),DateFormat.getCurrentYear() );
		assertEquals(Calendar.getInstance().get(Calendar.DAY_OF_WEEK),DateFormat.getCurrentDayOfWeek());
		assertEquals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),DateFormat.getCurrentDayOfMonth());
		assertEquals(Calendar.getInstance().get(Calendar.DATE),DateFormat.getCurrentDate());
		}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#getEndOfDate(String)}
	 * Test method for {@link com.smt.data.format.DateFormat#getEndOfDate(Date)}
	 */
	@Test
	public void getEndDate_Test() {
		Date ud = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		Date ud_new = new GregorianCalendar(1995, Calendar.OCTOBER, 28,23,59,59).getTime();
		assertEquals(ud_new.toInstant(),DateFormat.getEndOfDate("10/28/1995").toInstant());
		assertEquals(ud_new.toInstant(),DateFormat.getEndOfDate(ud).toInstant());
		assertNotEquals(new Date(),DateFormat.getEndOfDate((Date)null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#getStartOfDate(String)}
	 * Test method for {@link com.smt.data.format.DateFormat#getStartOfDate(Date)}
	 */
	@Test
	public void getStartOfDate_Test() {
		Date ud = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		Date ud_new = new GregorianCalendar(1995, Calendar.OCTOBER, 28,0,0,0).getTime();
		assertEquals(ud_new.getTime(),DateFormat.getStartOfDate("10/28/1995").getTime());
		assertEquals(ud_new.getTime(),DateFormat.getStartOfDate(ud).getTime());
		assertNotEquals(new Date(),DateFormat.getStartOfDate((Date)null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#getStartOfNextDate(String)}
	 * Test method for {@link com.smt.data.format.DateFormat#getStartOfNextDate(Date)}
	 */
	@Test
	public void getStartOfNextDate_Test() {
		Date ud = new GregorianCalendar(1995, Calendar.OCTOBER, 28).getTime();
		Date ud_new = new GregorianCalendar(1995, Calendar.OCTOBER, 29,0,0,0).getTime();
		assertEquals(ud_new,DateFormat.getStartOfNextDate("10/28/1995"));
		assertEquals(ud_new,DateFormat.getStartOfNextDate(ud));
		assertNotEquals(new Date(),DateFormat.getStartOfNextDate((Date)null));
	}
	
	/**
	 * Test method for {@link com.smt.data.format.DateFormat#convertAnyZoneToUTC(String, String)}
	 * Test method for {@link com.smt.data.format.DateFormat#convertAnyZoneToUTC(Date, String)}
	 */
	@Test
	public void convertAnyZoneToUTC() {
		Date d = new GregorianCalendar(1995, Calendar.OCTOBER, 29,0,0,0).getTime();
		ZonedDateTime then = DateFormat.convertAnyZoneToUTC(d,TimeZone.getDefault().getID().toString());
		ZonedDateTime then2 = DateFormat.convertAnyZoneToUTC("10/29/1995",TimeZone.getDefault().getID().toString());
		assertEquals("1995-10-29T06:00Z", then.toString());
		assertEquals("1995-10-29T06:00Z", then2.toString());
	}
}
