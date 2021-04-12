package com.siliconmtn.weather;

// JDK 11.x
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.TimeZone;

import com.siliconmtn.data.lang.SMTMath;

/****************************************************************************
 * <b>Title</b>: Coordinate.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Thanks to Mike Reedell for creating this class!
 * Parent class of the sunrise and sunset classes
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 3.0
 * @since Jan 15, 2021
 * Copyright 2008-2009 Mike Reedell / LuckyCatLabs.
 * http://www.apache.org/licenses/LICENSE-2.0
 ****************************************************************************/
public class SolarEventCalculator {
	private final Coordinate location;
	private final TimeZone timeZone;

    /**
     * Constructs a new <code>SolarEventCalculator</code> using the given parameters.
     *
     * @param location <code>Location</code> of the place where the solar event 
     * should be calculated from.
     * @param timeZoneIdentifier time zone identifier of the timezone of the 
     * location parameter. For example, "America/New_York".
     */
    public SolarEventCalculator(Coordinate location, String timeZoneIdentifier) {
        this.location = location;
        this.timeZone = TimeZone.getTimeZone(timeZoneIdentifier);
    }

    /**
     * Constructs a new <code>SolarEventCalculator</code> using the given parameters.
     * @param location <code>Location</code> of the place where the solar event should be calculated from.
     * @param timeZone timezone of the location parameter.
     */
    public SolarEventCalculator(Coordinate location, TimeZone timeZone) {
        this.location = location;
        this.timeZone = timeZone;
    }

    /**
     * Computes the sunrise time for the given zenith at the given date.
     *
     * @param solarZenith  <code>Zenith</code> enum corresponding to the type of sunrise to compute.
     * @param date <code>Calendar</code> object representing the date to compute 
     * the sunrise for.
     * @return the sunrise time, in HH:MM format (24-hour clock), 00:00 if the 
     * sun does not rise on the given date.
     */
    public String computeSunriseTime(Zenith solarZenith, Calendar date) {
        return getLocalTimeAsString(computeSolarEventTime(solarZenith, date, true));
    }

    /**
     * Computes the sunrise time for the given zenith at the given date.
     *
     * @param solarZenith  <code>Zenith</code> enum corresponding to the type of sunrise to compute.
     * @param date <code>Calendar</code> object representing the date to compute the sunrise for.
     * @return the sunrise time as a calendar or null for no sunrise
     */
    public Calendar computeSunriseCalendar(Zenith solarZenith, Calendar date) {
        return getLocalTimeAsCalendar(computeSolarEventTime(solarZenith, date, true), date);
    }

    /**
     * Computes the sunset time for the given zenith at the given date.
     *
     * @param solarZenith <code>Zenith</code> enum corresponding to the type of sunset to compute.
     * @param date <code>Calendar</code> object representing the date to compute the sunset for.
     * @return the sunset time, in HH:MM format (24-hour clock), 00:00 if the sun does not set on the given
     *         date.
     */
    public String computeSunsetTime(Zenith solarZenith, Calendar date) {
        return getLocalTimeAsString(computeSolarEventTime(solarZenith, date, false));
    }

    /**
     * Computes the sunset time for the given zenith at the given date.
     *
     * @param solarZenith <code>Zenith</code> enum corresponding to the type of sunset to compute.
     * @param date <code>Calendar</code> object representing the date to compute the sunset for.
     * @return the sunset time as a Calendar or null for no sunset.
     */
    public Calendar computeSunsetCalendar(Zenith solarZenith, Calendar date) {
        return getLocalTimeAsCalendar(computeSolarEventTime(solarZenith, date, false), date);
    }

    /**
     * <code>Zenith</code> enum corresponding to the type of sunset to compute.
     * @param solarZenith  Defines the solar declination used in computing the sunrise/sunset.
     * @param date Date to calculate
     * @param isSunrise is sunrise or sunset
     * @return The solar event time
     */
    protected BigDecimal computeSolarEventTime(Zenith solarZenith, Calendar date, boolean isSunrise) {
        date.setTimeZone(this.timeZone);
        BigDecimal longitudeHour = getLongitudeHour(date, isSunrise);

        BigDecimal meanAnomaly = getMeanAnomaly(longitudeHour);
        BigDecimal sunTrueLong = getSunTrueLongitude(meanAnomaly);
        BigDecimal cosineSunLocalHour = getCosineSunLocalHour(sunTrueLong, solarZenith);

        BigDecimal sunLocalHour = getSunLocalHour(cosineSunLocalHour, isSunrise);
        BigDecimal localMeanTime = getLocalMeanTime(sunTrueLong, longitudeHour, sunLocalHour);
        return getLocalTime(localMeanTime, date);
    }

