package com.siliconmtn.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * <b>Title:</b> OpenAPIBeanTest.java
 * <b>Project:</b> Notifications MicroService
 * <b>Description:</b> Unit Tests providing coverage for the OpenAPIBean Class
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
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties={"com.siliconmtn.openapi.title=Testing API",
		"com.siliconmtn.openapi.description=Manages DB Interactions for the Human Feedback Questionnaire portion of HMF Sustem",
		"com.siliconmtn.openapi.version=v0.0.1",
		"com.siliconmtn.openapi.license=Apache 2.0",
		"com.siliconmtn.openapi.servers.XC_TEST=http://localhost"})
@ContextConfiguration(classes = { OpenAPIConfig.class, OpenAPIBean.class})
class OpenAPIBeanTest {

	@Autowired
	OpenAPIConfig config;

	@Autowired
	OpenAPI bean;

	@Test
	void openAPITestVerifyConfigLoadingAndAutoWire() {
		assertEquals(bean.getInfo().getTitle(), config.getTitle());
		assertEquals(bean.getInfo().getDescription(), config.getDescription());
		assertEquals(bean.getInfo().getVersion(), config.getVersion());
		assertEquals(bean.getInfo().getLicense().getName(), config.getLicense());
		assertEquals(bean.getServers().size(), config.getServers().size());
		assertEquals(bean.getServers().get(0).getDescription(), config.getServers().keySet().iterator().next().replace('_', ' '));
		assertEquals(bean.getServers().get(0).getUrl(), config.getServers().values().iterator().next());
	}

	@Test
	void openAPITestNoServersConstructorCreation() {
		OpenAPIConfig config = new OpenAPIConfig();
		OpenAPI bean = new OpenAPIBean(config).libraryDefinition();
		assertEquals(bean.getInfo().getTitle(), config.getTitle());
		assertEquals(bean.getInfo().getDescription(), config.getDescription());
		assertEquals(bean.getInfo().getVersion(), config.getVersion());
		assertEquals(bean.getInfo().getLicense().getName(), config.getLicense());
		assertNull(bean.getServers());
		assertEquals(0, config.getServers().size());
	}
}
