package com.siliconmtn.data.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Title:</b> EntityIgnore.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Used to tell EntityUtil to ignore a field for toEntity conversion.
 *
 * <b>Copyright:</b> 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Sep 16, 2022
 * @updates
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityIgnore {

}
