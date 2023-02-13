package com.siliconmtn.data.event;

import org.springframework.context.event.EventListener;

public interface PublisherEventListener<T> {

	@EventListener(condition = "#event.isSuccessful && #event.isTimeout == false")
	public void successListener(PublisherEvent<T> event);

	@EventListener(condition = "#event.isTimeout")
	public void timeoutListener(PublisherEvent<T> event);

	@EventListener(condition = "#event.isTimeout == false && #event.isSuccessful == false")
	public void invalidListener(PublisherEvent<T> event);
}
