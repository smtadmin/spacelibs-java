package com.siliconmtn.core;

/****************************************************************************
 * <b>Title</b>: HashCodeUtil.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> * Collected methods which allow easy implementation of <code>hashCode</code>.
 * Example use case:
 * 
 * <pre>
 * public int hashCode() {
 * 	int result = HashCodeUtil.SEED;
 * 	//collect the contributions of various fields
 * 	result = HashCodeUtil.hash(result, fPrimitive);
 * 	result = HashCodeUtil.hash(result, fObject);
 * 	result = HashCodeUtil.hash(result, fArray);
 * 	return result;
 * }
 * </pre>
 * Example: HashCodeUtil.hash(HashCodeUtil.SEED, this.getKey());
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 12, 2021
 * @updates:
 ****************************************************************************/
public final class HashCodeUtil {

	/**
	 * 
	 */
	private static final int F_ODD_PRIME_NUMBER = 37;
	
	/**
	 * An initial value for a <code>hashCode</code>, to which is added
	 * contributions from fields. Using a non-zero value decreases collisions of
	 * <code>hashCode</code> values.
	 */
	public static final int SEED = 23;
	
	/**
	 * Private default constructor
	 */
	private HashCodeUtil() {
		super();
	}

	// ---- Implement the primitives --/
	
	/**
	 * Gets a hashcode for the given boolean value
	 * @param aBoolean hashes a boolean value
	 * @return hash for a boolean as an int
	 */
	public static int hash(boolean aBoolean) {
		return firstTerm() + (aBoolean ? 1 : 0);
	}

	/**
	 * Retrieves a hashcode for the given char
	 * @param aChar - Character to Utilize
	 * @return hash for a char as an int
	 */
	public static int hash(char aChar) {
		return firstTerm() + (int) aChar;
	}

	/**
	 * Implementation Note that byte and short are handled by this
	 * method, through implicit conversion.
	 * @param aInt integer to create a hash from
	 * @return Hashed Value
	 */
	public static int hash(int aInt) {
		return firstTerm() + aInt;
	}
	
	/**
	 * Creates a hashcode form the provided float value
	 * @param aFloat Float value to be hashed
	 * @return hashed value
	 */
	public static int hash(float aFloat) {
		return hash(Float.toHexString(aFloat));
	}

	/**
	 * Creates a hashcode form the provided double value
	 * @param aDouble double value to hash
	 * @return hashcode hashed value
	 */
	public static int hash(double aDouble) {
		return hash(Double.toHexString(aDouble));
	}
	
	// ---- Implement the objects/arrays --/
	
	/**
	 * <code>aObject</code> is a possibly-null object field, and possibly an array.
	 * If <code>aObject</code> is an array, then each element may be a primitive or
	 * a possibly-null object.
	 * @param aObject Object to be hashed.  Hashing depends mupon the object type
	 * @return Hashed value
	 */
	public static int hash(Object aObject) {
		if (aObject == null) {
			return 0;
		} else if (aObject instanceof String) {
			return (hashString(aObject.toString()));
			
		} else if (isArray(aObject)) {
			int total = 0;
			for (Object o : (Object[])aObject) {
				total += hash(o);
			}
			
			return total;
			
		} else {
			return hash(aObject.toString());
		}
	}
	
	/**
	 * Loops the chars and totals their ascii values
	 * @param val Hashes a string
	 * @return Hash of the string value
	 */
	private static int hashString(String val) {
		char[] item = val.toCharArray();
		int total = 0;
		for (char c : item) {
			total += (int) c;
		}
		
		return total;
	}

	/**
	 * Grabs the first term
	 * @return The first term value.  Used in the seeding
	 */
	private static int firstTerm() {
		return F_ODD_PRIME_NUMBER * SEED;
	}

	/**
	 * Determines if the provided class is an array
	 * @param obj determines if the object is an array
	 * @return true if an array.  false otherwise
	 */
	private static boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}

}
