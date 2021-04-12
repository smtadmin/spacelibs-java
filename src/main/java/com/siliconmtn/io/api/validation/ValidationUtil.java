package com.siliconmtn.io.api.validation;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.validator.ValidationDTO;
import com.siliconmtn.io.api.validation.validator.ValidatorIntfc;

/****************************************************************************
 * <b>Title</b>: ValidationUtil.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Loops through a supplied list of ValidationDTOs and applies the appropriate
 *  validation to each of them. Returns a list of errors that occurred during the validation.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public class ValidationUtil {
	
	/**
	 * Provate due to all methods being static
	 */
	private ValidationUtil() {
		super();
	}
	
	/**
	 * Loop through the supplied list ValidationDTOs and run them through the proper validation procedures.
	 * @param fields List of ValidationDTOs to be run through the validation process.
	 * @return List of ValidationErrorDTOs that list what failed and why.
	 */
	public static List<ValidationErrorDTO> validateData(List<ValidationDTO> fields) {
		List<ValidationErrorDTO> errors = new ArrayList<>();
		
		if (fields == null) return errors;
		
		for (ValidationDTO field : fields) {
			ValidatorIntfc validator = ValidationFactory.getValidator(field.getType());
			errors.addAll(validator.validate(field));
		}
		
		return errors;
	}

}
