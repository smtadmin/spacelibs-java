package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.List;
import java.util.regex.Pattern;

// Spacelibs
import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

// Lombok 1.18.x
import lombok.NoArgsConstructor;

/****************************************************************************
 * <b>Title</b>: StringValidator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Validator that handles validation of String parameters.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

@NoArgsConstructor
public class StringValidator extends AbstractValidator {

	/**
	 * Ensure that the supplied string's length is not shorter than the minimum required length
	 */
	@Override
	public void validateMin(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (StringUtil.defaultString(validation.getValue()).length() < Integer.parseInt(validation.getMin())) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value is under the required minimum length of " + validation.getMin()).validationError(ValidationError.RANGE).build());
		}
	}

	/**
	 * Ensure that the supplied string's length is not larger than the maximum required length
	 */
	@Override
	public void validateMax(ValidationDTO validation, List<ValidationErrorDTO> errors) {
		if (StringUtil.defaultString(validation.getValue()).length() > Integer.parseInt(validation.getMax())) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value is over the required maximum length of " + validation.getMax()).validationError(ValidationError.RANGE).build());
		}
	}

	/**
	 * Ensure that the supplied string matches the supplied regex pattern
	 */
	@Override
	public void validateRegex(ValidationDTO validation, List<ValidationErrorDTO> errors) {
	    Pattern pattern = Pattern.compile(validation.getRegex());
	    
		if (!pattern.matcher(StringUtil.defaultString(validation.getValue())).find()) {
			errors.add(ValidationErrorDTO.builder().elementId(validation.getElementId()).value(validation.getValue())
					.errorMessage("Value does not match the required pattern of " + validation.getRegex()).validationError(ValidationError.REGEX).build());
		}
	}

}
