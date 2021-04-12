package com.siliconmtn.io.api.validation.factory;

// JDK 11.x
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// Spacelibs java
import com.siliconmtn.io.api.validation.validator.ValidationDTO;

/****************************************************************************
 * <b>Title:</b> AbstractParser.java
 * <b>Project:</b> spacelibs-java
 * <b>Description: </b> This is the main abstract for all custom parsers.  These 
 * parsers review the incoming data and assign various data attributes to each.
 * This collection of meta data is utilized to validate each data element 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 18, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public abstract class AbstractParser implements ParserIntfc {
	
	/**
	 * This enum provides the list of keys utilized as the keys in the 
	 * attribute map
	 */
	public enum AttributeKey {
		PATH_VAR
	}
	
	/**
	 * Holds additional data attributes to perform the parser.
	 */
	private Map<AttributeKey, Object> attributes;
	
	/**
	 * Default constructor
	 */
	protected AbstractParser() {
		super();
		attributes = new EnumMap<>(AttributeKey.class);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.factory.ParserIntfc#getAttributes()
	 */
	@Override
	public Map<AttributeKey, Object> getAttributes() {
		return attributes;
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.factory.ParserIntfc#getAttribute(java.lang.AttributeKey)
	 */
	@Override
	public Object getAttribute(AttributeKey key) {
		return attributes.get(key);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.factory.ParserIntfc#addAttribute(java.lang.AttributeKey, java.lang.Object)
	 */
	@Override
	public void addAttribute(AttributeKey key, Object value) {
		if (key != null) attributes.put(key, value);
	}
	
	/*
	 * (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.factory.ParserIntfc#setAttributes(java.util.Map)
	 */
	@Override
	public void setAttributes(Map<AttributeKey, Object> data) {
		if (data != null) attributes.putAll(data);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.io.api.validation.factory.ParserIntfc#requestParser(java.lang.Object)
	 */
	@Override
	public List<ValidationDTO> requestParser(Object dataElement) throws IOException {
		return new ArrayList<>();
	}

}
