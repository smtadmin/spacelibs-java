package com.siliconmtn.io.api.validation;

// JDK 11.x
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// JEE 7.x
import javax.servlet.http.HttpServletRequest;

// Spring 5.5.x
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// Spacelibs 1.0
import com.siliconmtn.io.api.EndpointRequestException;
import com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey;
import com.siliconmtn.io.api.validation.factory.ParserFactory;
import com.siliconmtn.io.api.validation.factory.ParserIntfc;
import com.siliconmtn.io.api.validation.validator.ValidationDTO;

/****************************************************************************
 * <b>Title</b>: ValidateAop.java
 * <b>Project</b>: Spacelibs-java
 * <b>Description: </b> Aspect that validates the data passed into the server
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 4, 2021
 * @updates:
 ****************************************************************************/
@Aspect
@Component
public class ValidateAop {

	@Autowired
	HttpServletRequest request;

	@Autowired
	ParserFactory pFact;

	/**
	 * This is the method which I would like to execute before a selected method
	 * execution.
	 * 
	 * @param pjp  Join Point class which connects this Aspect to the annotation
	 *             method
	 * @param body The information that is going to be validated
	 * @throws EndpointRequestException Thrown when data validation fails
	 */
	@Before("@annotation(com.siliconmtn.io.api.validation.Validate) && args(.., @PathVariable pathVar, @RequestBody body)")
	public void beforeAdvice(JoinPoint pjp, Object body, Object pathVar) throws EndpointRequestException {
		Method m = MethodSignature.class.cast(pjp.getSignature()).getMethod();
		Validate validate = m.getAnnotation(Validate.class);

		if (validate != null) {
			Map<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);
			attributes.put(AttributeKey.PATH_VAR, pathVar);

			List<ValidationErrorDTO> errors = validateReponse(body, attributes,
					m.getDeclaringClass().getName() + "." + m.getName());
			if (! errors.isEmpty()) {
				throw new EndpointRequestException("Failed to validate request", errors);
			}
		}

	}

	/**
	 * Load the packager associated with the supplied class and method, package the
	 * response body into ValidationDTOs and validate them to ensure that there are
	 * no issues with the data.
	 * 
	 * @param attributes The information that is going to be validated
	 * @param attributes map of attributes from endpoint other than the body
	 * @param key        The key that identifies the packager that needs to be
	 *                   created.
	 * @throws EndpointRequestException ParserDispatcher failed to create a parser
	 *                                  or parse the supplied data in some way.
	 */
	private List<ValidationErrorDTO> validateReponse(Object body, Map<AttributeKey, Object> attributes, String key)
			throws EndpointRequestException {

		List<ValidationDTO> fields;
		try {
			ParserIntfc parser = pFact.parserDispatcher(key, attributes);

			if (parser == null)
				return Collections.emptyList();

			fields = parser.requestParser(body);
		} catch (Exception e) {
			throw new EndpointRequestException("Data validation preperation failed.", e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ValidationUtil.validateData(fields);
	}

}
