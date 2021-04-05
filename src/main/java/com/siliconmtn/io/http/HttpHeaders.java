package com.siliconmtn.io.http;

/****************************************************************************
 * <b>Title:</b> HttpHeaders.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> HTTP header fields are components of the header section of 
 * request and response messages in the Hypertext Transfer Protocol (HTTP). 
 * They define the operating parameters of an HTTP transaction.  this class
 * has a constant for each of the available request headers
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 30, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class HttpHeaders {
	/**
	 * Private constructor due to all elements in this class are static
	 */
	private HttpHeaders() {
		super();
	}
	
	/**
	 * Media type(s) that is/are acceptable for the response. See Content negotiation.
	 * <p>Accept: text/html</p>
	 */
	public static final String ACCEPT = "Accept";

	/**
	 * Character sets that are acceptable.
	 * <p>Accept-Charset: utf-8</p>
	 */
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	
	/**
	 * List of acceptable encodings.
	 * <p>Accept-Encoding: gzip, deflate</p>
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	
	/**
	 * List of acceptable human languages for response in ISO format
	 * <p>Accept-Language: en-US</p>
	 */
	public static final String ACCEPT_LANGUAGE = "Accept-Encoding";

	/**
	 * Authentication credentials for HTTP authentication.
	 * <p>Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==</p>
	 */
	public static final String AUTHORIZATION = "Authorization";

	/**
	 * Used to specify directives that must be obeyed by all caching mechanisms 
	 * along the request-response chain.
	 * <p>Cache-Control: no-cache</p>
	 */
	public static final String CACHE_CONTROL = "Cache-Control";

	/**
	 * The type of encoding used on the data
	 * <p>Content-Encoding: gzip</p>
	 */
	public static final String CONTENT_ENCODING = "Content-Encoding";

	/**
	 * The length of the request body in octets (8-bit bytes).
	 * <p>Content-Length: 348</p>
	 */
	public static final String CONTENT_LENGTH = "Content-Length";
	
	/**
	 * The Media type of the body of the request (used with POST and PUT requests).
	 * <p>Content-Type: application/x-www-form-urlencoded</p>
	 */
	public static final String CONTENT_TYPE = "Content-Type";
	
	/**
	 * Disclose original information of a client connecting to a web server 
	 * through an HTTP proxy
	 * <p>Forwarded: for=192.0.2.60;proto=http;by=203.0.113.43 Forwarded: for=192.0.2.43, for=198.51.100.17</p>
	 */
	public static final String FORWARDED = "Forwarded";
	
	/**
	 * The email address of the user making the request.
	 * <p>From: user@example.com</p>
	 */
	public static final String FROM = "From";
	
	/**
	 * The domain name of the server (for virtual hosting), and the TCP port 
	 * number on which the server is listening. The port number may be omitted 
	 * if the port is the standard port for the service requested.
	 * <p>Host: en.wikipedia.org</p>
	 */
	public static final String HOST = "Host";
	
	/**
	 * Initiates a request for cross-origin resource sharing (asks server for 
	 * Access-Control-* response fields).
	 * <p>Origin: http://www.example-social-network.com</p>
	 */
	public static final String ORIGIN = "Origin";
	
	/**
	 * Implementation-specific fields that may have various effects anywhere 
	 * along the request-response chain.
	 * <p>Pragma: no-cache</p>
	 */
	public static final String PRAGMA = "Pragma";
	
	/**
	 * Authorization credentials for connecting to a proxy.
	 * <p>Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==</p>
	 */
	public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
	
	/**
	 * This is the address of the previous web page from which a link to the 
	 * currently requested page was followed. (The word "referrer" has been 
	 * misspelled in the RFC as well as in most implementations to the point 
	 * that it has become standard usage and is considered correct terminology)
	 * <p>Referer: http://en.wikipedia.org/wiki/Main_Page</p>
	 */
	public static final String REFERER = "Referer";
	
	/**
	 * Implementation-specific fields that may have various effects anywhere 
	 * along the request-response chain.
	 * <p>User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0</p>
	 */
	public static final String USER_AGENT = "User-Agent";
	
	// Common, Non-Standard Request Header Keys
	
	/**
	 * Mainly used to identify Ajax requests (most JavaScript frameworks send 
	 * this field with value of XMLHttpRequest); also identifies Android apps 
	 * using WebView
	 * <p>X-Requested-With: XMLHttpRequest</p>
	 */
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	
	/**
	 * A de facto standard for identifying the originating IP address of a client 
	 * connecting to a web server through an HTTP proxy or load balancer. 
	 * Superseded by Forwarded header.
	 * <p>Referer: http://en.wikipedia.org/wiki/Main_Page</p>
	 */
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
}
