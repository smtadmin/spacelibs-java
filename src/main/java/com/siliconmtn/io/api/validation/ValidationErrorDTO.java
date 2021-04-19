package com.siliconmtn.io.api.validation;

// JDK 11.x
import java.io.Serializable;

// Lombok 1.18.x
import lombok.Data;
import lombok.Builder;

/****************************************************************************
 * <b>Title</b>: ValidationErrorDTO.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> DTO for returning pertinent information as to how a particular
 * peice of submitted data failed validation.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

@Data
@Builder
public class ValidationErrorDTO implements Serializable {
	/**
	 * Enum containing the type of validation failed on the field
	 */
	public enum ValidationError {
		REQUIRED, REGEX, RANGE, OPTION, CATASTROPHE, PARSE, URIMATCH
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary key of the data element that failed validation
	 */
	private String elementId;
	
	/**
	 * Value of the failed validation
	 */
	private Object value;
	
	/**
	 * Validation error message
	 */
	private String errorMessage; 
	
	/**
	 * The type of validation that failed
	 */
	private ValidationError validationError;

}
