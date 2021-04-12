package com.siliconmtn.io.api.validation.validator;

// JDK 11.x
import java.util.Map;

// Spacelibs
import com.siliconmtn.io.api.validation.validator.ValidatorIntfc.ValidatorType;

// Lombok 1.18.x
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/****************************************************************************
 * <b>Title</b>: ValidationDTO.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> DTO containing all information needed to validation a single peice of information.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0git 
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/

@Data
@AllArgsConstructor
@Builder
public class ValidationDTO {

	private String elementId;
	private String value;
	private String min;
	private String max;
	private String regex;
	private String uriText;
	private boolean isRequired;
	private ValidatorType type;
	
	// Id of the selected option. Used primarily when we need to deal with special options
	private String optionId;
	// Map of options a value can be, mapped to the option id and it's actual value
	private Map<String, String> validOptions;
	// Value that indicates an alternative option is allowed
	private boolean alternateValidationId;

}