    /**
     * Computes the base longitude hour, lngHour in the algorithm.
     * @return the longitude of the location of the solar event divided by 15 (deg/hour), in
     * <code>BigDecimal</code> form.
     */
    private BigDecimal getBaseLongitudeHour() {
        return SMTMath.divideBy(location.getLongitude(), BigDecimal.valueOf(15));
    }

    /**
     * Computes the longitude time, t in the algorithm.
     * @return longitudinal time in <code>BigDecimal</code> form.
     */
    private BigDecimal getLongitudeHour(Calendar date, Boolean isSunrise) {
        int offset = 18;
        if (isSunrise.booleanValue()) {
            offset = 6;
        }
        
        BigDecimal dividend = BigDecimal.valueOf(offset).subtract(getBaseLongitudeHour());
        BigDecimal addend = SMTMath.divideBy(dividend, BigDecimal.valueOf(24));
        BigDecimal longHour = getDayOfYear(date).add(addend);
        return setScale(longHour);
    }

    /**
     * Computes the mean anomaly of the Sun, M in the algorithm.
     * @return the suns mean anomaly, M, in <code>BigDecimal</code> form.
     */
    private BigDecimal getMeanAnomaly(BigDecimal longitudeHour) {
        BigDecimal meanAnomaly = SMTMath.multiplyBy(new BigDecimal("0.9856"), longitudeHour).subtract(new BigDecimal("3.289"));
        return setScale(meanAnomaly);
    }

    /**
     * Computes the true longitude of the sun, L in the algorithm, at the given location, adjusted to fit in
     * the range [0-360].
     * @param meanAnomaly the suns mean anomaly.
     * @return the suns true longitude, in <code>BigDecimal</code> form.
     */
    private BigDecimal getSunTrueLongitude(BigDecimal meanAnomaly) {
        BigDecimal sinMeanAnomaly = BigDecimal.valueOf(Math.sin(SMTMath.convertDegreesToRadians(meanAnomaly).doubleValue()));
        BigDecimal sinDoubleMeanAnomaly = BigDecimal.valueOf(Math.sin(SMTMath.multiplyBy(SMTMath.convertDegreesToRadians(meanAnomaly), BigDecimal.valueOf(2)).doubleValue()));

        BigDecimal firstPart = meanAnomaly.add(SMTMath.multiplyBy(sinMeanAnomaly, new BigDecimal("1.916")));
        BigDecimal secondPart = SMTMath.multiplyBy(sinDoubleMeanAnomaly, new BigDecimal("0.020")).add(new BigDecimal("282.634"));
        BigDecimal trueLongitude = firstPart.add(secondPart);

        if (trueLongitude.doubleValue() > 360) {
            trueLongitude = trueLongitude.subtract(BigDecimal.valueOf(360));
        }
        return setScale(trueLongitude);
    }

    /**
     * Computes the suns right ascension, RA in the algorithm, adjusting for the quadrant of L and turning it
     * into degree-hours. Will be in the range [0,360].
     * @param sunTrueLong Suns true longitude, in <code>BigDecimal</code>
     * @return suns right ascension in degree-hours, in <code>BigDecimal</code> form.
     */
    protected BigDecimal getRightAscension(BigDecimal sunTrueLong) {
        BigDecimal tanL = BigDecimal.valueOf(Math.tan(SMTMath.convertDegreesToRadians(sunTrueLong).doubleValue()));
        
        BigDecimal innerParens = SMTMath.multiplyBy(SMTMath.convertRadiansToDegrees(tanL), new BigDecimal("0.91764"));
        BigDecimal rightAscension = BigDecimal.valueOf(Math.atan(SMTMath.convertDegreesToRadians(innerParens).doubleValue()));
        rightAscension = setScale(SMTMath.convertRadiansToDegrees(rightAscension));

        if (rightAscension.doubleValue() < 0) {
            rightAscension = rightAscension.add(BigDecimal.valueOf(360));
        }

        BigDecimal ninety = BigDecimal.valueOf(90);
        BigDecimal longitudeQuadrant = sunTrueLong.divide(ninety, 0, RoundingMode.FLOOR);
        longitudeQuadrant = longitudeQuadrant.multiply(ninety);

        BigDecimal rightAscensionQuadrant = rightAscension.divide(ninety, 0, RoundingMode.FLOOR);
        rightAscensionQuadrant = rightAscensionQuadrant.multiply(ninety);

        BigDecimal augend = longitudeQuadrant.subtract(rightAscensionQuadrant);
        return SMTMath.divideBy(rightAscension.add(augend), BigDecimal.valueOf(15));
    }

