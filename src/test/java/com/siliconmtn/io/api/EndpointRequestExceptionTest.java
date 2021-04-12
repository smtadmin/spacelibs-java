package com.siliconmtn.io.api;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

// Spacelibs 1.x
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

/****************************************************************************
 * <b>Title</b>: EndpointRequestExceptionTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Test the EndPointRequestException class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

class EndpointRequestExceptionTest {
	
	// Members
	List<ValidationErrorDTO> errors;

	/**
	 * Initializes the validation errors
	 */
	@BeforeEach
	void initMethod() {
		errors = new ArrayList<>();
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
	}
	
	/**
	 * Test the constructor that accepts a list of validation errors
	 * @throws Exception
	 */
	@Test
	void testApiRequestExceptionErrorConstructor() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", HttpStatus.ALREADY_REPORTED, errors);
		
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
	void testEndpointRequestExceptionCuaseStatusConsturctor() throws Exception {

		EndpointRequestException ex = new EndpointRequestException(new Throwable("Failed to do the thing"), HttpStatus.CONFLICT);
		
		assertEquals("Failed to do the thing", ex.getCause().getMessage());
		assertEquals(HttpStatus.CONFLICT, ex.getStatus());
		assertEquals(0, ex.getFailedValidations().size());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionString() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure");
		assertNotNull(ex);
		assertEquals(0, ex.getFailedValidations().size());
		assertEquals("Failure", ex.getMessage());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionStringStatus() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", HttpStatus.METHOD_NOT_ALLOWED);
		assertNotNull(ex);
		assertEquals(0, ex.getFailedValidations().size());
		assertEquals("Failure", ex.getMessage());
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, ex.getStatus());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionStringExStatus() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", new Throwable("Failed to do the thing"), HttpStatus.METHOD_NOT_ALLOWED);
		assertNotNull(ex);
		assertEquals(0, ex.getFailedValidations().size());
		assertEquals("Failure", ex.getMessage());
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, ex.getStatus());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionStringErrors() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", errors);
		assertNotNull(ex);
		assertEquals(2, ex.getFailedValidations().size());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		assertEquals("Failure", ex.getMessage());
		assertEquals("Douglas Adams", ex.getFailedValidations().get(1).getValue());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionAddErrors() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", errors);
		ex.addAllFailedValidation(errors);
		assertNotNull(ex);
		assertEquals(4, ex.getFailedValidations().size());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		assertEquals("Failure", ex.getMessage());
		assertEquals("Douglas Adams", ex.getFailedValidations().get(1).getValue());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.ApiRequestException#ApiRequestException(java.lang.String)}.
	 */
	@Test
	void testApiRequestExceptionAddSingleError() throws Exception {
		EndpointRequestException ex = new EndpointRequestException("Failure", errors);
		ex.addFailedValidation(errors.get(0));
		assertNotNull(ex);
		assertEquals(3, ex.getFailedValidations().size());
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
		assertEquals("Failure", ex.getMessage());
		assertEquals("Douglas Adams", ex.getFailedValidations().get(1).getValue());
	}
}
