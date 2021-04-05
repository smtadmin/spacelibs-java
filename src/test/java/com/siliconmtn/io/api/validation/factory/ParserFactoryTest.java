package com.siliconmtn.io.api.validation.factory;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
// JDK 11.x
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

//Spacelibs 1.0
import com.siliconmtn.io.api.EndpointRequestException;
import com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey;
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
@ExtendWith(SpringExtension.class)
class ParserFactoryTest {
	
	@InjectMocks
	private ParserFactory parserFactory;
		
	/**
	 * Test the parser factory to ensure it functions as desired
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testParserFactory() throws Exception {
		ParserFactory fact = new ParserFactory();
		fact.autowireCapableBeanFactory = mock(AutowireCapableBeanFactory.class);
		Map<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);
		attributes.put(AttributeKey.PATH_VAR, "test");
		
		TestParser t = new TestParser();
		
		Map<String, String> builderMapper = mock(HashMap.class);
		ApplicationContext ctx = mock(ApplicationContext.class);
		when(builderMapper.get("test")).thenReturn("");
		when(builderMapper.get("com.fake.class.fakeMethod")).thenReturn("nothing");
		when(builderMapper.get("com.fake.class.otherFake")).thenReturn("testParser");
		when(ctx.getBean("testParser")).thenReturn(t);
		ReflectionTestUtils.setField(fact, "builderMapper", builderMapper);
		ReflectionTestUtils.setField(fact, "applicationContext", ctx);

		assertNull(fact.parserDispatcher(null, attributes));
		assertNull(fact.parserDispatcher("test", attributes));

		ParserIntfc parser = fact.parserDispatcher("com.fake.class.otherFake", attributes);

		List<ValidationDTO> fields = parser.requestParser("Test");
		
		assertEquals(0, fields.size());
		assertThrows(EndpointRequestException.class, () -> fact.parserDispatcher("com.fake.class.fakeMethod",attributes));
		
	}

}
