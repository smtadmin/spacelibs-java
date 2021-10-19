package com.siliconmtn.io.api.base;

// JDK 11.x
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/****************************************************************************
 * <b>Title:</b> TransactionInjector.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Empty annotation attached to the base service (or others) that
 * can be connected to the appropriate aspect to pre/post process a method
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Apr 27, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public @interface TransactionInjector {
	
	/**
	 * Enum for the action types
	 */
	public enum ActionType {
		CLONE, CREATE, DELETE, DELETE_ALL, FIND, REORDER, SAVE, SAVE_ALL
	}

	/**
	 * Assigns the action type for the transaction
	 * @return
	 */
	public ActionType type();
}
