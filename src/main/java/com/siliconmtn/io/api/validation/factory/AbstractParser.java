package com.siliconmtn.io.api.validation.factory;

// JDK 11.x
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// Spacelibs java
import com.siliconmtn.io.api.validation.validator.ValidationDTO;
import com.siliconmtn.io.api.validation.validator.ValidatorIntfc.ValidatorType;

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
	
	/**
	 * Method to add to validation dto to lists
	 * @param validationDTOList the list of ValidationDTO
	 * @param validationDTO the Validation DTO object
	 */
	public void addDTOToList(List<ValidationDTO> validationDTOList, ValidationDTO validationDTO) {
		validationDTOList.add(validationDTO);
	}
	
	/**
	 * Method to build List of Validation DTO 
	 * @param validationDTOList The List of Validation DTO
	 * @param value The value of response
	 * @param type The ValidatorType of value
	 * @param max The max range of value
	 * @param min The min range of value
	 */
	public ValidationDTO build(ValidatorType type, String value,
			Map<String, String> validOptions, boolean alternate, String max, String min) {
		return ValidationDTO
		.builder()
		.value(value)
		.type(type)
		.validOptions(validOptions)
		.alternateValidationId(alternate)
		.min(min)
		.max(max)
		.build();
		
	}

}
