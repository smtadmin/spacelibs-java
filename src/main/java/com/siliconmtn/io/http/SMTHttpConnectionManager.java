package com.siliconmtn.io.http;

// JDK 11.x
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

// Log4j 2.x
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: SMTHttpConnectionManager.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Wrapper class around URLs and connections.  Manages the 
 * sessions via cookies.  This allows  multiple requests to be made to the same 
 * server using any cookies that were present 
 * during the initial connection as well as subsequent sessions.  Allow
 * for the setting of an SSLSocketFactory in support of certificate-based auth
 * connections.  Redirects, timeouts, headers (both request and response) are managed.
 * Because we return byte[], any type of data can be retrieved
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 15, 2021
 * @updates:
 ****************************************************************************/
public class SMTHttpConnectionManager {
	
	
	/**
	 * Identifies the type of HTTP Connection
	 */
	public enum HttpConnectionType {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
	}

	/**
	 * Default connection prefix if not supplied
	 */
	public static final String HTTP_CONN_PREFIX = "http://";
	
	/**
	 * Default connection prefix if not supplied
	 */
	public static final String HTTPS_CONN_PREFIX = "https://";
	
	/**
	 * Socket timeout in ms
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 30000;

	/**
	 * Response code formatted string
	 */
	public static final String RESPONSE_CODE = "Response-Code";

	/**
	 * Defines the delimiter used in the cookie data between elements
	 */
	public static final String COOKIE_DELIMITER = ";";

	/**
	 * Defines the delimiter used in the cookie data between key/value pairs
	 */
	public static final String COOKIE_VALUE_DELIMITER = "=";

	/**
	 * Cookie header name
	 */
	public static final String COOKIE_HEADER_NAME = "Set-Cookie";

	/**
	 * String representation in the header 
	 */
	public static final String COOKIE_NAME = "Cookie";

	/**
	 * Identifies the request method for the connection as a GET method
	 */
	public static final String HTTP_GET_METHOD = "GET";

	/**
	 * Identifies the request method for the connection as a GET method
	 */
	public static final String HTTP_POST_METHOD = "POST";

	/**
	 * Identifies the request method for the connection as a GET method
	 */
	public static final String HTTP_PUT_METHOD = "PUT";
	
	/**
	 * Identifies the content type for the POST method.
	 */
	public static final String REQUEST_PROPERTY_CONTENT_TYPE = "Content-Type";
	
	/**
	 * Identifies the lengthof the message when posting data
	 */
	public static final String REQUEST_PROPERTY_CONTENT_LENGTH = "Content-Length";
	
	/**
	 * sslSocketFactory The SSLSocketFactory that is set on the HTTPS connection
	 * object just after it is instantiated and prior to any other properties being set
	 * for said object.  If a non-https url is passed to the connect methods, the
	 * SSLSocketFactory object will be ignored.
	 */
	private SSLSocketFactory sslSocketFactory;
	
	// Members
	static final Logger log = LogManager.getLogger(SMTHttpConnectionManager.class);
	private int connectionTimeout;
	private boolean followRedirects = true;
	private Map<String, String> requestHeaders;
	private int responseCode;
	private Map<String, String> cookies;
	private Map<String, String> headerMap;
	private int redirectLimit = 10;
	private boolean useCookieHandler = false;
	
	/**
	 * Initializes the manager
	 */
	public SMTHttpConnectionManager() {
		requestHeaders = new HashMap<>();
		cookies = new LinkedHashMap<>();
		headerMap = new LinkedHashMap<>();
	}
	
	/**
	 * Specifies if the cookie handler should be utilized
	 * @param useCookieHandler Boolean to indicate whther or not to use the provided
	 * cookie manager
	 */
	public SMTHttpConnectionManager(boolean useCookieHandler) {
		this();
		this.useCookieHandler = useCookieHandler;
	}
	
	/**
	 * Accepts an SSLSocketFactory.  The SSLSocketFactory is set on an HTTPS 
	 * connection object (HttpsURLConnection) when the connection object is 
	 * created, provided that the requested url's protocol is https.  Otherwise the 
	 * SSLSocketFactory object is not used.
	 * @param sslSocketFactory Socket factory to utilize for SSL connections
	 */
	public SMTHttpConnectionManager(SSLSocketFactory sslSocketFactory) {
		this();
		setSslSocketFactory(sslSocketFactory);
	}
	
