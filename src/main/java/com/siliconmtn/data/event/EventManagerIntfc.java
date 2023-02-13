package com.siliconmtn.data.event;

import java.util.Collection;
import java.util.TimerTask;

/**
 * <b>Title:</b> EventManagerIntfc.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Interface for managing related messaging in a controlled observable way.
 * This interface defines common high level interactions used for defining and feeding data in to
 * be managed.  This framework works using the Spring ApplicationEvent System for Alerting observing
 * code that some work is ready to be performed.
 *
 * <b>Copyright:</b> 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author raptor
 * @version 1.0
 * @since Nov 22, 2022
 * @updates
 *
 * @param <T>
 */
public interface EventManagerIntfc<T extends Comparable<T>> {

	/**
	 * Add data to collection
	 * @param element
	 */
	public void addData(T element);

	/**
	 * Remove data from collection
	 * @param element
	 */
	public void removeData(T element);

	/**
	 * Retrieve data in collection
	 * @return
	 */
	public Collection<T> getData();

	/**
	 * Validate status of collection.
	 * @return
	 */
	public boolean validateData();

	/**
	 * Send event through the Eventing System.
	 * @param valid
	 * @param timeout
	 */
	public void sendEvent(boolean valid, boolean timeout);

	/**
	 * Set the timeout in milliseconds before executing the related timeoutTask.
	 * @param timeoutInMilliseconds
	 */
	public void setTimeout(long timeoutInMilliseconds);

	/**
	 * Retrieve the timeout task for what happens on a timeout.
	 * @return
	 */
	public TimerTask getTimeoutTask();
}