package com.siliconmtn.io.api.validation.validator;

import java.util.ArrayList;
// JDK 11.x
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;
import com.siliconmtn.data.format.NumberUtil;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: IntegerValidator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Validator that handles validation of integer parameters.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public class NumberValidator extends AbstractValidator {
	
	/**
	 * Specialized validate method that checks to ensure that we have a proper number before we move on to other validations
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		// Ensure that there are no non-numeric characters in the value
		if (validation.getValue() != null && !StringUtil.isEmpty(validation.getValue()) 
				&& !StringUtil.defaultString(validation.getValue()).equals(StringUtil.removeNonNumeric(validation.getValue()))) {
			List<ValidationErrorDTO> errors = new ArrayList<>();
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage( "Value contains non-numeric characters " + validation.getValue()).validationError(ValidationError.PARSE).build());
			return errors;
		} else {
			return super.validate(validation);
		}
	}
	
	/**
	 * Ensure that the supplied number is not lower than the minimum
	 */
	@Override
	public void validateMin(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (NumberUtil.toInt(validation.getValue()) < NumberUtil.toInt(validation.getMin())) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value is under the required minimum value " + validation.getMin()).validationError(ValidationError.RANGE).build());
		}
	}

	/**
	 * Ensure that the supplied number is not higher than the maximum
	 */
	@Override
	public void validateMax(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (NumberUtil.toInt(validation.getValue()) > NumberUtil.toInt(validation.getMax())) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value is over the required maximum value " + validation.getMax()).validationError(ValidationError.RANGE).build());
		}
	}

}
