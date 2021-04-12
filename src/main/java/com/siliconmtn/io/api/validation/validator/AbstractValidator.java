package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

// Spacelibs
import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

/****************************************************************************
 * <b>Title</b>: AbstractValidator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Abstract validator that handles default behaviour for validators
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public abstract class AbstractValidator implements ValidatorIntfc {


	/*
	 * (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.validator.ValidatorIntfc#validate(com.siliconmtn.io.api.validation.validator.ValidationDTO)
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		List<ValidationErrorDTO> errors = new ArrayList<>();
		
		if (validation.isRequired()) validateRequired(validation, errors);
		
		if (validation.getValidOptions() != null && !validation.isAlternateValidationId()) {
			validateOptions(validation, errors);
			
			// If we are validating against options this is all that needs to be done for validation
			return errors;
		}
		
		if (validation.getUriText() != null) validateUriText(validation, errors);		
		if (validation.getMin() != null) validateMin(validation, errors);
		if (validation.getMax() != null) validateMax(validation, errors);
		if (validation.getRegex() != null) validateRegex(validation, errors);
		
		return errors;
	}
	
	/**
	 * Determine whether the value is in the list of accepted values.
	 * @param validation validation meta data
	 * @param errors List of validation errors
	 */
	public void validateOptions(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		
		for (Entry<String, String> e : validation.getValidOptions().entrySet()) {
			// Value is in map, validation complete and successful.
			if (e.getValue() == null || e.getValue().equals(validation.getValue())) return;
		}
		
		errors.add(ValidationErrorDTO.builder()
				.elementId(validation.getElementId())
				.value(validation.getValue())
				.errorMessage("Value is not in the supplied list of accepted values")
				.validationError(ValidationError.OPTION)
				.build());
	}

	/*
	 * (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.validator.ValidatorIntfc#validateRequired(com.siliconmtn.io.api.validation.validator.ValidationDTO, java.util.List)
	 */
	@Override
	public void validateRequired(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (validation.isRequired() && StringUtil.isEmpty(validation.getValue())) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue()).errorMessage("Value is required and nothing was set").validationError(ValidationError.REQUIRED).build());
		}
	}
	
	/**
	 * Check to ensure that the supplied URI parameter matches the provided value.
	 * This is used to ensure that the payload of the request and the uri we are using
	 * to get through security are the same.
	 * @param validation DTO to be validated
	 * @param errors list of errors that will be returned after all validation is completed
	 */
	public void validateUriText(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (!validation.getUriText().equals(validation.getValue()))
			errors.add(ValidationErrorDTO.builder()
					.elementId(validation.getElementId())
					.value(validation.getValue())
					.errorMessage("Payload did not match provided uri.")
					.validationError(ValidationError.URIMATCH)
					.build());
	}

	/*
	 * (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.validator.ValidatorIntfc#validateRegex(com.siliconmtn.io.api.validation.validator.ValidationDTO, java.util.List)
	 */
	@Override
	public void validateRegex(ValidationDTO validation, List<ValidationErrorDTO> errors) { /* Empty default method */ }
	
	
}
