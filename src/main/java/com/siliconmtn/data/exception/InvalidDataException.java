package com.siliconmtn.data.exception;

/**
 * <b>Title:</b> InvalidDataException.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Generic Exception for handling data related exceptions
 *
 * <b>Copyright:</b> 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Sep 10, 2022
 * @updates
 *
 */
public class InvalidDataException extends Exception {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 4697304800735763541L;

	/** Creates new InvalidDataException */
    public InvalidDataException() {
        super();
    }

    /** Creates new InvalidDataException
     *  @param msg msg to display or localize
     *  @param cause original error object */
    public InvalidDataException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /** Creates new InvalidDataExceptionon
     *  @param key Pass in just the error msg*/
    public InvalidDataException(String key) {
        super(key);
    }

    /** Creates new InvalidDataException
     *  @param cause Pass in just the error object */
    public InvalidDataException(Throwable cause) {
        super(cause);
    }
}
