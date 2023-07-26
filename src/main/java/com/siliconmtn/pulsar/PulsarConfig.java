package com.siliconmtn.pulsar;

import java.util.Map;

import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
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
	private String cronSchedule;
	private boolean tlsAllowInsecureConnection;
	private Map<String, TopicConfig> topics;
	private String clientId;
	private String clientSecret;
	private String scope;
	private String tokenUri;
	private String authorizationGrantType;

	/**
	 * Helper method to check if we have a valid JWT Token Configuration
	 * @return
	 */
	public boolean hasJWTAuth() {
		return !StringUtils.isEmpty(adminJWT) || !StringUtils.isEmpty(clientJWT);
	}

	/**
	 * Helper method to check if we have a valid NPE Configuration
	 * @return
	 */
	public boolean hasNPEAuth() {
		return !StringUtils.isEmpty(clientId) && !StringUtils.isEmpty(clientSecret) && !StringUtils.isEmpty(tokenUri) && !StringUtils.isEmpty(scope) && !StringUtils.isEmpty(authorizationGrantType);
	}

	/**
	 * Helper method to determine if there is a valid authentication method present in config.
	 * @return
	 */
	public boolean hasAuth() {
		return hasJWTAuth() || hasNPEAuth();
	}
}