package com.siliconmtn.io.api.validation;

// Jdk 11.x
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/****************************************************************************
 * <b>Title</b>: Validate.java
 * <b>Project</b>: planit-api
 * <b>Description: </b> Annotation to be applied to an endpoint that needs to 
 * have its data validated.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 4, 2021
 * @updates:
 ****************************************************************************/
public @interface Validate {
	
}
