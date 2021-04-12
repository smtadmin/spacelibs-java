package com.siliconmtn.io.api.validation.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey;

/****************************************************************************
 * <b>Title:</b> AbstractParserTest.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Tests the abstract parser class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 18, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
class AbstractParserTest {
	
	// Members
	protected AbstractParser parser = new TestParser();
	protected Map<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#AbstractParser()}.
	 */
	@Test
	void testAbstractParser() throws Exception {
		assertNotNull(parser);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#getAttributes()}.
	 */
	@Test
	void testGetAttributes() throws Exception {
		// Default state
		assertEquals(0, parser.getAttributes().size());
		
		// Add attribute and test
		parser.addAttribute(AttributeKey.PATH_VAR, "SOME_PATH");
		assertEquals(1, parser.getAttributes().size());
		assertEquals("SOME_PATH", parser.getAttributes().get(AttributeKey.PATH_VAR));	
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#getAttribute(com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey)}.
	 */
	@Test
	void testGetAttribute() throws Exception {
		parser.addAttribute(AttributeKey.PATH_VAR, "SOME_PATH");
		assertEquals("SOME_PATH", parser.getAttribute(AttributeKey.PATH_VAR));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#getAttribute(com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey)}.
	 */
	@Test
	void testGetAttributeNull() throws Exception {
		parser.addAttribute(null, "SOME_PATH");
		assertNull(parser.getAttribute(null));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#setAttributes(java.util.Map)}.
	 */
	@Test
	void testSetAttributesNull() throws Exception {
		// Default state
		assertEquals(0, parser.getAttributes().size());
		
		// Check the null parser
		parser.setAttributes(null);
		assertEquals(0, parser.getAttributes().size());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#setAttributes(java.util.Map)}.
	 */
	@Test
	void testSetAttributes() throws Exception {
		// Add the attributes
		attributes.put(AttributeKey.PATH_VAR, "HELLO");
		parser.setAttributes(attributes);
		assertEquals(1, parser.getAttributes().size());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.validation.factory.AbstractParser#requestParser(java.lang.Object)}.
	 */
	@Test
	void testRequestParser() throws Exception {
		assertEquals(0, parser.requestParser(null).size());
	}

}

/**
 * Empty dummy class to test the abstract
 */
class TestParser extends AbstractParser {
	
}
