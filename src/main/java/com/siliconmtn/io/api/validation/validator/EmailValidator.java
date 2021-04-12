package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.List;

// Spacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;

// Lombok 1.18.x
import lombok.NoArgsConstructor;

/****************************************************************************
 * <b>Title</b>: EmailValidator.java
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
public class EmailValidator extends StringValidator {
	
	public static final String EMAIL_REGEX = "^\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b$";


	/**
	 * If a regex has not been provided use the default EMAIL_REGEX and then treat as a normal string
	 */
	@Override
	public List<ValidationErrorDTO> validate(ValidationDTO validation) {
		if (validation.getRegex() == null) {
			validation.setRegex(EMAIL_REGEX);
		}
		return super.validate(validation);
	}

}
