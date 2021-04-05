package com.siliconmtn.data.parser;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

// JDK 11.x
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

// SPace Libs 1.x
import com.siliconmtn.data.format.DateFormat.DatePattern;

/****************************************************************************
 * <b>Title</b>: BeanDataMapperTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> CHANGE ME!!
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 28, 2021
 * @updates:
 ****************************************************************************/
class BeanDataMapperTest {
	
	// Members
	private TestDataBeanVO dbvo;
	private Map<String, String[]> map;
	private HttpServletRequest req;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		req = Mockito.mock(HttpServletRequest.class);
		map = new HashMap<>();
		map.put("name", new String[] {"SMT"});
		map.put("someNumber", new String[] {"1"});
		map.put("currDate", new String[] {"2020-01-01"});
		map.put("datePattern", new String[] {DatePattern.DATE_DASH.name()});
		map.put("arrNames", new String[] {"one", "two", "three"});
		map.put("names", new String[] {"James", "Mary", "Stef"});
		map.put("names_test", new String[] {"Sue", "Cindy", "Quianna", "Kate"});
		
		when(req.getParameterMap()).thenReturn(map);
		
		dbvo = new TestDataBeanVO();
		dbvo.setCurrDate(new Date());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.parser.BeanDataMapper#parseBean(java.lang.Object, java.util.Map)}.
	 */
	@Test
	void testParseBeanObjectMapOfStringString() {
		BeanDataMapper.parseBean(dbvo, map);
		assertEquals("SMT", dbvo.getName());
		assertEquals(3, dbvo.getArrNames().length);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.parser.BeanDataMapper#parseBean(java.lang.Object, java.util.Map)}.
	 */
	@Test
	void testParseBeanObjectMapOfStringStringNull() {
		map.put("name", null);
		BeanDataMapper.parseBean(dbvo, map);
		assertNull(dbvo.getName());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.parser.BeanDataMapper#parseBean(java.lang.Object, java.util.Map, java.lang.String)}.
	 */
	@Test
	void testParseBeanObjectMapOfStringStringString() {
		
		ConvertUtilsBean obj = Mockito.mock(ConvertUtilsBean.class);
		when(obj.convert(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
		.thenThrow(new ConversionException(""));
		Mockito.spy(obj);
		List<Object> data = BeanDataMapper.createList(String.class, new Object[] {"one", "two"});
		assertEquals(2, data.size());
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	void testConstructors() {
		dbvo = new TestDataBeanVO(req);
		assertEquals("SMT", dbvo.getName());
		
		req = Mockito.mock(HttpServletRequest.class);
		Map<String, String[]> testMap = new HashMap<>();
		testMap.put("name_test", new String[] { "Suffix Test"});
		when(req.getParameterMap()).thenReturn(testMap);
		dbvo = new TestDataBeanVO(req, "_test");
		assertEquals("Suffix Test", dbvo.getName());
	}

}
