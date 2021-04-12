package com.siliconmtn.io.api.validation.factory;

import java.io.IOException;
// JDK 11.x
import java.util.List;
import java.util.Map;

// Jackson JSON-processor
import com.fasterxml.jackson.core.JsonProcessingException;

// Spacelibs
import com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey;
import com.siliconmtn.io.api.validation.validator.ValidationDTO;

/****************************************************************************
 * <b>Title</b>: RequestBodyParser.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> This is the main interface for all custom parsers.  These 
 * parsers review the incoming data and assign various data attributes to each.
 * This collection of meta data is utilized to validate each data element 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 3.0
 * @since Mar 10, 2021
 * @updates:
 ****************************************************************************/
public interface ParserIntfc {
	
	/**
	 * Retrieves all of the attributes in the map
	 * @return Map with all attributes
	 */
	public Map<AttributeKey, Object> getAttributes();
	
	/**
	 * Retrieves a single value in the attribute map
	 * @param key Map key to retrieve the value
	 * @return Value assigned to the key. Null if not found  
	 */
	public Object getAttribute(AttributeKey key);
	
	/**
	 * Adds a single entry to the map
	 * @param key Key of the KV to add
	 * @param value The value of the KV to add
	 */
	public void addAttribute(AttributeKey key, Object value);
	
	/**
	 * Adds a single entry to the map
	 * @param key Key of the KV to add
	 * @param value The value of the KV to add
	 */
	public void setAttributes(Map<AttributeKey, Object> data);
	
	/**
	 * This method creates a collection of Fields to be validated by the 
	 * validation engine
	 * @param dataElement Data element retrieved from the end-point.  This can be in
	 * a variety of data formats
	 * @return Collection of fields to be validated.  Empty List if none
	 * @throws JsonProcessingException Throws parsing error if data can't be processed
	 */
	List<ValidationDTO> requestParser(Object dataElement) throws IOException;
}
