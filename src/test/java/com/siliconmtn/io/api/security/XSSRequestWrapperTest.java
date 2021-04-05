package com.siliconmtn.io.api.security;

// JEE 7
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

//Junit 5
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;

// Spring 5.5.x
import org.springframework.mock.web.MockHttpServletRequest;

// JDK 11.x
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: XSSRequestWrapperTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Unit test for the XSS wrapper
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 9, 2021
 * @updates:
 ****************************************************************************/
class XSSRequestWrapperTest {

	HttpServletRequest request;
	
	// Members
	XSSRequestWrapper wrappedRequest;
	
	@BeforeEach
	void setup() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
		wrappedRequest = new XSSRequestWrapper(request);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#XSSRequestWrapper(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	void testXSSRequestWrapper() throws Exception {
		assertNotNull(wrappedRequest);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#stripXSS(java.lang.String)}.
	 */
	@Test
	void testStripXSSEmpty() throws Exception {
		assertNull(XSSRequestWrapper.stripXSS(null));
		assertEquals("", XSSRequestWrapper.stripXSS(""));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#stripXSS(java.lang.String)}.
	 */
	@Test
	void testStripXSSNoXss() throws Exception {
		assertEquals("hello world", XSSRequestWrapper.stripXSS("hello world"));
		assertEquals("12345", XSSRequestWrapper.stripXSS("12345"));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#stripXSS(java.lang.String)}.
	 */
	@Test
	void testStripXSS() throws Exception {
		assertEquals("hello world", XSSRequestWrapper.stripXSS("<b>hello world</b>"));
		assertEquals("", XSSRequestWrapper.stripXSS("<script>function hello() {} </script>"));
	}
	
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#stripXSS(java.lang.String)}.
	 */
	@Test
	void testStripJson() throws Exception {
		String val = "{'com.smt.ezform.person.permission.EzformPermissionController.updateEzformPerson':'ezformUserParser','com.smt.ezform.response.ResponseController.submitResponse':'questionResponseParser'}";
		assertEquals(val, XSSRequestWrapper.stripXSS(val));
	}


	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#resetInputStream(byte[])}.
	 */
	@Test
	void testResetInputStream() throws Exception {
		wrappedRequest.resetInputStream("hello world".getBytes());
		
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while((c = wrappedRequest.servletStream.read()) > -1) {
			sb.append((char)c);
		}
		
		assertEquals("hello world", sb.toString());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getInputStream()}.
	 */
	@Test
	void testGetInputStreamRaw() throws Exception {
		wrappedRequest.resetInputStream("hello world".getBytes());
		ServletInputStream sis = wrappedRequest.getInputStream();
		
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while((c = sis.read()) > -1) {
			sb.append((char)c);
		}
		
		assertEquals("hello world", sb.toString());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getInputStream()}.
	 */
	@Test
	void testGetReader() throws Exception {
		BufferedReader br = new BufferedReader(new StringReader("hello world"));
		when(request.getReader()).thenReturn(br);
		Reader rdr = wrappedRequest.getReader();
		
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while((c = rdr.read()) > -1) {
			sb.append((char)c);
		}
		
		assertEquals("hello world", sb.toString());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getInputStream()}.
	 */
	@Test
	void testGetReaderRaw() throws Exception {
		wrappedRequest.resetInputStream("hello world".getBytes());
		Reader rdr = wrappedRequest.getReader();
		
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while((c = rdr.read()) > -1) {
			sb.append((char)c);
		}
		
		assertEquals("hello world", sb.toString());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getReader()}.
	 */
	@Test
	void testGetInputStream() throws Exception {
		BufferedReader br = new BufferedReader(new StringReader("hello world"));
		when(request.getReader()).thenReturn(br);
		ServletInputStream sis = wrappedRequest.getInputStream();
		sis.setReadListener(null);
		
		// Check some of the other overridden methods
		assertFalse(sis.isFinished());
		assertFalse(sis.isReady());
		
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while((c = sis.read()) > -1) {
			sb.append((char)c);
		}
		
		assertEquals("hello world", sb.toString());
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getParameterValues(java.lang.String)}.
	 */
	@Test
	void testGetParameterValues() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter("hello", "world");
		
		XSSRequestWrapper wrapper = new XSSRequestWrapper(req);
		assertEquals("world", wrapper.getParameterValues("hello")[0]);
		assertEquals(0, wrapper.getParameterValues("missing").length);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getParameter(java.lang.String)}.
	 */
	@Test
	void testGetParameter() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter("hello", "world");
		req.addParameter("script", "<script>function hello() {} </script>");
		XSSRequestWrapper wrapper = new XSSRequestWrapper(req);
		assertEquals("world", wrapper.getParameter("hello"));
		assertEquals("", wrapper.getParameter("script"));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getHeader(java.lang.String)}.
	 */
	@Test
	void testGetHeader() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader("User-Agent", "Firefox");
		req.addHeader("Accept-Language", "<script>function hello() {} </script>es");
		XSSRequestWrapper wrapper = new XSSRequestWrapper(req);
		
		assertEquals("es", wrapper.getHeader("Accept-Language"));
		assertEquals("Firefox", wrapper.getHeader("User-Agent"));
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.XSSRequestWrapper#getHeaders(java.lang.String)}.
	 */
	@Test
	void testGetHeaders() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader("Accept-Language", "en");
		req.addHeader("Accept-Language", "<script>function hello() {} </script>es");
		XSSRequestWrapper wrapper = new XSSRequestWrapper(req);
		Enumeration<String> e = wrapper.getHeaders("Accept-Language");
		
		List<String> values = Arrays.asList(new String[]{"en", "es"});
		int ctr = 0;
		while(e.hasMoreElements()) {
			String val = e.nextElement();
			assertNotNull(val);
			assertTrue(values.contains(val));
			ctr++;
		}
		
		assertEquals(2, ctr);
	}

}
