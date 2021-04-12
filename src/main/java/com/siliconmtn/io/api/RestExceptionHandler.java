package com.siliconmtn.io.api;

// Spring JPA
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

// Spring 5.x
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/****************************************************************************
 * <b>Title</b>: RestExceptionHandler.java
 * <b>Project</b>: planit-api
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

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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
            HttpStatus status, WebRequest request) {
        
    	String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiResponse(BAD_REQUEST, error, ex));
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
            HttpHeaders headers, HttpStatus status, WebRequest request) {
    	
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
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
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        
    	ApiResponse apiErrorResponse = new ApiResponse(BAD_REQUEST);
        apiErrorResponse.setMessage("Validation error");

        return buildResponseEntity(apiErrorResponse);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiResponse apiErrorResponse = new ApiResponse(BAD_REQUEST);
        apiErrorResponse.setMessage("Validation error");
        return buildResponseEntity(apiErrorResponse);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more 
     * detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiErrorResponse object
     */
    @ExceptionHandler(ApiRequestException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ApiRequestException ex) {
    	log.error("*********************");
        ApiResponse apiErrorResponse = new ApiResponse(NOT_FOUND);
        apiErrorResponse.setMessage(ex.getMessage());
        apiErrorResponse.setStatus(ex.getStatus());
        apiErrorResponse.addFailedValidations(ex.getFailedValidations());
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
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, error, ex));
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
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
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
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse apiErrorResponse = new ApiResponse(BAD_REQUEST);
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
        return buildResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND, ex));
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
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiResponse(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
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
        ApiResponse apiErrorResponse = new ApiResponse(BAD_REQUEST);
        Class<?> type = ex.getRequiredType();
        String name = type == null ? "" : type.getSimpleName();
        String msg = "The parameter '%s' of value '%s' could not be converted to type '%s'";
        apiErrorResponse.setMessage(String.format(msg, ex.getName(), ex.getValue(), name));
        apiErrorResponse.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiErrorResponse);
    }

    /**
     * Creates the response entity to format a common response to the UI
     * 
     * @param apiErrorResponse
     * @return
     */
    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiErrorResponse) {
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
    }

}
