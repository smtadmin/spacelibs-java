package com.siliconmtn.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
@TestPropertySource(properties={"com.siliconmtn.oe.hmf.openapi.title=Testing API",
		"com.siliconmtn.oe.hmf.openapi.description=Manages DB Interactions for the Human Feedback Questionnaire portion of HMF Sustem",
		"com.siliconmtn.oe.hmf.openapi.version=v0.0.1",
		"com.siliconmtn.oe.hmf.openapi.license=Apache 2.0"})
@ContextConfiguration(classes = { OpenAPIConfig.class, OpenAPIBean.class})
class OpenAPIBeanTest {

	@Autowired
	OpenAPIConfig config;

	@Autowired
	OpenAPI bean;
	
	@Test
	void openAPITest() {
		assertEquals(bean.getInfo().getTitle(), config.getTitle());
		assertEquals(bean.getInfo().getDescription(), config.getDescription());
		assertEquals(bean.getInfo().getVersion(), config.getVersion());
		assertEquals(bean.getInfo().getLicense().getName(), config.getLicense());
	}
}
