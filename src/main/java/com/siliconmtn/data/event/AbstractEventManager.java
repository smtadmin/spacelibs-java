package com.siliconmtn.data.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import javax.validation.constraints.NotNull;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.Getter;

/****************************************************************************
 * <b>Title:</b> AbstractEventManager.java <br>
 * <b>Project:</b> spacelibs-java <br>
 * <b>Description:</b> Abstract handling all the base implementation of the interface.
 * Handles setup, reset and teardown of timers and integrates checks for event sending<br>
 * <b>Copyright:</b> Copyright (c) 2023 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Billy Larsen
 * @version 1.x
 * @since Jan 13, 2023
 *        <b>updates:</b>
 * 
 ***************************************************************************
 */
@Getter
@Component
public abstract class AbstractEventManager<T extends Comparable<T>> implements EventManagerIntfc<T>{

	//Internal SpringBoot Eventing System that is leveraged for Observables. 
    protected ApplicationEventPublisher applicationEventPublisher;

    //Holds the Data for validation/processing
    protected Collection<T> data; 

    //Timeout duration in milliseconds.  -1 is disabled.
	protected long timeoutInMilliseconds = -1;

	//Holds the Timer Object for internal usage.
	protected Timer timer;

	//Default Constructor
	protected AbstractEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher) {
		this(applicationEventPublisher, null, -1);
	}

	//Default Constructor with predefined collection framework.
	protected AbstractEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher, @NotNull Collection<T> data) {
		this(applicationEventPublisher, data, -1);
	}

	//Default Constructor with timeout support.
	protected AbstractEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher, long timeoutInMilliseconds) {
		this(applicationEventPublisher, null, timeoutInMilliseconds);
	}

	//Root All Args Constructor for setting collection and timeout.
	protected AbstractEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher, Collection<T> data, long timeoutInMilliseconds) {
		if(applicationEventPublisher == null) {
			throw new IllegalArgumentException("ApplicationEventPublisher cannot be null.");
		}
		this.applicationEventPublisher = applicationEventPublisher;

		if(data == null) {
			this.data = new ArrayList<>();	
		} else {
			this.data = data;
		}

		this.timeoutInMilliseconds = timeoutInMilliseconds;
	}

	@Override
	public Collection<T> getData() {
		return data;
	}

	/**
	 * Adds the given element to the collection, resets the timeout timer and attempts to run validation on the collection.
	 */
	@Override
	public void addData(T element) {
		if(element != null) {
			data.add(element);
			setTimer();
			if(validateData()) {
				sendEvent(true, false);
			}
		}
	}

	/**
	 * Update the timeout and reset timer.
	 */
	@Override
	public void setTimeout(long timeoutInMilliseconds) {
		this.timeoutInMilliseconds = timeoutInMilliseconds;
		setTimer();
	}

	/**
	 * If the data exists in collection, removes it and resets timer.
	 */
	@Override
	public void removeData(T element) {
		if(data.contains(element)) {
			data.remove(element);
			setTimer();
		}
	}

	/**
	 * Returns a TimerTask implementation that by default sends a timeout fail
	 * event.
	 */
	@Override
	public TimerTask getTimeoutTask() {
		return new TimerTask() {
			@Override
			public void run() {
				sendEvent(false, true);
			}
		};
	}

	/**
	 * Cancels any running timer and sends an event with success and timeout
	 * status through the event framework.
	 */
	@Override
	public void sendEvent(boolean isSuccessful, boolean isTimeout) {
		timer.cancel();
		applicationEventPublisher.publishEvent(new PublisherEvent<>(this, isSuccessful, isTimeout, getData()));
	}

	/**
	 * Internal function for managing lifecycle of the timer.
	 */
	protected void setTimer() {
		if(timer != null) {
			timer.cancel();
		}

		timer = new Timer();
		if(this.timeoutInMilliseconds > 0) {
			this.timer.schedule(getTimeoutTask(), timeoutInMilliseconds);
		}
	}
}
