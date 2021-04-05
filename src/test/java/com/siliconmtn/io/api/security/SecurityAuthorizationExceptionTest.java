package com.siliconmtn.io.api.security;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title:</b> SecurityAuthorizationExceptionTest.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Unit test for the SecurityAuthorizationException class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 30, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
class SecurityAuthorizationExceptionTest {

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SecurityAuthorizationException#SecurityAuthorizationException(java.lang.String)}.
	 */
	@Test
	void testSecurityAuthorizationExceptionString() {
		SecurityAuthorizationException sae = new SecurityAuthorizationException("this is a test");
		assertNotNull(sae);
		assertEquals("this is a test", sae.getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SecurityAuthorizationException#SecurityAuthorizationException(java.lang.Throwable)}.
	 */
	@Test
	void testSecurityAuthorizationExceptionThrowable() {
		SecurityAuthorizationException sae = new SecurityAuthorizationException(new Throwable("Test"));
		assertNotNull(sae);
		assertEquals("Test", sae.getCause().getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SecurityAuthorizationException#SecurityAuthorizationException(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	void testSecurityAuthorizationExceptionStringThrowable() {
		SecurityAuthorizationException sae = new SecurityAuthorizationException("Another Test", new Throwable("Test"));
		assertNotNull(sae);
		assertEquals("Test", sae.getCause().getMessage());
		assertEquals("Another Test", sae.getMessage());
	}

}