    /**
     * Gets the sun local hour
     * @param sunTrueLong Suns true longitude, in <code>BigDecimal</code>
     * @param zenith <code>Zenith</code> enum corresponding to the type of sunrise to compute.
     * @return 
     */
    private BigDecimal getCosineSunLocalHour(BigDecimal sunTrueLong, Zenith zenith) {
        BigDecimal sinSunDeclination = getSinOfSunDeclination(sunTrueLong);
        BigDecimal cosineSunDeclination = getCosineOfSunDeclination(sinSunDeclination);

        BigDecimal zenithInRads = SMTMath.convertDegreesToRadians(zenith.degrees());
        BigDecimal cosineZenith = BigDecimal.valueOf(Math.cos(zenithInRads.doubleValue()));
        BigDecimal sinLatitude = BigDecimal.valueOf(Math.sin(SMTMath.convertDegreesToRadians(location.getLatitude()).doubleValue()));
        BigDecimal cosLatitude = BigDecimal.valueOf(Math.cos(SMTMath.convertDegreesToRadians(location.getLatitude()).doubleValue()));

        BigDecimal sinDeclinationTimesSinLat = sinSunDeclination.multiply(sinLatitude);
        BigDecimal dividend = cosineZenith.subtract(sinDeclinationTimesSinLat);
        BigDecimal divisor = cosineSunDeclination.multiply(cosLatitude);

        return setScale(SMTMath.divideBy(dividend, divisor));
    }

    /**
     * 
     * @param sunTrueLong Suns true longitude, in <code>BigDecimal</code>
     * @return longitude
     */
    private BigDecimal getSinOfSunDeclination(BigDecimal sunTrueLong) {
        BigDecimal sinTrueLongitude = BigDecimal.valueOf(Math.sin(SMTMath.convertDegreesToRadians(sunTrueLong).doubleValue()));
        BigDecimal sinOfDeclination = sinTrueLongitude.multiply(new BigDecimal("0.39782"));
        return setScale(sinOfDeclination);
    }

    /**
     * Gets sun declination  cosine
     * @param sinSunDeclination
     * @return
     */
    private BigDecimal getCosineOfSunDeclination(BigDecimal sinSunDeclination) {
        BigDecimal arcSinOfSinDeclination = BigDecimal.valueOf(Math.asin(sinSunDeclination.doubleValue()));
        BigDecimal cosDeclination = BigDecimal.valueOf(Math.cos(arcSinOfSinDeclination.doubleValue()));
        return setScale(cosDeclination);
    }

    /**
     * Get's sunrise local hour
     * @param cosineSunLocalHour
     * @param isSunrise
     * @return
     */
    private BigDecimal getSunLocalHour(BigDecimal cosineSunLocalHour, Boolean isSunrise) {
        BigDecimal arcCosineOfCosineHourAngle = SMTMath.getArcCosineFor(cosineSunLocalHour);
        BigDecimal localHour = SMTMath.convertRadiansToDegrees(arcCosineOfCosineHourAngle);
        if (isSunrise.booleanValue()) {
            localHour = BigDecimal.valueOf(360).subtract(localHour);
        }
        return SMTMath.divideBy(localHour, BigDecimal.valueOf(15));
    }

    /**
     * Gets the local mean time
     * @param sunTrueLong Suns true longitude, in <code>BigDecimal</code>
     * @param longitudeHour
     * @param sunLocalHour
     * @return
     */
    private BigDecimal getLocalMeanTime(BigDecimal sunTrueLong, BigDecimal longitudeHour, BigDecimal sunLocalHour) {
        BigDecimal rightAscension = this.getRightAscension(sunTrueLong);
        BigDecimal innerParens = longitudeHour.multiply(new BigDecimal("0.06571"));
        BigDecimal localMeanTime = sunLocalHour.add(rightAscension).subtract(innerParens);
        localMeanTime = localMeanTime.subtract(new BigDecimal("6.622"));

        if (localMeanTime.doubleValue() < 0) {
            localMeanTime = localMeanTime.add(BigDecimal.valueOf(24));
        } else if (localMeanTime.doubleValue() > 24) {
            localMeanTime = localMeanTime.subtract(BigDecimal.valueOf(24));
        }
        return setScale(localMeanTime);
    }

    /**
     * Calculates the local time
     * @param localMeanTime Time offset
     * @param date Date to convert
     * @return
     */
    private BigDecimal getLocalTime(BigDecimal localMeanTime, Calendar date) {
        BigDecimal utcTime = localMeanTime.subtract(getBaseLongitudeHour());
        BigDecimal utcOffSet = getUTCOffSet(date);
        BigDecimal utcOffSetTime = utcTime.add(utcOffSet);
        return adjustForDST(utcOffSetTime, date);
    }

