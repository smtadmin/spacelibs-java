package com.siliconmtn.io.api;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

// Spring 5.5.x
import org.springframework.http.HttpStatus;

// SPacelibs
import com.siliconmtn.io.api.validation.ValidationErrorDTO;

/****************************************************************************
 * <b>Title</b>: ValidationException.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Exception to be passed when a validation has failed
 * on data that is passed into the end-point.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 4, 2021
 * @updates:
 ****************************************************************************/
public class EndpointRequestException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5297222511403186020L;
	
	/**
	 * Sets the status to 400 by default
	 */
	private final HttpStatus status;

	/**
	 * Collection of failed validations
	 */
    private final List<ValidationErrorDTO> failedValidations;

	/**
	 * Error message to display.  Status set to HttpStatus.BAD_REQUEST
	 * @param message Error Message to capture
	 */
	public EndpointRequestException(String message) {
		this(message, HttpStatus.BAD_REQUEST, new ArrayList<>());
	}
	
	/**
	 * Error message to display.  Status set to HttpStatus.BAD_REQUEST
	 * @param message Error Message to use
	 * @param errors Validation errors encountered
	 */
	public EndpointRequestException(String message, List<ValidationErrorDTO> errors) {
		this(message, HttpStatus.BAD_REQUEST, errors);
	}

	/**
	 * Error message and status constructor
	 * @param message Error message to display
	 * @param status HttpStatus to send
	 */
	public EndpointRequestException(String message, HttpStatus status) {
		this(message, status, new ArrayList<>());
	}

	/**
	 * Error message and status constructor
	 * @param message Error message to display
	 * @param status HttpStatus to send
	 * @param errors Validation errors encountered
	 */
	public EndpointRequestException(String message, HttpStatus status, List<ValidationErrorDTO> errors) {
		super(message);
		this.status = status;
		this.failedValidations = errors;
	}

	/**
	 * Init with exception and status
	 * @param cause Exception that was thrown
	 * @param status HttpStatus to be applied
	 */
	public EndpointRequestException(Throwable cause, HttpStatus status) {
		this("", cause, status);
	}

	/**
	 * Initialize with exception, message and status
	 * @param message Error message to display
	 * @param cause Exception that was thrown
	 * @param status HttpStatus to be applied
	 */
	public EndpointRequestException(String message, Throwable cause, HttpStatus status) {
		super(message, cause);
		this.status = status;
		failedValidations = new ArrayList<>();
	}

	/**
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}
    
    /**
     * Adds a single failed validation to the collection
     * @param failedValidation Failed validation object with failed validation meta data
     */
    public void addFailedValidation(ValidationErrorDTO failedValidation) {
    	this.failedValidations.add(failedValidation);
    }

    /**
     * Adds a all failed validation to the collection
     * @param failedValidations Adds the collection of failed validations
     */
    public void addAllFailedValidation(List<ValidationErrorDTO> failedValidations) {
    	this.failedValidations.addAll(failedValidations);
    }

    /**
     * Getter for the validation errors
     * @return Collection of the assigned validation errors
     */
    public List<ValidationErrorDTO> getFailedValidations() {
    	return this.failedValidations;
    }
    
}
