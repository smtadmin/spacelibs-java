package com.siliconmtn.io.api.validation.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.siliconmtn.io.api.validation.validator.ValidationDTO;


/****************************************************************************
 * <b>Title</b>: ParserFactoryTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Test the parser factory
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 3.0
 * @since Mar 16, 2021
 * @updates:
 ****************************************************************************/

class ParserFactoryTest {
	
	
	/**
	 * Test the parser factory to ensure it functions as desired
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testParserFactory() throws Exception {
		ParserFactory fact = new ParserFactory();
		
		Map<String, String> builderMapper = mock(HashMap.class);
		when(builderMapper.get("test")).thenReturn("");
		when(builderMapper.get("com.fake.class.fakeMethod")).thenReturn("nothing");
		when(builderMapper.get("com.fake.class.otherFake")).thenReturn("com.siliconmtn.io.api.validation.TestParser");
		
		ReflectionTestUtils.setField(fact, "builderMapper", builderMapper);

		assertNull(fact.parserDispatcher(null));
		assertNull(fact.parserDispatcher("test"));
		
		ParserIntfc parser = fact.parserDispatcher("com.fake.class.otherFake");
		
		List<ValidationDTO> fields = parser.requestParser("Test");
		
		assertEquals(1, fields.size());
		assertEquals("Test", fields.get(0).getValue());
		assertEquals("id", fields.get(0).getElementId());
		assertTrue(fields.get(0).isRequired());
		
		try {
			fact.parserDispatcher("com.fake.class.fakeMethod");
		} catch(Exception e) {
			assert(e.getMessage().equals("Failed to create data parser"));
		}
		
	}

}
