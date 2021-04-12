package com.siliconmtn.io.api;

// JDK 11.x
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Spring 5.5.x
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

// Faster XML 2.1.x
import com.fasterxml.jackson.annotation.JsonFormat;

// PlanIt apps
import com.siliconmtn.io.api.validation.ValidationErrorDTO;

// Lombok 1.18.x
import lombok.Data;

/****************************************************************************
 * <b>Title</b>: ApiErrorResponse.java
 * <b>Project</b>: planit-api
 * <b>Description: </b> Common error response data when sending errors to the server
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 4, 2021
 * @updates:
 ****************************************************************************/
@Data
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class EndpointResponse {

	// Members
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected HttpStatus status;
    protected LocalDateTime timestamp;
    protected String message;
    protected String debugMessage;
    protected boolean isSuccess = false;
    protected int count;
    protected Object data;
    protected List<ValidationErrorDTO> failedValidations = new ArrayList<>();

    /**
     * Sets the current time when initializing
     */
    private EndpointResponse() {
    	super();
        timestamp = LocalDateTime.now();
        
    }
    
    /**
     * Success response with the data and count
     * @param data Object data for the payload to the endpoint
     */
    public EndpointResponse(Object data) {
    	this();
    	this.data = data;
    	this.count = 0;
    	this.isSuccess = true;
    	this.status = HttpStatus.OK;
    }
    
    /**
     * Success response with the data and count
     * @param data Object data for the payload to the endpoint
     * @param count Number of items.  Useful with server side pagination
     */
    public EndpointResponse(Object data, int count) {
    	this();
    	this.data = data;
    	this.count = count;
    	this.isSuccess = true;
    	this.status = HttpStatus.OK;
    }

    /**
     * Assigns the HTTP status and the the time
     * @param status HttpStatus to return
     */
    public EndpointResponse(HttpStatus status) {
        this();
        this.status = status;
    }
    
    /**
     * Assigns the status, time and exception stuff
     * @param status HttpStatus to return
     * @param ex Exception that was thrown
     */
    public EndpointResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Assigns all parameters with this constructor
     * @param status HttpStatus to return
     * @param message Error message to display
     * @param ex Exception that was thrown
     */
    public EndpointResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
    
    /**
     * Adds a single failed validation to the collection
     * @param failedValidation Adds a failed validation to the exception
     */
    public void addFailedValidation(ValidationErrorDTO failedValidation) {
    	this.failedValidations.add(failedValidation);
    }
    
    /**
     * Adds a all failed validations to the collection
     * @param failedValidations Collection of the failed validations
     */
    public void addFailedValidations(List<ValidationErrorDTO> failedValidations) {
    	this.failedValidations.addAll(failedValidations);
    }
}
