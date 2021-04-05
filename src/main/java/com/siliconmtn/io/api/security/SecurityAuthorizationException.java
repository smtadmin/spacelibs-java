package com.siliconmtn.io.api.security;

/****************************************************************************
 * <b>Title:</b> SecurityAuthorizationException.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Exception class to handle cross site scripting and other 
 * non-standard requests that are trapped in the various security handlers
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 30, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class SecurityAuthorizationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4091063205734831130L;

	/**
	 * @param messageError Message of the exception
	 */
	public SecurityAuthorizationException(String message) {
		super(message);
	}

	/**
	 * @param cause The thrown error that was caught
	 */
	public SecurityAuthorizationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message Message of the exception
	 * @param cause The thrown error that was caught
	 */
	public SecurityAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
