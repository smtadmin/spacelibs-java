package com.siliconmtn.security.encryption;

/****************************************************************************
 * <b>Title:</b> EncryptionIntfc.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> CHANGE ME!!
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 14, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public interface EncryptionIntfc {

	public static final String PASS_PHRASE_ELEMENT_NAME = "PASS_PHRASE";
	public static final String SALT_ELEMENT_NAME = "SALT_VALUE";
	public static final String VECTOR_ELEMENT_NAME = "VECTOR";
	public static final String PROVIDER_NAME = "BC";
	public static final String CHARSET_NAME = "US-ASCII";
	public static final String ALGORITHM_NAME = "AES";
	public static final String MODE_NAME = "CBC";
	public static final String PADDING_NAME = "PKCS5Padding";
	  
	/**
	 * Encrypts the provided data
	 * @param data Data to be encrypted
	 * @return Encrypted value
	 * @throws EncryptionException if value can't be encrypted
	 */
	public String encrypt(String data) throws EncryptionException ;
	
	/**
	 * Decrypts the provided data
	 * @param data Data to be encrypted
	 * @return Decrypted value
	 * @throws EncryptionException if value can't be decrypted
	 */
	public String decrypt(String data) throws EncryptionException ;
	
	/**
	 * Determines if the provided value is encrypted
	 * @param value Value to be checked
	 * @return True if encrypted, false otherwise
	 */
	public boolean isEncrypted(String value);
}
