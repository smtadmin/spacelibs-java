package com.siliconmtn.io.api;

// JEE 7
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

//Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
// Spring 5.5.x
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.siliconmtn.io.api.security.SecurityAuthorizationException;

import java.lang.reflect.Method;
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
		Method m = String.class.getDeclaredMethod("concat", String.class);
		MethodParameter p = new MethodParameter(m, 0);
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

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleHttpRequestMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleHttpRequestMethodNotSupported() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleHttpRequestMethodNotSupported(new HttpRequestMethodNotSupportedException("emailAddress", "String"), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Method is Not Supported", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleHttpMediaTypeNotAcceptable(org.springframework.web.HttpMediaTypeNotAcceptableException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleHttpMediaTypeNotAcceptable() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleHttpMediaTypeNotAcceptable(new HttpMediaTypeNotAcceptableException("emailAddress"), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Media Type is Not Acceptable", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleMissingPathVariable(org.springframework.web.bind.MissingPathVariableException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleMissingPathVariable() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		Method m = String.class.getDeclaredMethod("concat", String.class);
		MethodParameter mp = new MethodParameter(m, 0);
		
		ResponseEntity<Object> resp = rest.handleMissingPathVariable(new MissingPathVariableException("Some Val", mp), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("A Path variable is missing on this request", ((EndpointResponse)resp.getBody()).getMessage());
		
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleServletRequestBindingException(org.springframework.web.bind.ServletRequestBindingException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleServletRequestBindingException() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleServletRequestBindingException(new ServletRequestBindingException("emailAddress"), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Service Binding Exception", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleConversionNotSupported(org.springframework.beans.ConversionNotSupportedException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleConversionNotSupported() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleConversionNotSupported(new ConversionNotSupportedException("test", this.getClass(), null), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Unable to Convert data element", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleTypeMismatch(org.springframework.beans.TypeMismatchException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleTypeMismatch() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleTypeMismatch(new TypeMismatchException("test", this.getClass(), null), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Data Type Mismatch", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleMissingServletRequestPart(org.springframework.web.multipart.support.MissingServletRequestPartException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleMissingServletRequestPart() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleMissingServletRequestPart(new MissingServletRequestPartException("emailAddress"), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("multipart/form-data error", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleBindException(org.springframework.validation.BindException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleBindException() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleBindException(new BindException("this", "that"), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Unable to Support a Binding Result", ((EndpointResponse)resp.getBody()).getMessage());
		
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleAsyncRequestTimeoutException(org.springframework.web.context.request.async.AsyncRequestTimeoutException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)}.
	 */
	@Test
	void testHandleAsyncRequestTimeoutException() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleAsyncRequestTimeoutException(new AsyncRequestTimeoutException(), null, HttpStatus.BAD_REQUEST, null);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Request Timed Out", ((EndpointResponse)resp.getBody()).getMessage());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.RestExceptionHandler#handleSecurityAuthorizationException(com.siliconmtn.io.api.security.SecurityAuthorizationException)}.
	 */
	@Test
	void testHandleSecurityAuthorizationException() throws Exception {
		RestExceptionHandler  rest = new RestExceptionHandler();
		ResponseEntity<Object> resp = rest.handleSecurityAuthorizationException(new SecurityAuthorizationException("test"));
		assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
		assertEquals("test", ((EndpointResponse)resp.getBody()).getMessage());
	}

}
