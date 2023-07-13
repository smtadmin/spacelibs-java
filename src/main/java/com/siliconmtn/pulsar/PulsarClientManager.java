package com.siliconmtn.pulsar;

import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * <b>Title:</b> PulsarClientManager.java <b>Project:</b> Notifications
 * MicroService <b>Description:</b> Bean that manages the Creation of the
 * PulsarClient for use with async messaging functionality.
 *
 * <b>Copyright:</b> 2022 <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 7, 2022
 * @updates
 *
 */
@Component
@Import({PulsarAuthenticator.class})
public class PulsarClientManager {

	@Autowired
	public PulsarConfig config;

	@Autowired
    private PulsarAuthenticator auth;

	protected ClientConfigurationData buildClientConfig() {
		// Prepare ClientConfigurationData
		ClientConfigurationData conf = new ClientConfigurationData();

		// Set Client URL
		conf.setServiceUrl(config.getUrl());
		conf.setConnectionTimeoutMs(1000);
		conf.setLookupTimeoutMs(1000);
		conf.setRequestTimeoutMs(1000);
		conf.setOperationTimeoutMs(1000);
		conf.setTlsAllowInsecureConnection(config.isTlsAllowInsecureConnection());
		conf.setAuthentication(AuthenticationFactory.token(auth));

		return conf;
	}

	@Bean
	PulsarClient createPulsarClient() throws PulsarClientException {

		PulsarClientImpl client = null;

		// Create actual Client.
		client = PulsarClientImpl.builder().conf(buildClientConfig()).build();

		// Return Client
		return client;
	}
}
