package com.siliconmtn.data.format;

// JDK 11.x
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

// Spaceforce 1.x
import com.siliconmtn.data.text.StringUtil;

// Lombok 1.18.x
import lombok.extern.log4j.Log4j2;

/*********************************************************************************************************
 * <b>Title</b>: Convert Utilities for Date parsing, formatting and conversions
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 1.0
 * @since 01.25.2021 
 * @updates:         
 **********************************************************************************************************/
@Log4j2
public class DateFormat {
	
	/**
	 * Enum to provide the pattern name and date pattern for specifying types
	 */
	public enum DatePattern{
				// Defining and declaring enum type Date and Time Patterns
		        		        
				//Date Time formats that are RSS Related.
				RFC_2822_NO_SEC ("EEE dd MMM yyyy HH:mm zzz"),
				RFC_2822("EEE dd MMM yyyy HH:mm:ss zzz"),
				
				//Date Time formats that are ATOM Feed Related
				ISO_8601_NO_SEC("yyyy-MM-dd'T'HH:mmZ"),
				ISO_8601("yyyy-MM-dd'T'HH:mm:ssZ"),	
				
				//date & time formats
				DATE_TIME_SLASH("MM/dd/yyyy HH:mm:ss"),
				DATE_TIME_DASH("yyyy-MM-dd HH:mm:ss"),
				DATETIME_SLASH("MM/dd/yyyy HH:mm"),
				DATETIME_SLASH_SHORT("MM/dd/yy HH:mm"),
				DATETIME_DASH("yyyy-MM-dd HH:mm"),
				ISO("yyyy-MM-dd'T'HH:mm:ss.SSS zzz"),
				ISO_SHORT("yyyy-MM-dd'T'HH:mm:ss.SSS"),
				
				//date formats
				DATE_DASH("yyyy-MM-dd"),
				DATE_DASH_SIMPLE_YEAR("MM-dd-yyyy"),
				DATE_SLASH("MM/dd/yyyy"),
				DATE_SLASH_ABBREV("MMM/dd/yyyy"),
				DATE_SLASH_SHORT("MM/dd/yy"),
				DATE_LONG("EEEE MMMM dd, yyyy"),
				DATE_FULL_MONTH("MMMMMMMMM dd, yyyy"),
				DATE_SHORT_MONTH("MMM dd, yyyy"),
				DATE_NOSPACE("yyyyMMdd"),
				DATE_SHORT_NOSPACE("yyMMdd"),
				DATE_SIMPLE("MMddyy"),
				DATE_SIMPLE_YEAR("MMddyyyy"),
				DATE_SHORT("MMyy"),
				DATE_SLASH_MONTH("MM/yyyy"),
				DATE_SLASH_MONTH_SHORT("MM/yy"),
				DATE_LONG_DAY_OF_WEEK("EEE MMM dd HH:mm:ss zzz yyyy"),
				
				DATE_TIME_NOSPACE("yyyyMMdd_HHmmss"),
				DATE_TIME_DASH_12HR("yyyy-MM-dd hh:mm a"),
				DATE_TIME_SLASH_12HR("MM/dd/yyyy hh:mm a"),
				DATE_TIME_SLASH_FULL_12HR("MM/dd/yyyy hh:mm:ss a"),
				DATE_TIME_NOSPACE_12HR("yyyyMMdd_hhmma"),

				//time formats
				TIME_SHORT("h:mm a"),
				TIME_LONG("hh:mm:ss a"),
				TIME_SHORT_NOSPACE("hhmm"),
				TIME_SHORT_24HR_NOSPACE("HHmm"),
				TIME_LONG_24HR_NOSPACE("HHmmss");
	    
		private final String pattern;
		
		/**
		 * Initialization of patterns
		 * @param pattern
		 */
		private DatePattern(final String pattern) {
			this.pattern=pattern;
		}
		
		/**
		 * Returning pattern when called to get a specific pattern
		 * @return String representation of the pattern
		 */
		public String getPattern() {
	        return this.pattern;
	    }
	}
	
	/**
	 * Make the constructor private as this class has statics only
	 */
	private DateFormat() {
		super();
	}
	
	/**
	 * Converts given ZonedDateTime format date to Date format date
	 * "YYYY-MM-DD'T'HH:MMZ" to "EEE MMM DD HH:MM:SS zzz YYYY", Example:  "2021-01-22T07:13Z" to "Fri Jan 22 07:00:00 UTC 2021"
	 * @param zdt in ZonedDateTime zdt as input;
	 * @return Date to use as the end date of a query
	 */
	public static Date zoneDateToDate(ZonedDateTime zdt) {
        
        // Convert ZonedDateTime to Instant instance
        // We just say what Instant is and how it used in the blog
        Instant instant = zdt.toInstant();
        
        // Create Date instance out of Instant
		return Date.from(instant);
	}
	
