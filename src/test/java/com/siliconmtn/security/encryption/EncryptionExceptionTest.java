package com.siliconmtn.security.encryption;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: EncryptionExceptionTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Unit test for the Encryption Exception class!
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 19, 2021
 * @updates:
 ****************************************************************************/
class EncryptionExceptionTest {
	
	/**
	 * Test method for {@link com.siliconmtn.security.encryption.EncryptionException#EncryptionException()}.
	 */
	@Test
	void testEncryptionException() throws Exception {
		EncryptionException ee = new EncryptionException();
		assertNull(ee.getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.security.encryption.EncryptionException#EncryptionException()}.
	 */
	@Test
	void testEncryptionExceptionWithMessage() throws Exception {
		EncryptionException ee = new EncryptionException("TESTING");
		assertEquals("TESTING", ee.getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.security.encryption.EncryptionException#EncryptionException(java.lang.Throwable)}.
	 */
	@Test
	void testEncryptionExceptionThrowable() throws Exception {
		Throwable t = new Throwable("THROWABLE_MSG");
		EncryptionException ee = new EncryptionException(t);
		assertEquals("THROWABLE_MSG", ee.getCause().getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.security.encryption.EncryptionException#EncryptionException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	void testEncryptionExceptionStringThrowable() throws Exception {
		Throwable t = new Throwable("THROWABLE_MSG");
		EncryptionException ee = new EncryptionException("TESTING", t);
		assertEquals("THROWABLE_MSG", ee.getCause().getMessage());
		assertEquals("TESTING", ee.getMessage());
	}

}
