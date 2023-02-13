package com.siliconmtn.data.event;

import java.util.Collection;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/****************************************************************************
 * <b>Title:</b> PublisherEvent.java <br>
 * <b>Project:</b> spacelibs-java <br>
 * <b>Description:</b> Wrapper for sending data through the Spring Eventing System.<br>
 * <b>Copyright:</b> Copyright (c) 2023 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Billy Larsen
 * @version 1.x
 * @since Jan 13, 2023
 *        <b>updates:</b>
 * 
 ****************************************************************************/
@Getter
@Setter
@ToString
public class PublisherEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private boolean isSuccessful;
	private boolean isTimeout;
	private Collection<T> payload;

	public PublisherEvent(Object source, boolean isSuccessful, boolean isTimeout, Collection<T> payload) {
		super(source);
		this.isSuccessful = isSuccessful;
		this.isTimeout = isTimeout;
		this.payload = payload;
	}	
}
