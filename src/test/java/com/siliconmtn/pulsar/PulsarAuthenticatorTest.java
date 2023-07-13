/**
 * 
 */
package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.util.Set;

import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

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

	@Mock
	ClientRegistrationRepository repo;

	@InjectMocks
	PulsarAuthenticator auth;

	@Test
	void prepareManager() throws MalformedURLException {
		SMTHttpConnectionManager manager = new SMTHttpConnectionManager();
		auth.prepareManager(manager);
		assertNotNull(manager.getSslSocketFactory());
		assertTrue(manager.getRequestHeaders().containsKey(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE));
		assertEquals(manager.getRequestHeaders().get(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE), "application/x-www-form-urlencoded");
	}

	@Test
	void updateTokenFromConfig() {
		String clientToken = "client";
		String adminToken = "admin";
		when(config.getClientJWT()).thenReturn(clientToken);
		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(clientToken, auth.get());
		when(config.getAdminJWT()).thenReturn(adminToken);
		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(adminToken, auth.get());
	}

	@Test
	void retrieveNPEJWTToken() throws IOException {
		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(null));
		assertNull(auth.get());

		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		String scope = "email";
		String id = "clientId";
		String secret = "clientSecret";
		AuthorizationGrantType grantType = AuthorizationGrantType.CLIENT_CREDENTIALS;
		String tokenUri = "tokenUri";
		String accessToken = "token";
		Map<String, String> retData = new HashMap<>();
		retData.put(PulsarAuthenticator.ACCESS_TOKEN, accessToken);

		byte [] data = mapper.writeValueAsBytes(retData);
		SMTHttpConnectionManager manager = mock(SMTHttpConnectionManager.class);
		ProviderDetails det = mock(ProviderDetails.class);
		ClientRegistration reg = mock(ClientRegistration.class);
		auth.manager = manager;

		when(reg.getScopes()).thenReturn(Set.of(scope));
		when(reg.getClientId()).thenReturn(id);
		when(reg.getClientSecret()).thenReturn(secret);
		when(reg.getAuthorizationGrantType()).thenReturn(grantType);
		when(reg.getProviderDetails()).thenReturn(det);
		when(det.getTokenUri()).thenReturn(tokenUri);

		when(manager.getRequestData(eq(tokenUri), any(Map.class), eq(HttpConnectionType.POST))).thenReturn(data);
		
		assertDoesNotThrow(() -> auth.retrieveNPEJWTToken(reg));
		
		assertEquals(accessToken, auth.get());
	}
	
	@Test
	void updateTokenFromOauth() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		String scope = "email";
		String id = "clientId";
		String secret = "clientSecret";
		AuthorizationGrantType grantType = AuthorizationGrantType.CLIENT_CREDENTIALS;
		String tokenUri = "tokenUri";
		String accessToken = "token";
		Map<String, String> retData = new HashMap<>();
		retData.put(PulsarAuthenticator.ACCESS_TOKEN, accessToken);

		byte [] data = mapper.writeValueAsBytes(retData);
		SMTHttpConnectionManager manager = mock(SMTHttpConnectionManager.class);
		ProviderDetails det = mock(ProviderDetails.class);
		ClientRegistration reg = mock(ClientRegistration.class);
		auth.manager = manager;
		when(repo.findByRegistrationId(PulsarAuthenticator.OAUTH_IDENTIFIER)).thenReturn(reg);
		when(reg.getScopes()).thenReturn(Set.of(scope));
		when(reg.getClientId()).thenReturn(id);
		when(reg.getClientSecret()).thenReturn(secret);
		when(reg.getAuthorizationGrantType()).thenReturn(grantType);
		when(reg.getProviderDetails()).thenReturn(det);
		when(det.getTokenUri()).thenReturn(tokenUri);

		when(manager.getRequestData(eq(tokenUri), any(Map.class), eq(HttpConnectionType.POST))).thenReturn(data);

		assertDoesNotThrow(() -> auth.updateToken());
		assertEquals(accessToken, auth.get());
	}
}
