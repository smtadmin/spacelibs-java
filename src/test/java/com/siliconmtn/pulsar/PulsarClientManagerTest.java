package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * <b>Title:</b> PulsarClientManagerTest.java
 * <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Unit Tests providing coverage for the PulsarClientManager Class
 *
 * <b>Copyright:</b> 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 14, 2022
 * @updates
 *
 */
@ActiveProfiles("test")
@ContextConfiguration(classes = { PulsarConfig.class, PulsarClientManager.class})
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
		Mockito.when(config.getUrl()).thenReturn("pulsar://localhost:6650");
		
		ClientConfigurationData conf = manager.buildClientConfig();
		assertEquals("pulsar://localhost:6650", conf.getServiceUrl());
	}
	@Test
	void pulsarClientTest() {
		assertDoesNotThrow(() -> bean.isClosed());
	}
}
