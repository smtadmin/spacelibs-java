package com.siliconmtn.io.api.security;

// Junit 5
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import org.junit.jupiter.api.BeforeEach;

/****************************************************************************
 * <b>Title</b>: JWTTokenCreatorTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Tests the JWT Token creator class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 11, 2021
 * @updates:
 ****************************************************************************/
class JWTTokenManagerTest {
	
	// Members
	private String token;
	
	/**
	 * 
	 */
	@BeforeEach
	void setupTokens() {
		String issuer = "SMT";
		String id = "12345";
		String email = "info@siliconmtn.com";
		String name = "Joe Developer";
		
		token = JWTTokenManager.getTestToken(issuer, id, email, name);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.JWTTokenManager#getTestToken()}.
	 */
	@Test
	void testGetTestToken() throws Exception {

		assertNotNull(token);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.JWTTokenManager#getTestToken()}.
	 */
	@Test
	void testGetTestTokenFails() throws Exception {
		
		try (MockedStatic<JWT> jwtMock = mockStatic(JWT.class)) {
			jwtMock.when(() -> JWT.create()).thenThrow(JWTCreationException.class);
			
			assertNull(JWTTokenManager.getTestToken(null, null, null, null));
		}
		
	}
	

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.JWTTokenManager#isTokenValid(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testIsTokenValid() throws Exception {
		assertTrue(JWTTokenManager.isTokenValid(token, "SMT"));
		assertFalse(JWTTokenManager.isTokenValid(token, "SMT Inc"));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.JWTTokenManager#getToken(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testGetToken() throws Exception {
		DecodedJWT jwt = JWTTokenManager.getToken(token, "SMT");
		assertNotNull(jwt);
		assertEquals("SMT", jwt.getClaim("iss").asString());
		assertEquals("info@siliconmtn.com", jwt.getClaim("email").asString());
		assertEquals("SMT", jwt.getClaim("owner").asString());
		assertEquals("12345", jwt.getClaim("sub").asString());
		assertEquals("Joe Developer", jwt.getClaim("preferred_username").asString());
		
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.JWTTokenManager#getToken(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testGetTokenFail() throws Exception {
		assertThrows(JWTDecodeException.class, () -> JWTTokenManager.getToken("Should throw an error", "SMT"));
	}
}
