package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;

/****************************************************************************
 * <b>Title</b>: ValidatorIntfc.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Interface for the validators.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

public interface ValidatorIntfc {
	
	// Possible data types for a validator to have to handle
	public enum ValidatorType {
		STRING(StringValidator.class), 
		EMAIL(EmailValidator.class), 
		UUID(UUIDValidator.class), 
		NUMBER(NumberValidator.class), 
		DATE(DateValidator.class);
		
		
		Class<?> validator;
		ValidatorType(Class<?> validator) {
			this.validator = validator;
		}
		
		public Class<?> getValidator() { return this.validator; }
	}
	
	// Method for running through all applicable validators for the validator. 
	public List<ValidationErrorDTO> validate(ValidationDTO validation);

	// Validation methods for the validation parameters
	public void validateMin(ValidationDTO validation, List<ValidationErrorDTO> errors);
	public void validateMax(ValidationDTO validation, List<ValidationErrorDTO> errors);
	public void validateRegex(ValidationDTO validation, List<ValidationErrorDTO> errors);
	public void validateRequired(ValidationDTO validation, List<ValidationErrorDTO> errors);
	
}
