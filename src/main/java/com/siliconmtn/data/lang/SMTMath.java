package com.siliconmtn.data.lang;

// JDK 11.x
import java.math.BigDecimal;
import java.math.RoundingMode;

/****************************************************************************
 * <b>Title</b>: SMTMath.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Micellaneous math functions
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 22, 2021
 * @updates:
 ****************************************************************************/
public class SMTMath {

	/**
	 * Only static methods allowed
	 */
	private SMTMath() {
		super();
	}

	/**
	 * Calculates the Arc Cosine for the given value
	 * @param radians Decimal value 
	 * @return Arccosine for the provided radians
	 */
    public static BigDecimal getArcCosineFor(BigDecimal radians) {
        BigDecimal arcCosine = BigDecimal.valueOf(Math.acos(radians.doubleValue()));
        return arcCosine.setScale(4, RoundingMode.HALF_EVEN);
    }

    /**
     * Converts Radians to degrees
     * @param radians Unit for measuring angles
     * @return degrees of the radian
     */
    public static  BigDecimal convertRadiansToDegrees(BigDecimal radians) {
        return multiplyBy(radians, BigDecimal.valueOf(180 / Math.PI));
    }

    /**
     * Converts degrees to radians
     * @param degrees Degrees to be converted
     * @return Unit for measuring angles from degrees
     */
    public static BigDecimal convertDegreesToRadians(BigDecimal degrees) {
        return multiplyBy(degrees, BigDecimal.valueOf(Math.PI / 180.0));
    }

    /**
     * BigDecimal multiplication with rounding to 4 digits
     * @param multiplicand value to be multiplied
     * @param multiplier value to by multiplied by
     * @return multiplied value
     */
    public static  BigDecimal multiplyBy(BigDecimal multiplicand, BigDecimal multiplier) {
        return multiplicand.multiply(multiplier).setScale(4, RoundingMode.HALF_EVEN);
    }

    /**
     * BigDecimal division with rounding to 4 digits
     * @param dividend value to be divided
     * @param divisor value to be divided by
     * @return divided value
     */
    public static  BigDecimal divideBy(BigDecimal dividend, BigDecimal divisor) {
    	if (divisor == null || divisor.intValue() == 0) return BigDecimal.valueOf(0.0);
    	else return dividend.divide(divisor, 4, RoundingMode.HALF_EVEN);
    }

}
