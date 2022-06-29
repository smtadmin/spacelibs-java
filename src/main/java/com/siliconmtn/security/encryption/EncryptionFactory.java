package com.siliconmtn.security.encryption;

// JDK 11.x
import java.util.Map;

/****************************************************************************
 * <b>Title</b>: EncryptionFactory.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Factory to return the appropriate Encryption class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 19, 2021
 * @updates:
 ****************************************************************************/
public class EncryptionFactory {
	/**
	 * Enum to define the types of available encryption
	 * @author etewa
	 *
	 */
	public enum EncryptionType {
		AES_256
	}

	/**
	 * 
	 */
	private EncryptionFactory() {
		super();
	}

	/**
	 * Returns an instance of the requested encryption type
	 * @param type Type of encryption class to return
	 * @param attributes Holds the keys, pass phrases, salts, etc ...
	 * @return Concrete implementation of the encryption interface
	 */
	public static EncryptionIntfc getInstance(EncryptionType type, Map<String, String> attributes) {
		
		if (EncryptionType.AES_256.equals(type)) return new AESEncryption(attributes);
		return null;
	}
}
