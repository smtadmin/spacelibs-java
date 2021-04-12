package com.siliconmtn.io.api.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// JDK 11.x
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Junit 5
import org.junit.jupiter.api.Test;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;
import com.siliconmtn.io.api.validation.validator.ErrorValidator;
import com.siliconmtn.io.api.validation.validator.ValidationDTO;
import com.siliconmtn.io.api.validation.validator.ValidatorIntfc.ValidatorType;

/****************************************************************************
 * <b>Title</b>: ValidationUtilTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Test class that handles the validation utility and all associated
 * validators, DTO's, and factories.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

class ValidationUtilTest {


	/**
	 * Test the String validator, testing it's ability to fail based on min, max, isRequired, and regex.
	 */
	@Test
	void testStringValidators() {
		List<ValidationDTO> fields = new ArrayList<>(7);

		// Successful min, max, regex, and required test
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("value")
				.min("1")
				.max("100")
				.regex("va")
				.isRequired(true)
				.type(ValidatorType.STRING).build());
		// Fails due to minimum
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("value")
				.min("8")
				.type(ValidatorType.STRING).build());
		// Fails due to maximum
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("value")
				.max("2")
				.type(ValidatorType.STRING).build());
		// Fails due to regex
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("value")
				.regex("pie")
				.type(ValidatorType.STRING).build());
		// Fails due to required
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.isRequired(true)
				.type(ValidatorType.STRING).build());
		// Fails due to required
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("")
				.isRequired(true)
				.type(ValidatorType.STRING).build());
		// Successful validation
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.type(ValidatorType.STRING).build());
		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(5, errors.size());
		assertEquals(ValidationError.RANGE, errors.get(0).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(1).getValidationError());
		assertEquals(ValidationError.REGEX, errors.get(2).getValidationError());
		assertEquals(ValidationError.REQUIRED, errors.get(3).getValidationError());
		assertEquals(ValidationError.REQUIRED, errors.get(4).getValidationError());
		
	}

	/**
	 * Test the options aspect of the validator
	 */
	@Test
	void testOptionsValidation() {
		List<ValidationDTO> fields = new ArrayList<>(7);
		Map<String, String> options = new HashMap<>(6);
		options.put("1", "Apple");
		options.put("2", "Orange");
		options.put("3", "Grape");
		options.put("4", "Lime");
		options.put("5", "Canteloupe");
		options.put("6", "Banana");
		
		// Successful test
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Apple")
				.isRequired(true)
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		
		// Successful test with alternate validation id
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Pineapple")
				.optionId("6")
				.alternateValidationId(true)
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		
		// Successful test with alternate validation id but not using it
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Orange")
				.optionId("6")
				.isRequired(true)
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		
		// Successful test with nothing there
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.type(ValidatorType.STRING).build());
		
		// Failed test to due slight difference
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("apple")
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		
		// Failed test due to is required
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.isRequired(true)
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		
		// Failed test very different value
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Crested Gecko")
				.validOptions(options)
				.type(ValidatorType.STRING).build());
		

		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(4, errors.size());
		assertEquals(ValidationError.OPTION, errors.get(0).getValidationError());
		assertEquals(ValidationError.REQUIRED, errors.get(1).getValidationError());
		assertEquals(ValidationError.OPTION, errors.get(2).getValidationError());
		assertEquals(ValidationError.OPTION, errors.get(3).getValidationError());
		
	}
	
	
	/**
	 * Test the email validator, testing it's ability to fail based on regex 
	 * as the other methods are handled by its parent calss of StringValidator.
	 */
	@Test
	void testEmailValidators() {
		List<ValidationDTO> fields = new ArrayList<>(7);

		// Successful regex test
		fields.add(ValidationDTO.builder().elementId("id").value("edamschroder@siliconmtn.com").type(ValidatorType.EMAIL).build());
		fields.add(ValidationDTO.builder().elementId("id").value("fakeemail@somerandomemaildomain.uk").type(ValidatorType.EMAIL).build());
		// Fails due to regex
		fields.add(ValidationDTO.builder().elementId("id").value("edamsc@hroder@siliconmtn.com").type(ValidatorType.EMAIL).build());
		fields.add(ValidationDTO.builder().elementId("id").value("edamschrodersiliconmtn.com").type(ValidatorType.EMAIL).build());
		fields.add(ValidationDTO.builder().elementId("id").value("edamsc@hroder@siliconmtncom").type(ValidatorType.EMAIL).build());
		fields.add(ValidationDTO.builder().elementId("id").value("edamsc@hroder@siliconmtn.").type(ValidatorType.EMAIL).build());
		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(4, errors.size());
		assertEquals(ValidationError.REGEX, errors.get(0).getValidationError());
		assertEquals(ValidationError.REGEX, errors.get(1).getValidationError());
		assertEquals(ValidationError.REGEX, errors.get(2).getValidationError());
		assertEquals(ValidationError.REGEX, errors.get(3).getValidationError());
		
	}
	
	
	/**
	 * Test the email validator, testing it's ability to fail based on regex 
	 * as the other methods are handled by its parent calss of StringValidator.
	 */
	@Test
	void testUUIDValidators() {
		List<ValidationDTO> fields = new ArrayList<>(6);

		// Successful regex test
		fields.add(ValidationDTO.builder().elementId("id").value("fjhq-p1d84-fjqA2239-fjqa3334-fq343f9").type(ValidatorType.UUID).build());
		fields.add(ValidationDTO.builder().elementId("id").value("FD98av-shef-AN526UF-hfaqsdf-adfdJf26").type(ValidatorType.UUID).build());
		// Fails due to regex
		fields.add(ValidationDTO.builder().elementId("id").value("FD9-8a!shef-ANc56UF-hf3asdf-adfJaaa6").type(ValidatorType.UUID).build());
		fields.add(ValidationDTO.builder().elementId("id").value("FD98-icshef-AN5d6UF-hfa2sdfadfJ22ac6").type(ValidatorType.UUID).build());
		// Fails due to min length
		fields.add(ValidationDTO.builder().elementId("id").value("FD98-aisef-A56UF-hfadf-adfJ2").type(ValidatorType.UUID).build());
		// Fails due to max length
		fields.add(ValidationDTO.builder().elementId("id").value("FD98aiA-Sf3shef-AN5d6adsf43UF-hfads3shdf-adfafad3J26").type(ValidatorType.UUID).build());
		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(4, errors.size());
		assertEquals(ValidationError.REGEX, errors.get(0).getValidationError());
		assertEquals(ValidationError.REGEX, errors.get(1).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(2).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(3).getValidationError());
		
	}


	/**
	 * Test the date validator, testing it's ability to fail based on min, max, isRequired, 
	 * and the inability to parse any supplied date to be tested, either by the user or by the system
	 */
	@Test
	void testDateValidators() {
		List<ValidationDTO> fields = new ArrayList<>(7);

		// Successful min, max, and required test, ignoring the regex despite one being passed
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("10/10/2020")
				.min("10/9/2020")
				.max("10/11/2020")
				.regex("[A-Z]")
				.isRequired(true)
				.type(ValidatorType.DATE).build());
		//Fail due to parse min and parse max
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("9-8-2020")
				.min("December 12, 2020")
				.max("Octember 12, 2020")
				.type(ValidatorType.DATE).build());
		// Fails due to parse value
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Jul 17, 2021")
				.min("12/20/2020")
				.max("12/20/2020")
				.type(ValidatorType.DATE).build());
		// Fail due to not meeting min
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("9-8-2020")
				.min("12/20/2020")
				.type(ValidatorType.DATE).build());
		// Succeed despite not meeting min due to not having a value
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.min("12/20/2020")
				.type(ValidatorType.DATE).build());
		// Fail due to not meeting max
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("6/1/2030")
				.max("1/13/1920")
				.type(ValidatorType.DATE).build());
		// Succeed despite not meeting max due to not having a value
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.max("1/13/1920")
				.type(ValidatorType.DATE).build());
		// Fail due to required
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.isRequired(true)
				.type(ValidatorType.DATE).build());
		// Successful Test
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.type(ValidatorType.DATE).build());
		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(6, errors.size());
		assertEquals(ValidationError.PARSE, errors.get(0).getValidationError());
		assertEquals(ValidationError.PARSE, errors.get(1).getValidationError());
		assertEquals(ValidationError.PARSE, errors.get(2).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(3).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(4).getValidationError());
		assertEquals(ValidationError.REQUIRED, errors.get(5).getValidationError());
		
	}


	/**
	 * Test the date validator, testing it's ability to fail based on min, max, isRequired, 
	 * and the inability to parse any supplied date to be tested, either by the user or by the system
	 */
	@Test
	void testDefaultValidator() {
		List<ValidationDTO> fields = new ArrayList<>(1);

		// Fails due to lack of type
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("Test")
				.type(null).build());

		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(1, errors.size());
		assertEquals(ValidationError.CATASTROPHE, errors.get(0).getValidationError());
		
	}


	/**
	 * Test the date validator, testing it's ability to fail based on min, max, isRequired, 
	 * and the inability to parse any supplied date to be tested, either by the user or by the system
	 */
	@Test
	void checkDefaultValidatorUnusedMethods() {
		ErrorValidator e = new ErrorValidator();
		// Neither of these should do anything
		e.validateMin(null, null);
		e.validateMax(null, null);
	}


	/**
	 * Test the date validator, testing it's ability to fail based on min, max, isRequired, 
	 * and the inability to parse any supplied date to be tested, either by the user or by the system
	 */
	@Test
	void testNumberValidator() {
		List<ValidationDTO> fields = new ArrayList<>(7);

		// Succeeds min, max and requierd test, ignoring the regex despite one being passed
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("5")
				.min("1")
				.max("7")
				.regex("pointless")
				.isRequired(true)
				.type(ValidatorType.NUMBER).build());
		// Fails due to not providing a proper number
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("1234Five")
				.min("1")
				.max("7")
				.type(ValidatorType.NUMBER).build());
		// Succeeds with decimal number
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("5.0")
				.min("1")
				.max("7")
				.isRequired(true)
				.type(ValidatorType.NUMBER).build());
		// Fails due to min
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("8")
				.min("20")
				.type(ValidatorType.NUMBER).build());
		// Fails due to max
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.value("4")
				.max("2")
				.type(ValidatorType.NUMBER).build());
		//Fails due to required
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.isRequired(true)
				.type(ValidatorType.NUMBER).build());
		// Succeeds null value
		fields.add(ValidationDTO.builder()
				.elementId("id")
				.type(ValidatorType.NUMBER).build());
		
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(fields);
		
		assertEquals(4, errors.size());
		assertEquals(ValidationError.PARSE, errors.get(0).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(1).getValidationError());
		assertEquals(ValidationError.RANGE, errors.get(2).getValidationError());
		assertEquals(ValidationError.REQUIRED, errors.get(3).getValidationError());
		
	}
	
	/**
	 * Checks to ensure that an empty list of validatables returns properly.
	 */
	@Test
	void testNoValidation() {
		List<ValidationErrorDTO> errors = ValidationUtil.validateData(null);
		
		assertTrue(errors.isEmpty());
	}
}
