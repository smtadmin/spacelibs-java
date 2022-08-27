package com.siliconmtn.io;

// JDK 11.x
import java.io.Serializable;
import java.time.Instant;

// Space Libs 1.x
import com.siliconmtn.data.text.StringUtil;

// Lombox 1.x
import lombok.Data;

/****************************************************************************
 * <b>Title</b>: BaseFile.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Data structure/Value Object to hold file information
 * <b>Copyright:</b> Copyright (c) 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Aug 26, 2022
 * @updates:
 ****************************************************************************/
@Data
public class BaseFile implements Serializable {
	// SVersion UID
	private static final long serialVersionUID = 8099461843224087397L;
	
	// Members
	private String id;
	private String path;
	private String name;
	private String ext;
	private long size;
	private String owner;
	private String ownerId;
	private Instant updateDate;
	private byte[] data;
	
	/**
	 * Parses the path and file names into the various values
	 * @param canonicalName Full path and file name with extension
	 */
	public void assignFileInfo(String canonicalName) {
		// If no data is present, return
		if (StringUtil.isEmpty(canonicalName)) return;
		
		// Assign the full file path
		path = canonicalName;
		
		// Parse out the file name
		int index = path.lastIndexOf("/") > -1 ? path.lastIndexOf("/") + 1: 0;
		name = path.substring(index);
		
		// Get the extension
		index = path.lastIndexOf(".");
		if (index > 0) ext = path.substring(index+1);
	}
}
