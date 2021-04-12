package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;

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
public class UUIDValidator extends StringValidator {
	
	public static final String UUID_REGEX = "^[A-za-z0-9]*-[A-za-z0-9]*-[A-za-z0-9]*-[A-za-z0-9]*-[A-za-z0-9]*$";
	public static final String UUID_LENGTH = "36";
	


	/**
	 * Set validation parameters to the default if they are not 
	 * already provided the proceed with normal validation
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		
		if (validation.getMin() == null) {
			validation.setMin(UUID_LENGTH);
		}
		if (validation.getMax() == null) {
			validation.setMax(UUID_LENGTH);
		}
		if (validation.getRegex() == null) {
			validation.setRegex(UUID_REGEX);
		}
		return super.validate(validation);
	}

}
