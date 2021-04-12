package com.siliconmtn.io.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

/****************************************************************************
 * <b>Title</b>: EndpointResponseTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Build out tests to check any stragler constructors 
 * and currently unused methods in the EndpointResponse 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 16, 2021
 * @updates:
 ****************************************************************************/

class EndpointResponseTest {
	
	
	/**
	 * Test the data only constructor as well as the addFailedValidation method
	 * @throws Exception
	 */
	@Test
	void testApiReponseDataConstructor() throws Exception {
		EndpointResponse dataConstructor = new EndpointResponse("Test");
		
		assertTrue(dataConstructor.getFailedValidations().isEmpty());
		
		dataConstructor.addFailedValidation(ValidationErrorDTO.builder()
				.elementId("Id")
				.value("42")
				.validationError(ValidationError.REGEX)
				.build());
		
		assertEquals("Test", dataConstructor.data);
		assertEquals(0, dataConstructor.getCount());
		assertEquals(HttpStatus.OK, dataConstructor.getStatus());
		assertTrue(dataConstructor.isSuccess());
		assertEquals(1, dataConstructor.getFailedValidations().size());
		assertEquals(ValidationError.REGEX, dataConstructor.getFailedValidations().get(0).getValidationError());
		
	}
	
	
	/**
	 * Test the data and count constructor
	 * @throws Exception
	 */
	@Test
	void testApiReponseDataCountConstructor() throws Exception {
		EndpointResponse dataConstructor = new EndpointResponse("Test", 5);
		assertEquals("Test", dataConstructor.data);
		assertEquals(5, dataConstructor.getCount());
		assertEquals(HttpStatus.OK, dataConstructor.getStatus());
		assertTrue(dataConstructor.isSuccess());
	}

}
