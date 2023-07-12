package com.siliconmtn.pulsar;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Title:</b> PulsarConfig.java <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Manages configuration for the PulsarClient
 *
 * <b>Copyright:</b> 2022 <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 7, 2022
 * @updates
 *
 */
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "pulsar")
@Configuration
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PulsarConfig {
	private String key;
	private String url;
	private String securityMode;
	private String clientJWT;
	private String adminJWT;
	private boolean tlsAllowInsecureConnection;
	private String clientNpeId;
	private String clientNPESecret;
	private String keyCloakUrl;
	private OAuthConfig oauth2;
	private Map<String, TopicConfig> topics;
}