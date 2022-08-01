package com.siliconmtn.security.encryption;

// JDK 11.x
import java.io.IOException;

/****************************************************************************
 * <b>Title:</b> EncryptionException.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Exception to handle encryption related issues.  Extends 
 * from IOException.  
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 14, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class EncryptionException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4118740006065604389L;

	/**
	 * Default no args constructor
	 */
	public EncryptionException() {
		super();
	}

	/**
	 * Initializes the exception with the error message
	 * @param message
	 */
	public EncryptionException(String message) {
		super(message);
	}

	/**
	 * Initializes the exception with the error cause
	 * @param cause
	 */
	public EncryptionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Initializes the exception with the error message and cause
	 * @param message
	 * @param cause
	 */
	public EncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
