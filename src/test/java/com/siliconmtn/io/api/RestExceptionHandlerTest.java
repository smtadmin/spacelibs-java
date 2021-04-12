package com.siliconmtn.io.api;

// JEE 7
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

//Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

// Spring 5.5.x
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;

class RestExceptionHandlerTest {
	
	/**
	 * Test handleMissingServletRequestParameter exception
	 * @throws Exception
	 */
	@Test
	void testHandleMissingServletRequestParameter() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleMissingServletRequestParameter(new MissingServletRequestParameterException("emailAddress", "String"), null, null, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("emailAddress parameter is missing", ((EndpointResponse)resp.getBody()).getMessage());
	}
	
	/**
	 * Test handleHttpMediaTypeNotSupported exception
	 * @throws Exception
	 */
	@Test
    void testHandleHttpMediaTypeNotSupported() throws Exception {
		List<MediaType> approved = new ArrayList<>();

		approved.add(MediaType.APPLICATION_PDF);
		approved.add(MediaType.APPLICATION_JSON);
		
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException(MediaType.IMAGE_PNG, approved), null, null, null);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, resp.getStatusCode());
		assertEquals("image/png media type is not supported. Supported media types are application/pdf, application/json", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleHttpMediaTypeNotSupported exception
	 * @throws Exception
	 */
	@Test
    void testHandleMethodArgumentNotValid() throws Exception {
		MethodParameter p = mock(MethodParameter.class);
		BindingResult b = mock(BindingResult.class);
		
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleMethodArgumentNotValid(new MethodArgumentNotValidException(p, b), null, null, null);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Validation error", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleHttpMediaTypeNotSupported exception
	 * @throws Exception
	 */
	@Test
    void testHandleConstraintViolation() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleConstraintViolation(new ConstraintViolationException(null));

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Validation error", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleEntityNotFound exception
	 * @throws Exception
	 */
	@Test
    void testHandleApiEntityNotFound() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleEntityNotFound(new EndpointRequestException("Entity not found", HttpStatus.NOT_FOUND));

		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Entity not found", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleHttpMessageNotReadable exception
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("deprecation")
    void testHandleHttpMessageNotReadable() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleHttpMessageNotReadable(new HttpMessageNotReadableException("Entity not found"), null, null, null);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Malformed JSON request", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleHttpMessageNotWritable exception
	 * @throws Exception
	 */
	@Test
    void testHandleHttpMessageNotWritable() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleHttpMessageNotWritable(new HttpMessageNotWritableException("Cannot write"), null, null, null);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
		assertEquals("Error writing JSON output", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleNoHandlerFoundException exception
	 * @throws Exception
	 */
	@Test
    void testHandleNoHandlerFoundException() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleNoHandlerFoundException(new NoHandlerFoundException("POST", "/api/users", null), null, null, null);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Could not find the POST method for URL /api/users", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleEntityNotFound exception
	 * @throws Exception
	 */
	@Test
    void testHandleEntityNotFound() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleEntityNotFound(new EntityNotFoundException("Could not fnd requested entity"));

		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertEquals("Unexpected error", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleDataIntegrityViolation exception
	 * @throws Exception
	 */
	@Test
    void testHandleDataIntegrityViolation() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleDataIntegrityViolation(new DataIntegrityViolationException("Data error"), null);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
		assertEquals("Unexpected error", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	/**
	 * Test handleDataIntegrityViolation exception
	 * @throws Exception
	 */
	@Test
    void testHandleDataIntegrityConstraintViolation() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleDataIntegrityViolation(new DataIntegrityViolationException("Data error", 
				new ConstraintViolationException(null)) {private static final long serialVersionUID = 1L;}, null);

		assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
		assertEquals("Database error", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	
	/**
	 * Test handleMethodArgumentTypeMismatch exception
	 * @throws Exception
	 */
	@Test
    void testHandleMethodArgumentTypeMismatch() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		MethodParameter m = mock(MethodParameter.class);
		WebRequest w = mock(WebRequest.class);
		ResponseEntity<Object> resp = rest.handleMethodArgumentTypeMismatch(new MethodArgumentTypeMismatchException("pie", Integer.class
				, "emailAddress", m, new Throwable()), w);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("The parameter 'emailAddress' of value 'pie' could not be converted to type 'Integer'", ((EndpointResponse)resp.getBody()).getMessage());
    }
	
	
	/**
	 * Test handleMethodArgumentTypeMismatch exception
	 * @throws Exception
	 */
	@Test
    void testHandleMethodArgumentTypeMismatchNoType() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		MethodParameter m = mock(MethodParameter.class);
		WebRequest w = mock(WebRequest.class);
		ResponseEntity<Object> resp = rest.handleMethodArgumentTypeMismatch(new MethodArgumentTypeMismatchException("pie", null
				, "emailAddress", m, new Throwable()), w);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("The parameter 'emailAddress' of value 'pie' could not be converted to type ''", ((EndpointResponse)resp.getBody()).getMessage());
    }

}
