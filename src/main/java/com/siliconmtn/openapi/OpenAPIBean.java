package com.siliconmtn.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * <b>Title:</b> OpenAPIBean.java
 * <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Configures and registers a Bean for managing OpenAPI
 * Project level API Information.
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
@ConfigurationProperties(prefix = "com.siliconmtn.oe.hmf.openapi") 
@ConfigurationPropertiesScan
public class OpenAPIBean {

	@Autowired
	OpenAPIConfig config;

    @Bean
    OpenAPI libraryDefinition() {
        return new OpenAPI().info(new Info().title(config.getTitle())
                .description(config.getDescription())
                .version(config.getVersion()).license(new License().name(config.getLicense())));
    }
}
