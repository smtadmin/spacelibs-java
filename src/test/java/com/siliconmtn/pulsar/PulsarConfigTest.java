/**
 * 
 */
package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * 
 */
class PulsarConfigTest {

	@Test
	void hasJWTAuth() {
		PulsarConfig config = new PulsarConfig();
		assertFalse(config.hasJWTAuth());
		config.setClientJWT("test");
		assertTrue(config.hasJWTAuth());
		config.setAdminJWT("test");
		assertTrue(config.hasJWTAuth());
	}

	@Test
	void hasNPEAuth() {
		PulsarConfig config = new PulsarConfig();
		assertFalse(config.hasNPEAuth());
		config.setClientId("test");
		assertFalse(config.hasNPEAuth());
		config.setClientSecret("test");
		assertFalse(config.hasNPEAuth());
		config.setTokenUri("test");
		assertFalse(config.hasNPEAuth());
		config.setScope("test");
		assertFalse(config.hasNPEAuth());
		config.setAuthorizationGrantType("test");
		assertTrue(config.hasNPEAuth());
	}

	@Test
	void hasAuthWithClient() {
		PulsarConfig config = new PulsarConfig();
		assertFalse(config.hasAuth());
		config.setClientJWT("test");
		assertTrue(config.hasAuth());
		config.setAdminJWT("test");
		assertTrue(config.hasAuth());
	}

	@Test
	void hasAuthWithAdmin() {
		PulsarConfig config = new PulsarConfig();
		assertFalse(config.hasAuth());
		config.setAdminJWT("test");
		assertTrue(config.hasAuth());
	}
	
	@Test
	void hasAuthWithNPE() {
		PulsarConfig config = new PulsarConfig();
		assertFalse(config.hasAuth());
		config.setClientId("test");
		config.setClientSecret("test");
		config.setTokenUri("test");
		config.setScope("test");
		config.setAuthorizationGrantType("test");
		assertTrue(config.hasAuth());
	}
}
