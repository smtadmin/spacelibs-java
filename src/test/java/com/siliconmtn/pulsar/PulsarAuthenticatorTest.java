/**
 * 
 */
package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.siliconmtn.io.http.SMTHttpConnectionManager;
import com.siliconmtn.io.http.SMTHttpConnectionManager.HttpConnectionType;

/**
 * @author raptor
 *
 */
@ExtendWith(MockitoExtension.class)
class PulsarAuthenticatorTest {

	@Mock
	PulsarConfig config;

	@InjectMocks
	PulsarAuthenticator auth;

	@Test
	void prepareManager() throws MalformedURLException {
		SMTHttpConnectionManager manager = new SMTHttpConnectionManager();
		auth.prepareManager(manager);
		assertTrue(manager.getRequestHeaders().containsKey(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE));
		assertEquals("application/x-www-form-urlencoded", manager.getRequestHeaders().get(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE));
	}

	@Test
	void updateTokenNoAuth() {
		when(config.hasNPEAuth()).thenReturn(false);
		when(config.hasJWTAuth()).thenReturn(false);
		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals("", auth.get());
	}

	@Test
	void updateTokenFromConfig() {
		when(config.hasNPEAuth()).thenReturn(false);
		when(config.hasJWTAuth()).thenReturn(true);
		String clientToken = "client";
		String adminToken = "admin";
		when(config.getClientJWT()).thenReturn(clientToken);
		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(clientToken, auth.get());
		when(config.getAdminJWT()).thenReturn(adminToken);
		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(adminToken, auth.get());
	}

	@SuppressWarnings("unchecked")
	@Test
	void retrieveNPEJWTToken() throws IOException {
		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(null));
		assertNull(auth.get());
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		String scope = "email";
		String id = "clientId";
		String secret = "clientSecret";
		String grantType = "CLIENT_CREDENTIALS";
		String tokenUri = "tokenUri";
		String accessToken = "token";
		Map<String, String> retData = new HashMap<>();
		retData.put(PulsarAuthenticator.ACCESS_TOKEN, accessToken);

		byte [] data = mapper.writeValueAsBytes(retData);
		SMTHttpConnectionManager manager = mock(SMTHttpConnectionManager.class);
		auth.manager = manager;

		when(config.getScope()).thenReturn(scope);
		when(config.getClientId()).thenReturn(id);
		when(config.getClientSecret()).thenReturn(secret);
		when(config.getAuthorizationGrantType()).thenReturn(grantType);
		when(config.getTokenUri()).thenReturn(tokenUri);

		when(manager.getRequestData(eq(tokenUri), any(Map.class), eq(HttpConnectionType.POST))).thenReturn(data);
		
		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(config));
		
		assertEquals(accessToken, auth.get());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void retrieveNPEJWTTokenCatchError() throws IOException {
		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(null));
		assertNull(auth.get());
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		String scope = "email";
		String id = "clientId";
		String secret = "clientSecret";
		String grantType = "CLIENT_CREDENTIALS";
		String tokenUri = "tokenUri";
		String accessToken = "token";
		Map<String, String> retData = new HashMap<>();
		retData.put(PulsarAuthenticator.ACCESS_TOKEN, accessToken);

		byte [] data = null;
		SMTHttpConnectionManager manager = mock(SMTHttpConnectionManager.class);
		auth.manager = manager;

		when(config.getScope()).thenReturn(scope);
		when(config.getClientId()).thenReturn(id);
		when(config.getClientSecret()).thenReturn(secret);
		when(config.getAuthorizationGrantType()).thenReturn(grantType);
		when(config.getTokenUri()).thenReturn(tokenUri);

		when(manager.getRequestData(eq(tokenUri), any(Map.class), eq(HttpConnectionType.POST))).thenReturn(data);

		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(config));

		assertEquals(null, auth.get());
	}

	@SuppressWarnings("unchecked")
	@Test
	void updateTokenFromOauth() throws IOException {
		when(config.hasNPEAuth()).thenReturn(true);
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		String scope = "email";
		String id = "clientId";
		String secret = "clientSecret";
		String grantType = "CLIENT_CREDENTIALS";
		String tokenUri = "tokenUri";
		String accessToken = "token";
		Map<String, String> retData = new HashMap<>();
		retData.put(PulsarAuthenticator.ACCESS_TOKEN, accessToken);

		byte [] data = mapper.writeValueAsBytes(retData);
		SMTHttpConnectionManager manager = mock(SMTHttpConnectionManager.class);
		auth.manager = manager;
		when(config.getScope()).thenReturn(scope);
		when(config.getClientId()).thenReturn(id);
		when(config.getClientSecret()).thenReturn(secret);
		when(config.getAuthorizationGrantType()).thenReturn(grantType);
		when(config.getTokenUri()).thenReturn(tokenUri);

		when(manager.getRequestData(eq(tokenUri), any(Map.class), eq(HttpConnectionType.POST))).thenReturn(data);

		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(accessToken, auth.get());
	}
}
