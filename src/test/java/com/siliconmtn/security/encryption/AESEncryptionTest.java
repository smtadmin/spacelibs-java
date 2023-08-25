package com.siliconmtn.security.encryption;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

// JDK 11.x
import java.util.HashMap;
import java.util.Map;

// Spacelibs 1.x
import com.siliconmtn.security.encryption.EncryptionFactory.EncryptionType;

/****************************************************************************
 * <b>Title</b>: AESEncryptionTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Unit Test for the AESEncryption class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 19, 2021
 * @updates:
 ****************************************************************************/
class AESEncryptionTest {
	
	// Members
	private String passPhrase = "a595c4ec034c007e";
	private String salt = "1234567890abcdef";

	/**
	 * Test method for {@link com.siliconmtn.security.encryption.AESEncryption#encrypt(java.lang.String)}.
	 */
	@Test
	void testEncrypt() throws Exception {
		Map<String, String> attr = new HashMap<>();
		attr.put(EncryptionIntfc.PASS_PHRASE_ELEMENT_NAME, passPhrase);
		attr.put(EncryptionIntfc.SALT_ELEMENT_NAME, salt);
		EncryptionIntfc ei = EncryptionFactory.getInstance(EncryptionType.AES_256, attr);
		String value = "HelloWorld";
		
		String encVal = ei.encrypt(value);
		String decVal = ei.decrypt(encVal);
		System.out.println(encVal + "|" + decVal);
		assertEquals(value, decVal);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.security.encryption.AESEncryption#encrypt(java.lang.String)}.
	 */
	@Test
	void testEncryptNoType() throws Exception {
		Map<String, String> attr = new HashMap<>();
		attr.put(EncryptionIntfc.PASS_PHRASE_ELEMENT_NAME, passPhrase);
		attr.put(EncryptionIntfc.SALT_ELEMENT_NAME, salt);
		EncryptionIntfc ei = EncryptionFactory.getInstance(null, attr);
		assertNull(ei);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.security.encryption.AESEncryption#decrypt(java.lang.String)}.
	 */
	@Test
	void testDecrypt() throws Exception {
		EncryptionIntfc ei = new AESEncryption(passPhrase, salt);
		String value = "3a793ea17226fc819e61b8ac2965db37817d31193205ad35fe4cafa180c73535";
		String decVal = ei.decrypt(value);
		assertEquals("HelloWorld", decVal);
	}

	/**
	 * Test method for {@link com.siliconmtn.security.encryption.AESEncryption#isEncrypted(java.lang.String)}.
	 */
	@Test
	void testIsEncryptedEmpty() throws Exception {
		EncryptionIntfc ei = new AESEncryption(passPhrase, salt);
		assertFalse(ei.isEncrypted(""));
		assertTrue(ei.isEncrypted("3a793ea17226fc819e61b8ac2965db37817d31193205ad35fe4cafa180c73535"));
		assertFalse(ei.isEncrypted("HelloWorld"));
		assertFalse(ei.isEncrypted("flow"));
	}
}
