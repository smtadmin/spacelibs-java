package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * <b>Title:</b> PulsarClientManagerTest.java <b>Project:</b> Notifications
 * MicroService <b>Description:</b> Unit Tests providing coverage for the
 * PulsarClientManager Class
 *
 * <b>Copyright:</b> 2022 <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 14, 2022
 * @updates
 *
 */
@ActiveProfiles("test")
@ContextConfiguration(classes = { PulsarConfig.class, PulsarClientManager.class })
class PulsarClientManagerTest {

	@Mock
	PulsarConfig config;

	@Mock
	PulsarClient bean;

	@InjectMocks
	PulsarClientManager manager;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void buildPulsarClientConfigTest() throws MalformedURLException {
		when(config.getUrl()).thenReturn("pulsar://localhost:6650");
		when(config.hasAuth()).thenReturn(false);

		ClientConfigurationData conf = manager.buildClientConfig();
		assertEquals("pulsar://localhost:6650", conf.getServiceUrl());
	}

	@Test
	void buildPulsarClientConfigTestWithClientJWT() throws MalformedURLException {
		when(config.getUrl()).thenReturn("pulsar://localhost:6650");
		when(config.getClientJWT()).thenReturn("HelloWorld");
		when(config.hasAuth()).thenReturn(true);
		ClientConfigurationData conf = manager.buildClientConfig();
		assertNotNull(conf.getAuthentication());
	}

	@Test
	void pulsarClientTest() {
		when(config.getUrl()).thenReturn("pulsar://localhost:6650");
		when(config.getClientJWT()).thenReturn("HelloWorld");
		PulsarClient client = assertDoesNotThrow(() -> manager.createPulsarClient());
		assertNotNull(client);
	}
}
