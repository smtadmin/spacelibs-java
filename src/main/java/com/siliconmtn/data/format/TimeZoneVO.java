package com.siliconmtn.data.format;

import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: TimeZoneVO.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Manages the data for a single time zone
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
public class TimeZoneVO {
	
	private String id;
	private String name;
	private String description;
	private String isoCode;
	
	/**
	 * Default constructor
	 */
	public TimeZoneVO() {
		super();
	}
	
	/**
	 * Helper constructor to assign members
	 * @param id Timezone ID
	 * @param name Name of the timezone
	 * @param description Timezone description information
	 * @param isoCode Timezone ISO code
	 */
	public TimeZoneVO(String id, String name, String description, String isoCode) {
		this();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isoCode = isoCode;
	}

	/*
	 * (non-javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.getToString(this);
	}
	
	/**
	 * Gets the timezone ISO ID
	 * @return the timzone ISO id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the timezone name
	 * @return the name of the timezone
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the timezone decription
	 * @return the description of the timezone
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the timezone iso code
	 * @return the isoId
	 */
	public String getIsoCode() {
		return isoCode;
	}

	/**
	 * Sets the timezone ID
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the timezone name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the timezone description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the timezone ISO code
	 * @param isoCode the isoId to set
	 */
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

}