	/**
	 * Takes a provided date and figures out the formatting needed to turn the string
	 * into a date
	 * @param theDate string formatted date/time
	 * @return Date object. Null if unable to match
	 */
	public static Date parseUnknownPattern(String theDate) {	
		if (theDate == null) return null;
		theDate = theDate.replace(",", "");
		theDate = theDate.trim();
		Date d = null;
	
		for(DatePattern pn: DatePattern.values()) {
			d = formatDate(pn, theDate);
			if (d != null)
				break;
		}
		return d;
	}

	/**
	 * Converts a String date into a Date date
	 * @param dp Pattern Enum
	 * @param dateText text to be converted
	 * @return null if unable to convert, if successful return Date
	 */
	public static Date formatDate(DatePattern dp, String dateText) {	
		if(StringUtil.isEmpty(dateText) || dateText.length()<4) return null;

		SimpleDateFormat formatter = new SimpleDateFormat(dp.getPattern(), Locale.ENGLISH);
		Date theDate = null;
		try {
			theDate = formatter.parse(dateText);
		} catch (Exception e) {
			log.error("Unable to convert date", e);
		}
		
		return theDate;
	}
	
	/**
	 * Converts given Date object into a given Specific pattern
	 * @param dp PatternName Date pattern
	 * @param date Date to be formatted
	 * @return Converts to String date in desired pattern, else empty string
	 */
	public static String toFormattedString(DatePattern dp, Date date) {
		if (dp == null || date == null) return null;

	    // create and return a new String using the date format we want
		SimpleDateFormat formatter = new SimpleDateFormat(dp.getPattern());
	    return formatter.format(date);
	}
	
