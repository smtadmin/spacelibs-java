package com.siliconmtn.io.api.security;

// JDK 11.x
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

// Jee 7.x
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

// Apache commons 3.x
import org.apache.commons.io.IOUtils;

// Jsoup 1.13.x
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

// 2.2.x
import org.owasp.esapi.ESAPI;

// Spacelibs 1.x
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: XSSRequestWrapper.java
 * <b>Project</b>: ezform-api
 * <b>Description: </b> Validates the headers and data from the request object.
 * Data with improper input has that input stripped .  Once the headers and body
 * has been updated, the data is streamed back into the request object to overwrite
 * the original data
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Scarola
 * @version 3.0
 * @since Mar 5, 2021
 * @updates:
 ****************************************************************************/
public class XSSRequestWrapper extends HttpServletRequestWrapper{
	
	
	
	private byte[] rawData;
    private HttpServletRequest request;
    protected ResettableServletInputStream servletStream;

    /**
     * Constructor that assigns the request object and sets the servlet stream to
     * a resettable input stream defined below
     * @param request Http request object to validate/parse
     */
    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();
    }
    
    /**
     * Strips the Unwanted tags from the request parameter value
     * @param value Body of the message to strip
     * @return Stripped data elements
     */
    public static String stripXSS(String value) {
        if (StringUtil.isEmpty(value)) return value;
        
        value = ESAPI.encoder().canonicalize(value).replace("\0", "");
        return Jsoup.clean(value, Whitelist.none());
    }
    
    /**
     * Resets the request object utilizing the provided byte array
     * @param newRawData Raw request obejct data
     */
    public void resetInputStream(byte[] newRawData) {
        rawData = newRawData;
        servletStream.stream = new ByteArrayInputStream(newRawData);
    }

    /*
     * (non-javadoc)
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader(), StandardCharsets.UTF_8);
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    /*
     * (non-javadoc)
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader(), StandardCharsets.UTF_8);
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        
        return new BufferedReader(new InputStreamReader(servletStream));
    }
    
    /*
     * (non-javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return new String[0];
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }

    /*
     * (non-javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    /*
     * (non-javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    /*
     * (non-javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(java.lang.String)
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> result = new ArrayList<>();
        Enumeration<String> headers = super.getHeaders(name);
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String[] tokens = header.split(",");
            for (String token : tokens) {
                result.add(stripXSS(token));
            }
        }
        
        return Collections.enumeration(result);
    }
    
    /**
     * Extends the ServletInputStream with local functionality
     */
    protected class ResettableServletInputStream extends ServletInputStream {

        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        	// Intentionally Blank
        }
    }
}
