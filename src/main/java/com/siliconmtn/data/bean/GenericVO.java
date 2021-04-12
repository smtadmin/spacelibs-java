package com.siliconmtn.data.bean;

// JDK 11.x
import java.io.Serializable;

import com.siliconmtn.core.HashCodeUtil;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: GenericVO.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Simple Key/Value bean to hold a key and value
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
public class GenericVO implements Serializable, Comparable<GenericVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Serializable key = null;
	private Serializable value = null;
	
	/**
	 * 
	 */
	public GenericVO() {
		super();
	}

	
	/**
	 * Assigns key and value to the bean
	 * @param key Key for this bean
	 * @param value Stored value for the associated key
	 */
	public GenericVO(Serializable key, Serializable value) {
		this();
		this.key = key;
		this.value = value;
	}

	/*
	 * (non-javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return StringUtil.getToString(this);
	}

	/**
	 * @return the key
	 */
	public Serializable getKey() {
		return key;
	}

	/**
	 * Sets the key object (Must be serializable)
	 * @param key the key to set
	 */
	public void setKey(Serializable key) {
		this.key = key;
	}

	/**
	 * Gets the value object
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value object (Must be serializable)
	 * @param value the value to set
	 */
	public void setValue(Serializable value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GenericVO val) {
		if (val == null || val.getKey() == null) return 0;
		
		if (key instanceof java.lang.String) {
			return (((String)key).compareToIgnoreCase((String)val.getKey()));
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		GenericVO val = null;
		if (o instanceof GenericVO) val = (GenericVO)o;

		if (val != null && key instanceof java.lang.String) {
			return (((String)key).equalsIgnoreCase((String)val.getKey()));
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeUtil.hash(this.getKey());
	}
}
