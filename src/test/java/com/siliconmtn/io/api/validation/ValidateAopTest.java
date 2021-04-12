package com.siliconmtn.io.api.validation;

//Junit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Spacelibs 1.x
import com.siliconmtn.io.api.EndpointRequestException;
import com.siliconmtn.io.api.validation.factory.AbstractParser;
import com.siliconmtn.io.api.validation.factory.ParserFactory;
import com.siliconmtn.io.api.validation.validator.ValidationDTO;

// JDK 11.x
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// AspectJ
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/****************************************************************************
 * <b>Title</b>: ValidateAopTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Test the ValidateAop
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 16, 2021
 * updates:
 * March 18 - James Camire - Rewrote the test to accomodate for API changes
 ****************************************************************************/

class ValidateAopTest {
	
	// Mocked Members
	ParserFactory pFact;
	ValidateAop va;
	Method myMethod;
	JoinPoint joinPoint;
	MethodSignature signature;
	
	/**
	 * Runs before each unit test
	 */
	@BeforeEach
	void initBeforeMethods() throws Exception {
		va = new ValidateAop();
		
		// Setup the mocks
		pFact = mock(ParserFactory.class);
		va.pFact = pFact;
		
		// mocks
		myMethod = ValidateAopTest.class.getDeclaredMethod("myTestMethod");
		joinPoint = mock(JoinPoint.class);
		signature = mock(MethodSignature.class);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testValidateAop() throws Exception {
		ValidateAop va = new ValidateAop();
		assertNotNull(va);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testBeforeAdvice() throws Exception {
		when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod);
		when(pFact.parserDispatcher(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

		assertDoesNotThrow(() -> va.beforeAdvice(joinPoint, "body", "PathVar"));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testBeforeAdviceBaseParser() throws Exception {
		when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod);
		when(pFact.parserDispatcher(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new MyTestParser());

		assertDoesNotThrow(() -> va.beforeAdvice(joinPoint, "body", "PathVar"));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testBeforeAdviceErrorParser() throws Exception {
		when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod);
		when(pFact.parserDispatcher(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new MyTestParserError());

		assertThrows(EndpointRequestException.class, () -> {
			va.beforeAdvice(joinPoint, "body", "PathVar");
		});
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testBeforeAdviceErrorParserRequest() throws Exception {
		when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod);
		when(pFact.parserDispatcher(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new MyTestParserThrow());

		assertThrows(EndpointRequestException.class, () -> {
			va.beforeAdvice(joinPoint, "body", "PathVar");
		});
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.ValidateAop#beforeAdvice(org.aspectj.lang.JoinPoint, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	void testBeforeAdviceNoValidate() throws Exception {
		myMethod = ValidateAopTest.class.getDeclaredMethod("noValidateMethod");
		when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(myMethod);
		assertDoesNotThrow(() -> va.beforeAdvice(joinPoint, "body", "PathVar"));
	}
	
	/**
	 * Dummy method to test with an annotation
	 */
	@Validate
	public void myTestMethod() { /* Nothing to do */ }
	
	/**
	 * Dummy method to test with no annotation
	 */
	public void noValidateMethod() { /* Nothing to do */ }

}

/**
 * Empty parser to return with no errors
 */
class MyTestParser extends AbstractParser { /* Nothing to do */ }

/**
 * Error parser to return with multiple errors
 */
class MyTestParserError extends AbstractParser { 
	public List<ValidationDTO> requestParser(Object obj) {
		List<ValidationDTO> errors = new ArrayList<>();
		errors.add(ValidationDTO.builder().build());
		errors.add(ValidationDTO.builder().build());
		return errors;
	}
}

/**
 * Exception parser that throws an exception when called
 */
class MyTestParserThrow extends AbstractParser { 
	public List<ValidationDTO> requestParser(Object obj) 
	throws IOException {
		if (obj != null) throw new IOException();
		return null;
	}
}
