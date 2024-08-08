package com.siliconmtn.openapi;

import java.util.Map.Entry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * <b>Title:</b> OpenAPIBean.java <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Configures and registers a Bean for managing OpenAPI
 * Project level API Information.
 *
 * <b>Copyright:</b> 2022 <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Jul 7, 2022
 * @updates
 *
 */
@Configuration
public class OpenAPIBean {

	OpenAPIConfig config;

	OpenAPIBean(OpenAPIConfig config) {
		this.config = config;
	}

	@Bean
	OpenAPI libraryDefinition() {
		OpenAPI api = new OpenAPI().info(new Info().title(config.getTitle()).description(config.getDescription())
				.version(config.getVersion()).license(new License().name(config.getLicense())));

		// Add ability to specify Server Endpoints.
		if (!config.getServers().isEmpty()) {
			for (Entry<String, String> e : config.getServers().entrySet()) {
				api.addServersItem(new Server().url(e.getValue()).description(e.getKey().replace('_', ' ')));
			}
		}

		return api;
	}
}
