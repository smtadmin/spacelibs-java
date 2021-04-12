package com.siliconmtn.io.http;

// JDK 11.x
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

// JUnit5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

// Libs
import com.siliconmtn.io.http.SMTHttpConnectionManager.HttpConnectionType;

/// Mockito
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

// Apache IOUtils 1.3.2
import org.apache.commons.io.IOUtils;

/****************************************************************************
 * <b>Title</b>: SMTHttpConnectionManagerTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the HttpConnectionManager
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 15, 2021
 * @updates:
 ****************************************************************************/
class SMTHttpConnectionManagerTest {
	
	/**
	 * Request parameters for the test
	 */
	static Map<String, Object> params;
	Map<String, String> headers;
	Map<String, String> cookies;
	String sUrl = "https://www.siliconmtn.com";
	String url = "http://www.siliconmtn.com";
	
	@Mock
	SMTHttpConnectionManager connection;
	
	@Mock
	HttpURLConnection mockUrlConn;
	
	@Mock
	URL mockUrl;
	
	@Mock
	URL sMockUrl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		params = new HashMap<>();
		
		List<String> values = new ArrayList<>();
		values.add("one");
		values.add("two");
		values.add("three");
		
		params.put("counter", values);
		params.put("arrCtr", new String[] {"alpha", "brave", "charlie"});
		params.put("name","James");
		params.put("age","30");
	}
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
	
		// Instantiate the conn mgr
		connection = new SMTHttpConnectionManager();
		
		// Assign the headers
		headers = new HashMap<>();
		headers.put("Host", "www.siliconmtn.com");
		headers.put("Referer", "www.google.com");
		headers.put("User-Agent", "Mozilla/5.0 (platform; rv:geckoversion) Gecko/geckotrail");
		
		// Assign the cookies
		cookies = new HashMap<>();
		cookies.put("JSESSION_ID", "12345678");
		cookies.put("AWSALB", "AWS_COOKIE");
		cookies.put("AWSALBCORS", "AWS_COR_COOKIE");
	}
	
	/**
	 * Initializes and turns cookie management on and off
	 */
	@Test
	void testSMTHttpConnectionManagerBoolean() {
		
		SMTHttpConnectionManager newConn = new SMTHttpConnectionManager(true);
		assertTrue(newConn.isUseCookieHandler());
		
		newConn = new SMTHttpConnectionManager(false);
		assertFalse(newConn.isUseCookieHandler());
	}

	/**
	 * Validates the constructor with an SSL Socket factory
	 */
	@Test
	void testSMTHttpConnectionManagerSSLSocketFactory() {
		assertDoesNotThrow(() -> new SMTHttpConnectionManager(null));
	}

	/**
	 * Retrieves the data form the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestData() throws Exception {
		
		// Valid the exception when null is passed
		String nullUrl = null;
		URL nullURL = null;
		assertThrows(IOException.class, () -> connection.getRequestData(nullUrl, null, HttpConnectionType.GET));
	    assertThrows(IOException.class, () -> connection.getRequestData(nullURL, null, HttpConnectionType.GET));
	    
		// Tests with a URL Class
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		mockUrlConn = mock(HttpURLConnection.class);
		InputStream mis = IOUtils.toInputStream("Hello World", "UTF-8");
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(mis);
		when(mockUrlConn.getErrorStream()).thenReturn(mis);
		doReturn(200).when(mockUrlConn).getResponseCode();
	    assertEquals("Hello World", new String(connection.getRequestData(mockUrl, null, HttpConnectionType.GET)));
	}

	/**Cookie
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataString () throws Exception {
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		connection = Mockito.spy(connection);
		Mockito.doReturn(mockUrl).when(connection).createURL(url);

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(url, null, HttpConnectionType.GET)));
	}
	
	/**
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataStringNulls () throws Exception {
		connection.setConnectionTimeout(1000);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		connection = Mockito.spy(connection);
		Mockito.doReturn(mockUrl).when(connection).createURL(url);

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		when(mockUrlConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(url, null, null)));
	}
	
	/**
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataStringPut () throws Exception {
		headers.put(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE, "text/html");
		connection.setRequestHeaders(headers);
		connection.setConnectionTimeout(1000);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		connection = Mockito.spy(connection);
		Mockito.doReturn(mockUrl).when(connection).createURL(url);

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		when(mockUrlConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(url, null, HttpConnectionType.PUT)));
	}
	
	
	/**
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataURL () throws Exception {
		headers.put(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE, "text/html");
		connection.setRequestHeaders(headers);
		connection.setConnectionTimeout(1000);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		when(mockUrlConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(mockUrl, null, HttpConnectionType.PUT)));
	}
	
	/**
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataURLNull () throws Exception {
		headers.put(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE, "text/html");
		connection.setRequestHeaders(headers);
		connection.setConnectionTimeout(1000);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		when(mockUrlConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(mockUrl, null, null)));
	}
	
	/**
	 * Retrieves the data from the end server.  Needs an http mock
	 */
	@Test
	void testGetRequestDataStringPutNoHeader () throws Exception {
		connection.setConnectionTimeout(1000);
		connection.setUseCookieHandler(true);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		connection = Mockito.spy(connection);
		Mockito.doReturn(mockUrl).when(connection).createURL(url);

		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Error Message", "UTF-8"));
		when(mockUrlConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		doReturn(200).when(mockUrlConn).getResponseCode();
		assertEquals("Hello World", new String(connection.getRequestData(url, null, HttpConnectionType.PUT)));
	}

	/**
	 * Converts the map of past params into a post formatted data string
	 */
	@Test
	void testConvertPostData() {
		SMTHttpConnectionManager conn = new SMTHttpConnectionManager(true);
		assertTrue(new String(conn.convertPostData(params)).contains("one"));
		assertEquals("", new String(conn.convertPostData(null)));
		assertFalse(new String(conn.convertPostData(new HashMap<>())).contains("one"));
	}

	/**
	 * Tests the ssl socket information timeout metadata
	 * @throws Exception
	 */
	@Test
	void testSetSslSocketFactory() {
		connection.setSslSocketFactory(null);
		assertNull(connection.getSslSocketFactory());
	}

	/**
	 * Tests the connection timeout metadata
	 * @throws Exception
	 */
	@Test
	void testGetConnectionTimeout() throws Exception {
		connection.setConnectionTimeout(5);
		assertEquals(5, connection.getConnectionTimeout());
	}

	/**
	 * Tests the connection meta data around following redirects
	 * @throws Exception
	 */
	@Test
	void testIsFollowRedirects() throws Exception {
		connection.setFollowRedirects(true);
		assertTrue(connection.isFollowRedirects());
	}

	/**
	 * Tests the connection request headers
	 * @throws Exception
	 */
	@Test
	void testGetRequestHeaders() throws Exception {
		Map<String, String> nullHeaders = null;
		connection.setRequestHeaders(nullHeaders);
		assertEquals(0, connection.getRequestHeaders().size());
		
		connection.setRequestHeaders(headers);
		assertEquals(3, connection.getRequestHeaders().size());
	}

	/**
	 * Test the response code from the request
	 * @throws Exception
	 */
	@Test
	void testGetResponseCode() throws Exception {
		assertEquals(0, connection.getResponseCode());
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetCookies() throws Exception {
		connection.setCookies(cookies);
		assertEquals(3, connection.getCookies().size());
		
		// Loop the original cookies and make sure they are all returned
		for(String s: cookies.values()) {
			assertTrue(connection.getCookies().values().contains(s));
		}
		
		cookies = null;
		connection.setCookies(cookies);
		assertEquals(0, connection.getCookies().size());
		
		HttpURLConnection cConn = null;
		connection.assignCookies(cConn);
	}

	/**
	 * Gets the values in the headerMap
	 * @throws Exception
	 */
	@Test
	void testGetHeaderMap() throws Exception {
		connection.setHeaderMap(cookies);
		assertEquals(3, connection.getHeaderMap().size());
		
		// Loop the original cookies and make sure they are all returned
		for(String s: cookies.values()) {
			assertTrue(connection.getHeaderMap().values().contains(s));
		}
		
		connection.setHeaderMap(null);
		assertEquals(0, connection.getHeaderMap().size());
	}

	/**
	 * Assigns and gets the maximum number of redirects to follow on a request
	 * @throws Exception
	 */
	@Test
	void testGetRedirectLimit() throws Exception {
		connection.setRedirectLimit(5);
		assertEquals(5, connection.getRedirectLimit());
	}

	/**
	 * tests getting and setting the cookie handler
	 * @throws Exception
	 */
	@Test
	void testIsUseCookieHandler() throws Exception {
		connection.setUseCookieHandler(true);
		assertTrue(connection.isUseCookieHandler());
	}

	/**
	 * Tests the get connection stream method.  This method performs redirects as 
	 * needed when response code is 30X.  
	 * actions and other 
	 * @throws Exception
	 */
	@Test
	void testGetConnectionStream() throws Exception {
		// Tests with a URL Class
		connection.setFollowRedirects(true);
		connection.setRedirectLimit(1);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrlConn.getHeaderField("Location")).thenReturn("http://www.google.com");
		
		InputStream mis = IOUtils.toInputStream("Hello World", "UTF-8");
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(mis);
		when(mockUrlConn.getErrorStream()).thenReturn(mis);
		doReturn(HttpURLConnection.HTTP_MOVED_PERM).when(mockUrlConn).getResponseCode();
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);

		
		doReturn(HttpURLConnection.HTTP_MOVED_PERM).when(mockUrlConn).getResponseCode();
		when(mockUrlConn.getHeaderField("Location")).thenReturn(null);
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
	}
	
	/**
	 * Tests the get connection stream method.  This method performs redirects as 
	 * needed when response code is 30X.  
	 * actions and other 
	 * @throws Exception
	 */
	@Test
	void testGetConnectionStreamRedirNull() throws Exception {
		// Tests with a URL Class
		connection.setFollowRedirects(true);
		connection.setRedirectLimit(0);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrlConn.getHeaderField("Location")).thenReturn(null);
		
		InputStream mis = IOUtils.toInputStream("Hello World", "UTF-8");
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(mis);
		when(mockUrlConn.getErrorStream()).thenReturn(mis);
		doReturn(HttpURLConnection.HTTP_MOVED_TEMP).when(mockUrlConn).getResponseCode();
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
	}
	
	/**
	 * Tests the get connection stream method.  This method performs redirects as 
	 * needed when response code is 30X.  
	 * actions and other 
	 * @throws Exception
	 */
	@Test
	void testGetConnectionStream404() throws Exception {
		// Tests with a URL Class
		connection.setFollowRedirects(false);
		connection.setRedirectLimit(0);
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("http://www.siliconmtn.com"));
		mockUrlConn = mock(HttpURLConnection.class);
		when(mockUrlConn.getHeaderField("Location")).thenReturn(null);
		
		InputStream mis = IOUtils.toInputStream("Hello World", "UTF-8");
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(mis);
		when(mockUrlConn.getErrorStream()).thenReturn(mis);
		doReturn(404).when(mockUrlConn).getResponseCode();
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
		
		doReturn(100).when(mockUrlConn).getResponseCode();
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
	}

	@Test
	void testGetConnectionStreamSSL() throws Exception {
		// Test with no SSL Factory
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("https://www.siliconmtn.com"));
		when(mockUrl.getProtocol()).thenReturn("https");
		mockUrlConn = mock(HttpsURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
		
		// Test with an SSL Factory
		connection.setSslSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault());
		mockUrl = mock(URL.class, Mockito.withSettings().useConstructor("https://www.siliconmtn.com"));
		when(mockUrl.getProtocol()).thenReturn("https");
		mockUrlConn = mock(HttpsURLConnection.class);
		when(mockUrl.openConnection()).thenReturn(mockUrlConn);
		when(mockUrlConn.getInputStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		when(mockUrlConn.getErrorStream()).thenReturn(IOUtils.toInputStream("Hello World", "UTF-8"));
		assertTrue(connection.getConnectionStream(mockUrl, null, HttpConnectionType.GET) instanceof InputStream);
	}

	/**
	 * Tests the create URL Method.  Validates url is created and exception is 
	 * thrown when null data is presented
	 * @throws Exception
	 */
	@Test
	void testCreateURL() throws Exception {
		assertThrows(IOException.class, () -> connection.createURL(null));
	    assertEquals(-1, connection.createURL(sUrl).getPort());
	    assertEquals("www.siliconmtn.com", connection.createURL(sUrl).getHost());
	    assertEquals("www.siliconmtn.com", connection.createURL("www.siliconmtn.com").getHost());

	    connection.setSslSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault());
	    assertEquals("www.siliconmtn.com", connection.createURL("www.siliconmtn.com").getHost());
	}
	
	/**
	 * tests the storage of cookies capabilities
	 * @throws Exception
	 */
	@Test
	void testStoreCookies() throws Exception {
		URL myUrl = new URL(sUrl);
		mockUrlConn = (HttpURLConnection)myUrl.openConnection();
		mockUrlConn = Mockito.spy(mockUrlConn);
		Mockito.doReturn("JSESSION_ID=12345678").when(mockUrlConn).getHeaderField(0);
		Mockito.doReturn("AWSALB=AWS_COOKIE").when(mockUrlConn).getHeaderField(1);
		Mockito.doReturn("AWSALBCORS=AWS_COR_COOKIE").when(mockUrlConn).getHeaderField(2);
		Mockito.doReturn("NO_VALUE").when(mockUrlConn).getHeaderField(3);
		Mockito.doReturn("Set-Cookie").when(mockUrlConn).getHeaderFieldKey(0);
		Mockito.doReturn("Set-Cookie").when(mockUrlConn).getHeaderFieldKey(1);
		Mockito.doReturn("Set-Cookie").when(mockUrlConn).getHeaderFieldKey(2);
		Mockito.doReturn("Set-Cookie").when(mockUrlConn).getHeaderFieldKey(3);

		connection.storeCookies(mockUrlConn);
		assertTrue(connection.getCookies().containsKey("JSESSION_ID"));
		assertTrue(connection.getCookies().containsKey("AWSALB"));
		assertTrue(connection.getCookies().containsKey("AWSALBCORS"));
	}

	/**
	 * Tests the assignment of cookies to the url connection class.  Utilizes a 
	 * Mockito Mock class to perform this action
	 * @throws Exception
	 */
	@Test
	void testAssignCookiesHttpURLConnection() throws Exception {
		URL myUrl = new URL(url);
		mockUrlConn = (HttpURLConnection)myUrl.openConnection();
		connection.assignCookies(mockUrlConn);
		assertNull(mockUrlConn.getRequestProperty("Cookie"));
		
		connection.setCookies(cookies);
		connection.assignCookies(null);
		
		connection.assignCookies(mockUrlConn);
		assertTrue(mockUrlConn.getRequestProperty("Cookie").contains("JSESSION_ID"));
		assertTrue(mockUrlConn.getRequestProperty("Cookie").contains("AWSALB"));
		assertTrue(mockUrlConn.getRequestProperty("Cookie").contains("AWSALBCORS"));
	}

	/**
	 * Tests the assignment of request headers to the url connection class.  Utilizes a 
	 * Mockito Mock class to perform this action
	 * @throws Exception
	 */
	@Test
	void testSetRequestHeadersHttpURLConnection() throws Exception {
		connection.setRequestHeaders(headers);
		URL myUrl = new URL(sUrl);
		mockUrlConn = (HttpURLConnection)myUrl.openConnection();
		connection.setRequestHeaders(mockUrlConn);
		
		assertTrue(mockUrlConn.getRequestProperties().toString().contains("Referer"));
		assertTrue(mockUrlConn.getRequestProperties().toString().contains("User-Agent"));
		assertFalse(mockUrlConn.getRequestProperties().toString().contains("Host"));
	}
}
