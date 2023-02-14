package com.siliconmtn.data.event;

import org.springframework.context.event.EventListener;

/****************************************************************************
 * <b>Title:</b> PublisherEventListener.java <br>
 * <b>Project:</b> spacelibs-java <br>
 * <b>Description:</b> Interface for how to handle PublishEvent Messages.<br>
 * <b>Copyright:</b> Copyright (c) 2023 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Billy Larsen
 * @version 1.x
 * @since Feb 14, 2023
 *        <b>updates:</b>
 * 
 ***************************************************************************
 */
public interface PublisherEventListener<T> {

	@EventListener(condition = "#event.isSuccessful && #event.isTimeout == false")
	public void successListener(PublisherEvent<T> event);

	@EventListener(condition = "#event.isTimeout")
	public void timeoutListener(PublisherEvent<T> event);

	@EventListener(condition = "#event.isTimeout == false && #event.isSuccessful == false")
	public void invalidListener(PublisherEvent<T> event);
}