    /**
     * Adjusts for Daylight savings time
     * @param localMeanTime
     * @param date
     * @return
     */
    BigDecimal adjustForDST(BigDecimal localMeanTime, Calendar date) {
        BigDecimal localTime = localMeanTime;
        if (timeZone.inDaylightTime(date.getTime())) {
            localTime = localTime.add(BigDecimal.ONE);
        }
        if (localTime.doubleValue() > 24.0) {
            localTime = localTime.subtract(BigDecimal.valueOf(24));
        }
        return localTime;
    }

    /**
     * Returns the local rise/set time in the form HH:MM.
     *
     * @param localTime <code>BigDecimal</code> representation of the local rise/set time.
     * @return <code>String</code> representation of the local rise/set time in HH:MM format.
     */
    String getLocalTimeAsString(BigDecimal localTimeParam) {
        if (localTimeParam == null) {
            return "99:99";
        }

        BigDecimal localTime = localTimeParam;
        if ((localTime.compareTo(BigDecimal.ZERO)) < 0) {
            localTime = localTime.add(BigDecimal.valueOf(24.00));
        }
        String[] timeComponents = localTime.toPlainString().split("\\.");
        int hour = Integer.parseInt(timeComponents[0]);

        BigDecimal minutes = timeComponents.length < 2 ? BigDecimal.ZERO : new BigDecimal("0." + timeComponents[1]);
        minutes = minutes.multiply(BigDecimal.valueOf(60)).setScale(0, RoundingMode.HALF_EVEN);

        if (minutes.intValue() == 60) {
            minutes = BigDecimal.ZERO;
            hour += 1;
        }
        if (hour == 24 || hour < 0) {
            hour = 0;
        }

        String minuteString = minutes.intValue() < 10 ? "0" + minutes.toPlainString() : minutes.toPlainString();
        String hourString = (hour < 10) ? ("0" + hour) : String.valueOf(hour);
        return hourString + ":" + minuteString;
    }

    /**
     * Returns the local rise/set time in the form HH:MM.
     * @param localTimeParam <code>BigDecimal</code> representation of the local rise/set time.
     * @param date Date to assign the local time
     * @return <code>Calendar</code> representation of the local time as a calendar, or null for none.
     */
    protected Calendar getLocalTimeAsCalendar(BigDecimal localTimeParam, Calendar date) {
        if (localTimeParam == null) {
            return null;
        }

        // Create a clone of the input calendar so we get locale/timezone information.
        Calendar resultTime = (Calendar) date.clone();

        BigDecimal localTime = localTimeParam;
        if (localTime.compareTo(BigDecimal.ZERO) < 0) {
            localTime = localTime.add(BigDecimal.valueOf(24.0D));
            resultTime.add(Calendar.HOUR_OF_DAY, -24);
        }
        
        String[] timeComponents = localTime.toPlainString().split("\\.");
        int hour = Integer.parseInt(timeComponents[0]);
        
        BigDecimal minutes = new BigDecimal("0." + timeComponents[1]);
        minutes = minutes.multiply(BigDecimal.valueOf(60)).setScale(0, RoundingMode.HALF_EVEN);
        
        if (minutes.intValue() == 60) {
            minutes = BigDecimal.ZERO;
            hour += 1;
        }
        
        if (hour == 24) hour = 0;

        // Set the local time
        resultTime.set(Calendar.HOUR_OF_DAY, hour);
        resultTime.set(Calendar.MINUTE, minutes.intValue());
        resultTime.set(Calendar.SECOND, 0);
        resultTime.set(Calendar.MILLISECOND, 0);
        resultTime.setTimeZone(date.getTimeZone());

        return resultTime;
    }

    /** ******* UTILITY METHODS (Should probably go somewhere else. ***************** */

    /**
     * Gets the day of year
     * @param date date ot use
     * @return dayof year
     */
    private BigDecimal getDayOfYear(Calendar date) {
        return new BigDecimal(date.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * Retrives the UTC offset for the provided ddate
     * @param date
     * @return
     */
    private BigDecimal getUTCOffSet(Calendar date) {
        BigDecimal offSetInMillis = new BigDecimal(date.get(Calendar.ZONE_OFFSET));
        return offSetInMillis.divide(new BigDecimal(3600000), new MathContext(2));
    }
    
    /**
     * Performs rounding and truncating of decimal places
     * @param number
     * @return
     */
    private BigDecimal setScale(BigDecimal number) {
    	return number.setScale(4, RoundingMode.HALF_EVEN);
    }

	/**
	 * @return the location
	 */
	public Coordinate getLocation() {
		return location;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}
}
