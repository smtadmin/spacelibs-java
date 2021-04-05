package com.siliconmtn.io.api.security;

// Jee 7.x
import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Spacelibs 1.x
import com.siliconmtn.io.ServletOutputStreamMock;
import com.siliconmtn.io.http.HttpHeaders;

/****************************************************************************
 * <b>Title:</b> SessionHijackFilterTest.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> Tests the SessionHijackFilter class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 30, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
class SessionHijackFilterTest {
	// Members
	private SessionHijackFilter shj;
	private HttpServletRequest req;
	private HttpServletResponse response;
	private FilterChain chain;
	private HttpSession session; 
	
	@BeforeEach
	void init() {
		shj = new SessionHijackFilter();
        //ServletRequest req = mock(ServletRequest.class);
        req = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilter() throws Exception {
        
        when(req.getSession()).thenReturn(session);
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
        when(session.isNew()).thenReturn(true);
        
		assertDoesNotThrow(()-> shj.doFilter(req, response, chain));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilterExisting() throws Exception {
        
        when(req.getSession()).thenReturn(session);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
		when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn("MyBrowser");
        when(session.isNew()).thenReturn(false);
        
		assertDoesNotThrow(()-> shj.doFilter(req, response, chain));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilterNoUserIP() throws Exception {
        
        when(req.getSession()).thenReturn(session);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("127.0.0.1");
		when(req.getHeader(HttpHeaders.X_FORWARDED_FOR)).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
		when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn("MyBrowser");
        when(session.isNew()).thenReturn(false);
        
		assertDoesNotThrow(()-> shj.doFilter(req, response, chain));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilterNoUserIPNew() throws Exception {
        
        when(req.getSession()).thenReturn(session);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("127.0.0.1");
		when(req.getHeader(HttpHeaders.X_FORWARDED_FOR)).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
		when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn("MyBrowser");
        when(session.isNew()).thenReturn(true);
        
		assertDoesNotThrow(()-> shj.doFilter(req, response, chain));
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilterExistingMismatchIP() throws Exception {
        ServletOutputStream sos = new ServletOutputStreamMock();
        when(req.getSession()).thenReturn(session);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("128.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
		when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn("MyBrowser");
        when(session.isNew()).thenReturn(false);
        when(response.getOutputStream()).thenReturn(sos);
        shj.doFilter(req, response, chain);
        System.out.println("SOS: " + sos.toString());
        assertTrue(sos.toString().indexOf("IP Address Changed") > -1);
	}

	
	/**
	 * Test method for {@link com.siliconmtn.io.api.security.SessionHijackFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
	 */
	@Test
	void testDoFilterExistingMismatchUA() throws Exception {
		ServletOutputStream sos = new ServletOutputStreamMock();
        when(req.getSession()).thenReturn(session);
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_IP_ADDRESS)).thenReturn("127.0.0.1");
		when(session.getAttribute(SessionHijackFilter.USER_AGENT)).thenReturn("MyBrowser");
		when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn("YourBrowser");
        when(session.isNew()).thenReturn(false);
        when(response.getOutputStream()).thenReturn(sos);
        shj.doFilter(req, response, chain);
        
        assertTrue(sos.toString().indexOf("User Agent Changed") > -1);
	}
}