	/**
	 * Retrieves data from an HTTP server and returns the data
	 * @param url fully qualified URL (http://www.somedomain.com)
	 * @param parameters HTTP POST data as a Map of key, value pairs
	 * @return binary data containing information retrieved from the site
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * Defaults to POST if type is null
	 * @throws IOException When data can't be retrieved, this exception is thrown
	 */
	public byte[] getRequestData(String url, Map<String, Object> parameters, HttpConnectionType type) 
	throws IOException {
		if (StringUtil.isEmpty(url)) throw new IOException("Url is required");
		return connect(createURL(url), convertPostData(parameters), type == null ? HttpConnectionType.POST : type);
	}
	
	/**
	 * Retrieves data from an HTTP server and returns the data
	 * @param url fully qualified URL (http://www.somedomain.com)
	 * @param parameters HTTP POST data as a Map of key, value pairs
	 * @return binary data containing information retrieved from the site
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * Defaults to POST if type is null
	 * @throws IOException When data can't be retrieved, this exception is thrown
	 */
	public byte[] getRequestData(URL url, Map<String, Object> parameters, HttpConnectionType type) 
	throws IOException {
		if (url == null) throw new IOException("Url is required");
		return connect(url, convertPostData(parameters), type == null ? HttpConnectionType.POST : type);
	}

	/**
	 * Connects to a HTTP server using the supplied URL and gets the data
	 * @param actionUrl
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * @return data for the request
	 * @throws IOException When data can't be retrieved, this exception is thrown
	 */
	private byte[] connect(URL actionUrl, byte[] postDataBytes, HttpConnectionType type) throws IOException {
		int nRead;
		byte[] byteBuffer = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = connectStream(actionUrl, postDataBytes, 0, type)) {
			while ((nRead = in.read(byteBuffer)) != -1) {
				baos.write(byteBuffer, 0, nRead);
			}

		}
		
