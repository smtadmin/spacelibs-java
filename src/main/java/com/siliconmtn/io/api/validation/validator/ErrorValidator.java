package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

/****************************************************************************
 * <b>Title</b>: ErrorValidator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Default validator that only is made when a catastrophic failure occurs. Will only fail whatever has been passed to it.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public class ErrorValidator extends AbstractValidator {

	/**
	 * Always returns a failure state since the only way to reach this is for the data to not
	 * be able to be validated by a standard validator.
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		List<ValidationErrorDTO> errors = new ArrayList<>();

		errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
				.errorMessage( "Failed to create proper validator for field, please contact an administrator about this issue").validationError(ValidationError.CATASTROPHE).build());
		
		return errors;
	}

	@Override
	public void validateMin(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		// required method that is does not get used here.
	}

	@Override
	public void validateMax(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		// required method that is does not get used here.
	}
}