	/**
	 * The date is adjusted in the field entry and by the amount.  For example, 
	 * if the date is 1/1/2008 and you want to move the month back one month use:
	 * formatDate(date, Calendar.MONTH, -1);
	 * @param d Date to be adjusted
	 * @param field What to adjust (day, month ...)
	 * @param amount Amount of adjustment
	 * @return the adjusted date value
	 */
	public static Date adjustDate(Date d, int field, int amount) {
		if(d==null)
			return null;
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		cal.add(field, amount);
		return cal.getTime();
	}
	/**
	 * Checks the submitted string and if it is a date returns true.  
	 * @param sDate Date in a string format
	 * @return True if the string is a date.  False otherwise
	 */
	public static boolean isDate(String sDate) {
		Date parsedDate = parseUnknownPattern(sDate);
		return (parsedDate != null);
	}
	/**
	 * Overloaded to take an object and checks to see if it is a date.
	 * @param oDate Date to check
	 * @return True if the object is a date.  False otherwise
	 */
	public static boolean isDate(Object oDate) {
		if (oDate == null) return isDate("");
		return isDate(oDate.toString().trim());
	}
	/**
	 * Converts a java.util.Date to a java.sql.Date
	 * @param val Date to be converted
	 * @return java.sql.Date Object. Null if the value param is null
	 */
	public static java.sql.Date formatSQLDate(java.util.Date val) {
		return formatSQLDate(val, false);
	}
	/**
	 * Converts a java.util.Date to a java.sql.Date
	 * @param val Date to be converted to a sql date
	 * @param defaultNow Uses current date and time if true
	 * @return java.sql.Date Object. Null if the value param is null and defaultNow
	 * is false.  Otherwise, on null, current date and time will be returned
	 */
	public static java.sql.Date formatSQLDate(java.util.Date val, boolean defaultNow) {
		java.sql.Date dt = null;

		if (val != null)
			dt = new java.sql.Date(val.getTime());
		else if (defaultNow)
			dt = new java.sql.Date(new Date().getTime());

		return dt;
	}
	/**
	 * Converts a String into a java.sql.Date Object
	 * @param datePattern PatternName
	 * @param dateText text to be converted
	 * @return null if unable to convert, else java.sql.Date object
	 */
	public static java.sql.Date formatSQLDate(DatePattern datePattern,String dateText) {
		java.util.Date uDate = formatDate(datePattern, dateText);
		return formatSQLDate(uDate);
	}
	/**
	 * Converts a String into a java.sql.Date Object
	 * @param dateText Must be formatted as the default pattern (MM/DD/YYYY)
	 * @return null if unable to convert, else java.sql.Date object
	 */
	public static java.sql.Date formatSQLDate(String dateText) {
		java.util.Date uDate = formatDate(DatePattern.DATE_SLASH, dateText);
		return formatSQLDate(uDate);
	}
	/**
	 * Creates a SQL Timestamp out of a String date that matches the supplied pattern
	 * @param datePattern PatternName Type of pwattern
	 * @param dateText text to be converted
	 * @return Timestamp from the provided date as a string
	 */ 
	public static Timestamp formatTimestamp(DatePattern datePattern, String dateText) {
		Date d = formatDate(datePattern,dateText);
		if (d == null) return null;
		else return new Timestamp(d.getTime());
	}
	/**
	 * takes a java.util.Date object and if it's not null converts it to a 
	 * java.sql.Timestamp, for insertion into DBs.
	 * @param  dt Date to convert to a timestamp
	 * @return Timestamp Converted form a date
	 */
	public static Timestamp formatTimestamp(Date dt) {
		if (dt == null) return null;
		else return new Timestamp(dt.getTime());
	}
	/**
	 * Returns the current month as an integer,Example: 1 for January, 2 for February....
	 * @return The current month
	 */
	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * Returns the current year as an integer, Example: 2021,..
	 * @return The current year
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	/**
	 * Returns the current day of Week as an integer; example: 1 for Sunday, 2 for Monday......
	 * @return Current day of week as an integer
	 */
	public static int getCurrentDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}
	/**
	 * Returns the current day of a month as an integer, dates in the range of 1-30, 1-31, 1-28, leap year, 1-29
	 * @return Current day of month as an integer
	 */
	public static int getCurrentDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * Returns the current date as an integer , dates in the range of 1-30, 1-31, 1-28, leap year, 1-29
	 * @return The day of the month as an int
	 */
	public static int getCurrentDate() {
		return Calendar.getInstance().get(Calendar.DATE);
	}
	/**
	 * Returns the end date for a sql query.  Keeps current date and sets
	 * the time to 23:59:59
	 * @param date String to format (MM/DD/YYYY).  Uses current date if null;
	 * @return Date to use as the end date of a query
	 */
	public static Date getEndOfDay(String date) {
		return getEndOfDay(parseUnknownPattern(date));
	}
	/**
	 * Returns the end date for a sql query.  Keeps current date and sets
	 * the time to 23:59:59
	 * @param d Date to format.  Uses current date if null;
	 * @return Date to use as the end date of a query
	 */
	public static Date getEndOfDay(Date d) {	
		if (d == null) d = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	/**
	 * Returns the start date for a sql query. Keeps current date and sets
	 * the time to 00:00:00
	 * @param date String to format (MM/DD/YYYY).  Uses current date if null;
	 * @return Date to use as the end date of a query
	 */
	public static Date getStartOfDay(String date) {
		return getStartOfDay(parseUnknownPattern(date));
	}
	/**
	 * Returns the start date for a sql query. Keeps current date and sets
	 * the time to 00:00:00
	 * @param d Date to format.  Uses current date if null;
	 * @return Date to use as the end date of a query
	 */
	public static Date getStartOfDay(Date d) {	
		if (d == null) d = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * Converts date to UTC as ZonedDateTime object.
	 * @param date as String and fromZone string in the format
	 * @param fromZone is the timezone to convert from
	 * @return returns UTC ZonedDateTime
	 */
	public static ZonedDateTime anyZoneToUTC(String date, String fromZone) {
		Date d = parseUnknownPattern(date);
		if (d == null) return null;
		return anyZoneToUTC(d,fromZone);
	}
	/**
	 * Converts date to UTC as ZonedDateTime object.
	 * @param date as Date and fromZone string in the format
	 * @param fromZone is the timezone to convert from
	 * @return returns UTC ZonedDateTime
	 */
	public static ZonedDateTime anyZoneToUTC(Date date, String fromZone) {	    
	    LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());	  
	    ZoneId zone = ZoneId.of(fromZone);
	    // parse the offset
	    ZoneOffset zoneOffSet = zone.getRules().getOffset(ldt);
	    // create an OffsetDateTime using the parsed offset
	    OffsetDateTime odt = OffsetDateTime.of(ldt, zoneOffSet);	
	    // create a ZonedDateTime from the OffsetDateTime and use UTC as time zone
	    return odt.atZoneSameInstant(ZoneOffset.UTC);	   
	}	
}
	
	
