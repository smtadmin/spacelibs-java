package com.siliconmtn.pulsar;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Title:</b> OAuthConfig.java
 * <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Manages configuration for the PulseClient OAuth security
 *
 * <b>Copyright:</b> 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 7, 2022
 * @updates
 *
 */
@Configuration
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OAuthConfig {

	String audience;
	String credentialsUrl;
	String issuerUrl;
}
