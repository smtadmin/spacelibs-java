package com.siliconmtn.io.api.security;

// JDK 11.x
import java.io.IOException;

// JEE 7.x
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Spring 5.x
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// Spacelibs 1.x
import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.api.EndpointResponse;
import com.siliconmtn.io.http.HttpHeaders;

/****************************************************************************
 * <b>Title</b>: UserHijackFilter.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Checks the user against the current session to ensure that
 * the user's session has not been hijacked.  This class utilizes the user's IP address
 * and the user's browser type (User Agent) to determine if there is a potential 
 * hijacking of the user's session.  If there is a mismatch, the session is 
 * invalidated and the user's request is intercepted with an error thrown
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 1.0
 * @since Mar 30, 2021
 * @updates:
 ****************************************************************************/
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SessionHijackFilter implements Filter {
	
	/**
	 * Key for the user's IP address in session
	 */
	public static final String USER_IP_ADDRESS = "userIpAddress";
	
	/**
	 * Key for the user's IP address in session
	 */
	public static final String USER_AGENT = "userAgent";

	/*
	 * (non-javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
    	
    	// Cast the request and get the session
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        HttpServletResponse response = (HttpServletResponse)res;
        
        // Determine if the session is new and either add to session or compare
        if (session.isNew()) {
        	assignLocationToSession(request, session);
        	
        	// Pass along to next process in chain
            chain.doFilter(request, response);
        } else {
        	try {
        		validateUserInfo(request, session);
        		
            	// Pass along to next process in chain
                chain.doFilter(request, response);
        	} catch (Exception e) {
        		EndpointResponse epres = new EndpointResponse(HttpStatus.FORBIDDEN);
        		epres.setMessage(e.getMessage());
        		response.setContentType("application/json");
        		response.getOutputStream().write(epres.toString().getBytes());
        	}
        }
    }
    
	/**
	 * Helper function to add the user's connection info (IP Address and User Agent) 
	 * into session
	 * @param request HTTP Request Object
	 * @param session User Session
	 */
	protected void assignLocationToSession(HttpServletRequest request, HttpSession session) {
		// Get the user's IP Address
		String addr = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
		if (StringUtil.isEmpty(addr)) addr = request.getRemoteAddr();
		
		// Add the IP and the user agent to the session
		session.setAttribute(USER_IP_ADDRESS, addr);
		session.setAttribute(USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT));
	}
	
	/**
	 * Checks to make sure the user's ip and user agent haven't changed
	 * @param request Http Request Object
	 * @param session User Session
	 * @throws SecurityAuthorizationException 
	 */
	protected void validateUserInfo(HttpServletRequest request, HttpSession session) throws SecurityAuthorizationException {
		String addr = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
		if (StringUtil.isEmpty(addr)) addr = request.getRemoteAddr();
		
		if (! addr.equals(session.getAttribute(USER_IP_ADDRESS)))
			throw new SecurityAuthorizationException("IP Address Changed");
		
		if (! request.getHeader(HttpHeaders.USER_AGENT).equals(session.getAttribute(USER_AGENT)))
			throw new SecurityAuthorizationException("User Agent Changed");
		
	}

}
