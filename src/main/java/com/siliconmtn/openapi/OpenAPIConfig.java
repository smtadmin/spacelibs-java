package com.siliconmtn.openapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Title:</b> OpenAPIConfig.java
 * <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Manages configuration of the OpenAPI data for the project
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
@ConfigurationProperties(prefix = "com.siliconmtn.openapi")
@ConfigurationPropertiesScan
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OpenAPIConfig {
	private String title;
	private String description;
	private String version;
	private String license;
	private Map<String, String> servers = new HashMap<>();
}