		return baos.toByteArray();
	}
	
	/**
	 * Connects to the end device and returns a stream so the data can be 
	 * processed sequentially.
	 * @param url URL for the connection
	 * @param params Post data to pass as a Map key, value pairs
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * @return Connection stream to the data
	 * @throws IOException When data can't be retrieved, this exception is thrown
	 */
	public InputStream getConnectionStream(URL url, Map<String, Object> params, HttpConnectionType type) throws IOException {
		return connectStream(url, convertPostData(params), 0, type);
	}
	
	/**
	 * Connects to the end device and returns a stream so the data can be processed sequentially
	 * @param actionUrl URL for the connection
	 * @param postData Post data to pass
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * @return Stream of the connection
	 * @throws IOException When data can't be retrieved, this exception is thrown
	 */
	private InputStream connectStream(URL actionUrl, byte[] postDataBytes, int redirectAttempt, HttpConnectionType type) 
	throws IOException {
		// build connection
		HttpURLConnection conn = createConnection(actionUrl);

		// execute the connection
		executeConnection(conn, postDataBytes, type);

		//see if we need to follow a redirect
		if (followRedirects && (HttpURLConnection.HTTP_MOVED_PERM == responseCode || HttpURLConnection.HTTP_MOVED_TEMP == responseCode) && redirectAttempt < redirectLimit) {
			String redirUrl = conn.getHeaderField("Location");
			if (!StringUtil.isEmpty(redirUrl)) {
				conn.disconnect();
				return connectStream(createURL(redirUrl), postDataBytes, ++redirectAttempt, type);
			}
		}

		// return the response stream from the server - if the request failed return the error stream
		return (200 <= responseCode && 300 > responseCode) ? conn.getInputStream() : conn.getErrorStream();
	}
	
	/**
	 * Validates and creates a URL using actionUrl.  Default prototcal is http://
	 * @param actionUrl Creates a URL object form the string url
	 * @return URL object representing the string url
	 * @throws IOException  When data can't be retrieved, this exception is thrown
	 */
	URL createURL(String actionUrl) throws IOException {
		if (StringUtil.isEmpty(actionUrl)) throw new IOException("Invalid URL");
		if (! actionUrl.startsWith("http")) {
			actionUrl = ((sslSocketFactory == null) ? HTTP_CONN_PREFIX : HTTPS_CONN_PREFIX) + actionUrl;
		}
		
		return new URL(actionUrl);
	}
	
	/**
	 * Returns a connection type based on the URL protocol
	 * @param url Pointer to the end server
	 * @return Http URL Connection to the end server
	 * @throws IOException  When data can't be retrieved, this exception is thrown
	 */
	private HttpURLConnection createConnection(URL url) throws IOException {
		// Set the cookie handler. Since this is and accessed 
		// via static method lack of use still requires action to be taken
		if (useCookieHandler) {
			CookieHandler.setDefault(new CookieManager());
		} else {
			CookieHandler.setDefault(null);
		}
		// build connection
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection sConn = (HttpsURLConnection) url.openConnection();
			if (sslSocketFactory != null) sConn.setSSLSocketFactory(sslSocketFactory);
			return sConn;
		} else {
			return (HttpURLConnection) url.openConnection();
		}
	}


	/**
	 * Initializes and executes the connection
	 * @param conn COnnection to the end server
	 * @param postData data to send to the server
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * @throws IOException  When data can't be retrieved, this exception is thrown
	 */
	private void executeConnection(HttpURLConnection conn, byte[] postDataBytes, HttpConnectionType type) throws IOException {
		// Setup the connection parameters
		initConnection(conn, postDataBytes, type);

		// connect and retrieve data
		conn.connect();
		responseCode = conn.getResponseCode();

		//Parse header information
		storeCookies(conn);
	}
	
	/**
	 * Initializes the Connection parameters
	 * @param conn Connecton to the server to be initialized
	 * @param postData Data to post to the end server
	 * @param type Request Type.  One of GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE.
	 * @throws IOException  When data can't be retrieved, this exception is thrown
	 */
	private void initConnection(HttpURLConnection conn, byte[] postDataBytes, HttpConnectionType type) 
	throws IOException {
		// set additional common connection properties
		conn.setDoOutput(true);
		conn.setReadTimeout(connectionTimeout > 0 ? connectionTimeout : DEFAULT_SOCKET_TIMEOUT);
		conn.setConnectTimeout(connectionTimeout > 0 ? connectionTimeout : DEFAULT_SOCKET_TIMEOUT);
		conn.setUseCaches (false);
		conn.setAllowUserInteraction(false);
		HttpURLConnection.setFollowRedirects(followRedirects);
		conn.setInstanceFollowRedirects(followRedirects);
		conn.setDoInput(true);

		// Add any request headers to the connection
		setRequestHeaders(conn);

		// Add any cookies to the mix
		assignCookies(conn);

		// Define the request type
		conn.setRequestMethod(type.toString());

		if (HttpConnectionType.POST.equals(type) || HttpConnectionType.PUT.equals(type)) {
			if (! requestHeaders.containsKey(REQUEST_PROPERTY_CONTENT_TYPE))
				conn.setRequestProperty(REQUEST_PROPERTY_CONTENT_TYPE,"application/x-www-form-urlencoded");
			
			conn.setRequestProperty(REQUEST_PROPERTY_CONTENT_LENGTH, Integer.toString(postDataBytes.length));
			try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
				out.write(postDataBytes);
			}
		}
	}
	
	/**
	 * Parses the returned Set-Cookie parameter in the header into name value
	 * pairs and stores them in a hash map to be used during future connections.
	 * @param conn Connection to the server to retrieve / assign cookies
	 */
	void storeCookies(HttpURLConnection conn) {
		//Loop all of the HTTP header info
		int c = 0;
		while (conn.getHeaderField(c) != null) {
			// Store each header param in the headerMap collection
			String key = conn.getHeaderFieldKey(c);
			String value = StringUtil.defaultString(conn.getHeaderField(c));
			headerMap.put(key, value);
			
			// Find the Set-Cookie parameters
			if (COOKIE_HEADER_NAME.equalsIgnoreCase(key)) {
				// Parse out the data
				int length = value.indexOf(COOKIE_DELIMITER);
				if (length < 0) length = value.length();
				value = value.substring(0, length);

				// Parse out the name/value pairs
				int sepVal = value.indexOf(COOKIE_VALUE_DELIMITER);
				if (sepVal > -1) {
					String valueKey = value.substring(0,sepVal);
					String valueVal = value.substring(sepVal + 1, value.length());
					addCookie(valueKey, valueVal);
				}
			}

			c++;
		}

		// Add the Response Code
		headerMap.put(RESPONSE_CODE, Integer.toString(responseCode));
	}
	
	/**
	 * Converts the map data into a url encoded string and converts to a byte[]
	 * @param postData Converts map of data elements into a delimited string
	 * @return URL encoded data parameters
	 */
	public byte[] convertPostData(Map<String, Object> postData) {
		if (postData == null || postData.isEmpty()) return new byte[0];
		StringBuilder sb = new StringBuilder(512);
		
		//convert the postData Map to URL-encoded UTF-8 key=value pairs
		for (Entry<String, Object> entry : postData.entrySet()) {
			if(sb.length() > 0) sb.append('&');
			
			if(entry.getValue() instanceof Object[] || entry.getValue() instanceof Collection) 
				listParams(sb, entry);
			else {
				String value = StringUtil.defaultString(entry.getValue() + "", "");
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8));
			}
		}
		
		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Helper method ensures that multiple values for a given key get added
	 * correctly.
	 * @param sb assigns key values into the string builder
	 * @param entry items to assign
	 */
	private void listParams(StringBuilder sb, Entry<String, Object> entry) {
		Object raw = entry.getValue();
		String key = entry.getKey();
		Object[] values = (raw instanceof Collection) ? ((Collection<?>)raw).toArray() : (Object[]) raw;

		int i = 0;
		for(Object o : values) {
			if(i > 0) sb.append("&");
			sb.append(key).append('=').append(URLEncoder.encode(o.toString(), StandardCharsets.UTF_8));
			i++;
		}
	}
	
	/**
	 * Sets the factory for the SSL connection
	 * @param sslSocketFactory the sslSocketFactory to set
	 */
	public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	/**
	 * Returns the socket factory
	 * @return sslSocketFactory assigned
	 */
	public SSLSocketFactory getSslSocketFactory() {
		return this.sslSocketFactory;
	}

	/**
	 * Gets the connection timeout
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Determines if this class will follow redirects
	 * @return the followRedirects
	 */
	public boolean isFollowRedirects() {
		return followRedirects;
	}

	/**
	 * Gets the request headers
	 * @return the requestHeaders
	 */
	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	/**
	 * Gets the onnection response code
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * Gets the cookies
	 * @return the cookies
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * Gets the header maps
	 * @return the headerMap
	 */
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	/**
	 * Number of redirects to foloow
	 * @return the redirectLimit
	 */
	public int getRedirectLimit() {
		return redirectLimit;
	}

	/**
	 * Determines if cookie handler is in use
	 * @return the useCookieHandler
	 */
	public boolean isUseCookieHandler() {
		return useCookieHandler;
	}

	/**
	 * Sets the connection timeout
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Sets whether redirects should be followed
	 * @param followRedirects the followRedirects to set
	 */
	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	/**
	 * Sets request headers for the connection
	 * @param requestHeaders the requestHeaders to set
	 */
	public void setRequestHeaders(Map<String, String> requestHeaders) {
		if (requestHeaders == null) this.requestHeaders.clear();
		else this.requestHeaders = requestHeaders;
	}
	
	/**
	 * Adds any request headers assigned to the request connection
	 * @param conn Connection to the end server
	 */
	protected void setRequestHeaders(HttpURLConnection conn) {
		if (requestHeaders.isEmpty()) return;
		for (Map.Entry<String, String> entry: requestHeaders.entrySet())
			conn.setRequestProperty(entry.getKey(), entry.getValue());
	}
	
	/**
	 * Adds a header map value for the HTTP connection
	 * @param key Cookie unique identifier
	 * @param value Cookie value
	 */
	public void addCookie(String key, String value) {
		cookies.put(key, value);
	}

	/**
	 * Map containing multiple cookies to add
	 * @param cookies the cookies to set
	 */
	public void setCookies(Map<String, String> cookies) {
		if (cookies == null) this.cookies.clear(); 
		else this.cookies = cookies;
	}
	
	/**
	 * Adds any cookies from the collection of stored cookies and formats
	 * the data into: Cookie: name=value; name=value;
	 * Do not use this to initially add cookies to the cookie map on this object, rather
	 * use the addCookie(key,value) method.
	 * @param conn Connection to the end server
	 */
	void assignCookies(HttpURLConnection conn) {
		if (cookies.isEmpty() || conn == null) return;
		StringBuilder sb = new StringBuilder(250);

		for (Map.Entry<String, String> entry: cookies.entrySet())
			sb.append(entry.getKey()).append(COOKIE_VALUE_DELIMITER).append(entry.getValue()).append(COOKIE_DELIMITER);

		conn.addRequestProperty(COOKIE_NAME, sb.toString());
	}

	/**
	 * Adds parameters to the header map
	 * @param headerMap the headerMap to set
	 */
	public void setHeaderMap(Map<String, String> headerMap) {
		if (headerMap == null) this.headerMap.clear(); 
		else this.headerMap = headerMap;
	}

	/**
	 * Number of redirects to follow before quitting
	 * @param redirectLimit the redirectLimit to set
	 */
	public void setRedirectLimit(int redirectLimit) {
		this.redirectLimit = redirectLimit;
	}

	/**
	 * Sets whether the cookie handler should be used
	 * @param useCookieHandler the useCookieHandler to set
	 */
	public void setUseCookieHandler(boolean useCookieHandler) {
		this.useCookieHandler = useCookieHandler;
	}
}
