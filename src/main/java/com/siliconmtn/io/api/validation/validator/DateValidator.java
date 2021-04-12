package com.siliconmtn.io.api.validation.validator;

import java.util.ArrayList;
import java.util.Date;
// JDK 11.x
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;
import com.siliconmtn.data.format.DateFormat;

/****************************************************************************
 * <b>Title</b>: DateValidator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Validator that handles validation of date parameters.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public class DateValidator extends AbstractValidator {
	
	/**
	 * Specialized validate method that checks to ensure that we have a proper date before we move on to other validations
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		if (validation.getValue() != null && DateFormat.parseUnknownPattern(validation.getValue()) == null) {
			List<ValidationErrorDTO> errors = new ArrayList<>();
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage( "Unable to properly parse submitted date of " + validation.getValue()).validationError(ValidationError.PARSE).build());
			return errors;
		} else {
			return super.validate(validation);
		}
	}

	/**
	 * Ensure that the supplied number is not lower than the minimum
	 * Additionally, ensure that the supplied dates, for both value and min are valid dates.
	 */
	@Override
	public void validateMin(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (validation.getValue() == null) return;
		Date min = DateFormat.parseUnknownPattern(validation.getMin());
		if (min == null) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage( "Unable to properly parse required minimum date of " + validation.getMin()).validationError(ValidationError.PARSE).build());
			return;
		}
		if (DateFormat.parseUnknownPattern(validation.getValue()).before(DateFormat.parseUnknownPattern(validation.getMin()))) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage( "Value is before the minumum date of " + validation.getMin()).validationError(ValidationError.RANGE).build());
		}
	}

	/**
	 * Ensure that the supplied number is not higher than the maximum
	 * Additionally, ensure that the supplied dates, for both value and max are valid dates.
	 */
	@Override
	public void validateMax(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (validation.getValue() == null) return;
		Date min = DateFormat.parseUnknownPattern(validation.getMax());
		if (min == null) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage( "Unable to properly parse required maximum date of " + validation.getMax()).validationError(ValidationError.PARSE).build());
			return;
		}
		if (DateFormat.parseUnknownPattern(validation.getValue()).after(DateFormat.parseUnknownPattern(validation.getMax()))) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value is after the maximum date of " + validation.getMax()).validationError(ValidationError.RANGE).build());
		}
	}

}
