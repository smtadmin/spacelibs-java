package com.siliconmtn.io;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// JDK 11.x
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// J2EE 8.x
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

// Spacelibs 1.x
import com.siliconmtn.data.report.ExcelReport;

/****************************************************************************
 * <b>Title</b>: StreamUtilTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Unit tests for the StreamUtil class.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 17, 2021
 * @updates:
 ****************************************************************************/
class StreamUtilTest {
	
	// Members
	ExcelReport ar;
	
	// Mocks
	HttpServletResponse response;
	private ServletOutputStream servletOutputStream;
	
	/**
	 * Sets up the report and stream util for testing
	 * @throws Exception
	 */
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		// Initialize the response
		response = mock(HttpServletResponse.class);
		servletOutputStream = mock(ServletOutputStream.class);
		
		// Add the header columns
		Map<String, String> headerMap = new LinkedHashMap<>();
		headerMap.put("one","Heading One");
		headerMap.put("two","Heading Two");

		// Add the row data
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("one", "Some Value");
		data.put("two", "Some Other Value");
		List<Map<String, Object>> rows = new ArrayList<>();
		rows.add(data);
		
		// Setup the report
		ar = new ExcelReport(headerMap);
		ar.setData(rows);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.StreamUtil#StreamUtil()}.
	 */
	@Test
	void testStreamUtil() {
		StreamUtil su = new StreamUtil();
		assertNotNull(su);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.StreamUtil#streamReport(com.siliconmtn.data.report.AbstractReport, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	void testStreamReport() throws Exception {
		// Add params to report
		ar.setFileName("My Test File");

		// Add the mocked ServletOutputStream
		when(response.getOutputStream()).thenReturn(servletOutputStream);
		
		// Call the util class
		StreamUtil su = new StreamUtil();
		assertDoesNotThrow(() -> {
			su.streamReport(ar, response);
		});
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.StreamUtil#streamReport(com.siliconmtn.data.report.AbstractReport, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	void testStreamReportAttachment() throws Exception {
		// Add params to report
		ar.setFileName("My Test File");
		ar.setHeaderAttachment(true);

		// Add the mocked ServletOutputStream
		when(response.getOutputStream()).thenReturn(servletOutputStream);
		
		// Call the util class
		StreamUtil su = new StreamUtil();
		assertDoesNotThrow(() -> {
			su.streamReport(ar, response);
		});
	}

	/**
	 * Test method for {@link com.siliconmtn.io.StreamUtil#streamReport(com.siliconmtn.data.report.AbstractReport, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	void testStreamReportNoFilename() throws Exception {
		// Add the mocked ServletOutputStream
		when(response.getOutputStream()).thenReturn(servletOutputStream);
		
		// Call the util class
		StreamUtil su = new StreamUtil();
		
		assertDoesNotThrow(() -> {
			su.streamReport(ar, response);
		});
		
	}
}
