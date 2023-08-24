package com.siliconmtn.io.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;

// Spring JPA
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

// Spring 5.x
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.siliconmtn.io.api.security.SecurityAuthorizationException;
import com.siliconmtn.io.api.validation.ValidationErrorDTO;
import com.siliconmtn.io.api.validation.ValidationErrorDTO.ValidationError;

// Log4J 2.x
import lombok.extern.log4j.Log4j2;

/****************************************************************************
 * <b>Title</b>: RestExceptionHandler.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Handles various errors that can be thrown withn the 
 * Spring application and formats the errors into a common response
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 4, 2021
 * @updates:
 ****************************************************************************/
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Customize the response for HttpRequestMethodNotSupportedException.
	 * <p>This method logs a warning, sets the "Allow" header, and delegates to
	 * {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Method is Not Supported", ex));
	}
	
	/**
	 * Customize the response for HttpMediaTypeNotAcceptableException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	@Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
			HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Media Type is Not Acceptable", ex));		
	}
	
	/**
	 * Customize the response for MissingPathVariableException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
    		MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "A Path variable is missing on this request", ex));		
	}
	
	/**
	 * Customize the response for ServletRequestBindingException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
    		ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Service Binding Exception", ex));		
	}

	/**
	 * Customize the response for ConversionNotSupportedException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
    		ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Unable to Convert data element", ex));
    }
    
	/**
	 * Customize the response for TypeMismatchException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
    		TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Data Type Mismatch", ex));		
	}

	/**
	 * Customize the response for MissingServletRequestPartException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
    		MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "multipart/form-data error", ex));	
    }
	
	/**
	 * Customize the response for BindException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleBindException(
    		BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Unable to Support a Binding Result", ex));		
	}
	
	/**
	 * Customize the response for AsyncRequestTimeoutException.
	 * <p>This method delegates to {@link #buildResponseEntity}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param webRequest the current request
	 * @return a {@code ResponseEntity} instance
	 */
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
    		AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex);
		return buildResponseEntity(new EndpointResponse(status, "Request Timed Out", ex));		
	}
	
    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException Exception to be processed
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
    	log.error(ex);
    	String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new EndpointResponse(BAD_REQUEST, error, ex));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException  Exception to be processed
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus Status to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	log.error(ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new EndpointResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more 
     * detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(EndpointRequestException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EndpointRequestException ex) {
    	log.error(ex);
    	EndpointResponse apiErrorResponse = new EndpointResponse(NOT_FOUND);
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setStatus(ex.getStatus());
        apiErrorResponse.addFailedValidations(ex.getFailedValidations());
        return buildResponseEntity(apiErrorResponse);
    }
    
    /**
     * Handles SecurityAuthorizationException. This exception is thrown when 
     * a malformed or malicious request is made and trapped
     *
     * @param ex the SecurityAuthorizationException
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(SecurityAuthorizationException.class)
    protected ResponseEntity<Object> handleSecurityAuthorizationException(SecurityAuthorizationException ex) {
    	log.error(ex);
    	EndpointResponse apiErrorResponse = new EndpointResponse(NOT_FOUND);
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setStatus(HttpStatus.FORBIDDEN);
        return buildResponseEntity(apiErrorResponse);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException Not readable exception
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	log.error(ex);
    	String error = "Malformed JSON request";
        return buildResponseEntity(new EndpointResponse(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException Not writable exception
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	log.error(ex);
    	String error = "Error writing JSON output";
        return buildResponseEntity(new EndpointResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex NoHandlerFoundException Unable to find a suitable endpoint
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    	log.error(ex);       
    	EndpointResponse apiErrorResponse = new EndpointResponse(BAD_REQUEST);
        apiErrorResponse.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiErrorResponse.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiErrorResponse);
    }
    
    /**
     * Handle EntityNotFound Exception
     * 
     * @param ex EntityNotFound Exception.
     * @return The ApiErrorResponse object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    	log.error(ex);
        return buildResponseEntity(new EndpointResponse(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,WebRequest request) {
    	log.error(ex);
        return buildResponseEntity(new EndpointResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
    	log.error(ex);
        EndpointResponse apiErrorResponse = new EndpointResponse(BAD_REQUEST);
        Class<?> type = ex.getRequiredType();
        String name = type == null ? "" : type.getSimpleName();
        String msg = "The parameter '%s' of value '%s' could not be converted to type '%s'";
        apiErrorResponse.setMessage(String.format(msg, ex.getName(), ex.getValue(), name));
        apiErrorResponse.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiErrorResponse);
    } 
    
	/**
	 * Handle Constraint violations in data validation
	 * 
	 * @param e The exception
	 * @return the ApiErrorResponse object
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> onConstraintViolationException(ConstraintViolationException e) {
		var failedValidations = new ArrayList<ValidationErrorDTO>();
		for (var violation : e.getConstraintViolations()) 
		{
			var validationError = ValidationErrorDTO.builder()
					.elementId(violation.getPropertyPath().toString())
					.value(violation.getInvalidValue())
					.errorMessage(violation.getMessage())
					.validationError(ValidationError.PARSE)
					.build();
			failedValidations.add(validationError);
		}
		
		var apiErrorResponse = new EndpointResponse(BAD_REQUEST);
		apiErrorResponse.setFailedValidations(failedValidations);
		apiErrorResponse.setMessage("Input validation error");
		apiErrorResponse.setDebugMessage(e.getMessage());
		
		return buildResponseEntity(apiErrorResponse);
	}
	
    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders Response headers
     * @param status  HttpStatus to be returned
     * @param request WebRequest Request Metadata
     * @return the ApiErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
    		MethodArgumentNotValidException ex,
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) 
    {
    	var failedValidations = new ArrayList<ValidationErrorDTO>();
    	for (var violation : ex.getBindingResult().getFieldErrors()) 
    	{
    		var validationError = ValidationErrorDTO.builder()
    				.elementId(violation.getField())
    				.errorMessage(violation.getDefaultMessage())
    				.value(violation.getRejectedValue())
    				.validationError(ValidationError.PARSE)
    				.build();
    		failedValidations.add(validationError);
    	}
    	
    	var apiErrorResponse = new EndpointResponse(BAD_REQUEST);
        apiErrorResponse.setMessage("Input validation error");
        apiErrorResponse.setFailedValidations(failedValidations);
        
        return buildResponseEntity(apiErrorResponse);
    }
    
    /**
     * Catch all handler to pick up any exceptions missed from the previous handlers
     * 
     * @param ex the Exception
     * @param request WebRequest Metadata
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler({ NullPointerException.class })
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error(ex);
        EndpointResponse apiErrorResponse = new EndpointResponse(BAD_REQUEST);
        apiErrorResponse.setMessage(ex.getClass().getName());
        apiErrorResponse.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiErrorResponse);
    }

    /**
     * Creates the response entity to format a common response to the UI
     * 
     * @param apiErrorResponse
     * @return
     */
    private ResponseEntity<Object> buildResponseEntity(EndpointResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

}
