package com.siliconmtn.io.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

/****************************************************************************
 * <b>Title</b>: ApiRequestExceptionTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Test the 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

class ApiRequestExceptionTest {

	/**
	 * Test the constructor that accepts a list of validation errors
	 * @throws Exception
	 */
	@Test
	void testApiRequestExceptionErrorConsturctor() throws Exception {
		List<ValidationErrorDTO> errors = new ArrayList<>();
		errors.add(ValidationErrorDTO.builder()
				.elementId("Id")
				.value("42")
				.validationError(ValidationError.RANGE)
				.build());
		errors.add(ValidationErrorDTO.builder()
				.elementId("name")
				.value("Douglas Adams")
				.validationError(ValidationError.REGEX)
				.build());
		
		ApiRequestException ex = new ApiRequestException("Failure", HttpStatus.ALREADY_REPORTED, errors);
		
		assertEquals("Failure", ex.getMessage());
		assertEquals(HttpStatus.ALREADY_REPORTED, ex.getStatus());
		assertEquals(2, ex.getFailedValidations().size());
		assertEquals("Douglas Adams", ex.getFailedValidations().get(1).getValue());
		assertEquals(ValidationError.RANGE, ex.getFailedValidations().get(0).getValidationError());
		
	}

	/**
	 * Test the cause and status constructor along with the addFailedValidations methods
	 * @throws Exception
	 */
	@Test
	void testApiRequestExceptionCuaseStatusConsturctor() throws Exception {

		ApiRequestException ex = new ApiRequestException(new Throwable("Failed to do the thing"), HttpStatus.CONFLICT);
		
		assertEquals("Failed to do the thing", ex.getCause().getMessage());
		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
		assertEquals(0, ex.getFailedValidations().size());
		
		ex.addFailedValidation(ValidationErrorDTO.builder()
				.elementId("book")
				.value("Vogon Poetry")
				.validationError(ValidationError.CATASTROPHE)
				.build());

		assertEquals(1, ex.getFailedValidations().size());
		assertEquals(ValidationError.CATASTROPHE, ex.getFailedValidations().get(0).getValidationError());
		
		List<ValidationErrorDTO> errors = new ArrayList<>();
		errors.add(ValidationErrorDTO.builder()
				.elementId("Id")
				.value("42")
				.validationError(ValidationError.RANGE)
				.build());
		errors.add(ValidationErrorDTO.builder()
				.elementId("name")
				.value("Douglas Adams")
				.validationError(ValidationError.REGEX)
				.build());

		ex.addAllFailedValidation(errors);
		
		assertEquals(3, ex.getFailedValidations().size());
		assertEquals("42", ex.getFailedValidations().get(1).getValue());
		assertEquals("name", ex.getFailedValidations().get(2).getElementId());
	}

}
