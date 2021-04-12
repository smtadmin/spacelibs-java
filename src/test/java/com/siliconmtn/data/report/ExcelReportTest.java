package com.siliconmtn.data.report;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

// Space Libs 1.x
import com.siliconmtn.data.format.DateFormat;
import com.siliconmtn.data.format.DateFormat.DatePattern;
import com.siliconmtn.data.report.ExcelReport.CellValueType;
import com.siliconmtn.data.report.ExcelStyleFactory.Styles;

// Apache POI 5.x
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

// JDK 11.x
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/****************************************************************************
 * <b>Title</b>: ExcelReportTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b>Test class for the Excel report egneration class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
class ExcelReportTest {
	
	private ExcelReport rpt;
	private Map<String, String> headerMap;
	private List<Map<String, Object>> dataSet;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		headerMap = new HashMap<>();
		headerMap.put("ID", "Identification");
		headerMap.put("NAME", "Name");
		headerMap.put("AGE", "Age");
		headerMap.put("DOB", "Date of Birth");
		headerMap.put("NULL", "Null Column");
		rpt = new ExcelReport(headerMap);
		
		// Assign the data
		dataSet = new ArrayList<>();
		dataSet.add(getRowData());
		dataSet.add(getRowData());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#generateReport()}.
	 * @throws IOException 
	 */
	@Test
	void testGenerateReport() throws IOException {
		rpt.setData(dataSet);
		byte[] data = rpt.generateReport();
		assertNotNull(data);
		assertTrue(data.length > 3000);
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setData(java.lang.Object)}.
	 */
//	@Test
//	void testSetData() {
//		rpt.setData(null);
//		rpt.setData("Test");
//		rpt.setData(dataSet);
//	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setFileName(java.lang.String)}.
	 */
	@Test
	void testSetFilename() throws Exception {
		rpt.setFileName("MyReport.xlsx");
		assertEquals("MyReport.xlsx", rpt.getFileName());
		assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", rpt.getContentType());
		
		rpt.setFileName("AnotherReport.123xyz");
		assertNull(rpt.getContentType());
		
		try (MockedStatic<Files> theMock = Mockito.mockStatic(Files.class)) {
			theMock.when(() -> Files.probeContentType(ArgumentMatchers.any())).thenThrow(new IOException());
			ExcelReport rpt1 = new ExcelReport(headerMap);
			rpt1.setFileName("MyReport.xlsx");
			rpt1.setContentTypeByFileName();
			assertNull(rpt.getContentType());
		}
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#ExcelReport(java.util.Map, com.siliconmtn.data.report.ExcelStyleFactory.Styles)}
	 */
	@Test
	void testExcelReportMapOfStringStringStyles() {
		rpt = new ExcelReport(headerMap, Styles.BORDERS_ONLY);
		assertNotNull(rpt);
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setTitle(java.lang.String)}.
	 */
	@Test
	void testSetTitle() {
		rpt.setTitle("My Report Title");
		assertEquals("My Report Title", rpt.getTitle());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#createSheet(java.lang.String, java.lang.String, java.util.Map)}
	 * Tests edge conditions of the create Sheet process
	 * @throws Exception
	 */
	@Test
	void testCreateSheet() throws Exception {
		List<Map<String, Object>> largeData = new ArrayList<>(1024);
		for (int i=0; i < 800; i++) {
			largeData.add(getRowData());
		}
		
		assertEquals(800, largeData.size());
		ExcelStyleInterface esi = ExcelStyleFactory.getExcelStyle(Styles.STANDARD);
		esi.setExpandColumnFlag(true);
		esi.setDisplayDate(false);
		ExcelReport erpt = new ExcelReport(headerMap, esi);
		erpt.setMaxRowsPerSheet(700);
		erpt.setData(largeData);
		erpt.createSheet("My New Sheet", "Test Create Sheet", headerMap,largeData);
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#createSheet(java.lang.String, java.lang.String, java.util.Map)}
	 * Tests edge conditions of the create Sheet process
	 * @throws Exception
	 */
	@Test
	void testCreateSheetNoExpand() throws Exception {
		List<Map<String, Object>> largeData = new ArrayList<>(1024);
		for (int i=0; i < 50; i++) {
			largeData.add(getRowData());
		}
		
		assertEquals(50, largeData.size());
		ExcelStyleInterface esi = ExcelStyleFactory.getExcelStyle(Styles.STANDARD);
		esi.setExpandColumnFlag(true);
		esi.setDisplayDate(true);
		ExcelReport erpt = new ExcelReport(headerMap, esi);
		erpt.setData(largeData);
		erpt.createSheet("My New Sheet", "Test Create Sheet", headerMap,largeData);
	}


	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#getBytes()}
	 * Validates the ability to create a custom WB and convert to a byte[]
	 * @throws Exception
	 */
	@Test
	void testGetBytes() throws Exception {
		assertNotNull(rpt.getBytes(null));
		Workbook wb = new HSSFWorkbook();
		assertEquals(3584, rpt.getBytes(wb).length);
		wb.close();
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setCellValue(java.lang.Object, org.apache.poi.ss.usermodel.Cell)}
	 * @throws Exception
	 */
	@Test
	void testSetCellValue() throws Exception {
		Workbook wb = new HSSFWorkbook();
		Sheet s = wb.createSheet();
		Row row = s.createRow(0);
		Cell cell = row.createCell(0);
		
		rpt.setCellValue("12", cell);
		assertEquals(12, cell.getNumericCellValue());
		
		rpt.setCellValue("12.25", cell);
		assertEquals(12.25, cell.getNumericCellValue());
		
		rpt.setCellValue(true, cell);
		assertTrue(cell.getBooleanCellValue());
		
		rpt.setCellValue(Boolean.TRUE, cell);
		assertTrue(cell.getBooleanCellValue());
		
		rpt.setCellValue("James Camire", cell);
		assertEquals("James Camire", cell.getStringCellValue());
		
		rpt.setCellValue("2021-01-01", cell);
		assertNotNull(cell.getDateCellValue());
		
		rpt.setCellValue("2021-02-04T16:57:00.000Z", cell);
		assertNotNull(cell.getDateCellValue());
		
		rpt.setCellValue(new Date(), cell);
		assertNotNull(cell.getDateCellValue());
		
		rpt.setCellValue(DateFormat.formatTimestamp(new Date()), cell);
		assertNotNull(cell.getDateCellValue());
		
		rpt.setCellValue(null, cell);
		assertEquals("", cell.getStringCellValue());
		
		wb.close();
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setMaxRowsPerSheet(int)}
	 * Tests changing the max allowable rows per sheet
	 * @throws Exception
	 */
	@Test
	void testSetMaxRowsPerSheet() throws Exception {
		rpt.setMaxRowsPerSheet(0);
		assertEquals(10, rpt.getMaxRowsPerSheet());
		
		rpt.setMaxRowsPerSheet(100000);
		assertEquals(ExcelReport.MAX_ROWS_PER_SHEET, rpt.getMaxRowsPerSheet());
		
		rpt.setMaxRowsPerSheet(1000);
		assertEquals(1000, rpt.getMaxRowsPerSheet());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#setHeaderAttachment(boolean)}
	 * Test the ability to set/unset the report as an attachment
	 * @throws Exception
	 */
	@Test
	void testAttachment() throws Exception {
		rpt.setHeaderAttachment(false);
		assertFalse(rpt.isHeaderAttachment());
		
		rpt.setHeaderAttachment(true);
		assertTrue(rpt.isHeaderAttachment());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.data.report.ExcelReport#getAttributes()}
	 * Test the ability to set/unset the report dynamic attributes
	 * @throws Exception
	 */
	@Test
	void testGetAttributes() throws Exception {
		Date dob = DateFormat.formatDate(DatePattern.DATE_DASH, "1991-01-01");
		rpt.addAttribute("NAME", "James");
		rpt.addAttribute("AGE", 30);
		rpt.addAttribute("DOB", dob);
		assertEquals("James", rpt.getAttributes().get("NAME"));
		assertEquals(30, rpt.getAttributes().get("AGE"));
		assertEquals(dob, rpt.getAttributes().get("DOB"));
		
		Map<String, ?> map = rpt.getAttributes();
		
		rpt.setAttributes(null);
		assertEquals(0, rpt.getAttributes().size());
		
		rpt.setAttributes(map);
		assertEquals("James", rpt.getAttributes().get("NAME"));
		assertEquals(30, rpt.getAttributes().get("AGE"));
		assertEquals(dob, rpt.getAttributes().get("DOB"));
	}
	
	/**
	 * Builds a row of data.  used to insert large number of rows
	 * @return
	 */
	private Map<String, Object> getRowData() {
		Map<String, Object> data = new HashMap<>();
		data.put("ID", "12345");
		data.put("NAME", "James Camire");
		data.put("AGE", 56);
		data.put("NULL", null);
		
		// Randomly add a timestamp or date
		if (new Random().nextBoolean())
			data.put("DOB", DateFormat.formatDate(DatePattern.DATE_DASH, "1964-08-25"));
		else
			data.put("DOB", DateFormat.formatTimestamp(new Date()));
		
		return data;
	}
	
	/**
	 * tests the assignment of the body cell type
	 * @throws Exception
	 */
	@Test
	void testSetBodyCellStyle() throws Exception {
		Workbook wb = rpt.getWorkbook();
		Sheet s = wb.createSheet();
		Row row = s.createRow(0);
		Cell cell = row.createCell(0);
		
		rpt.setBodyCellStyle(cell, CellValueType.DATE);
		assertEquals(164, cell.getCellStyle().getDataFormat());
		
		rpt.setBodyCellStyle(cell, CellValueType.TIMESTAMP);
		assertEquals(164, cell.getCellStyle().getDataFormat());
		
		rpt.setBodyCellStyle(cell, CellValueType.STRING);
		assertEquals(0, cell.getCellStyle().getDataFormat());
	}

	/**
	 * Tests the set data method
	 * @throws Exception
	 */
	@Test
	void testSetData() throws Exception {
		rpt.setData(null);
		assertNotNull(rpt.getRowData());
		
		rpt.setData("Hello");
		assertNotNull(rpt.getRowData());
		
		Collection<Map<String, Object>> c = new ArrayList<>();
		Map<String, Object> items = new HashMap<>();
		items.put("key", "value");
		c.add(items);
		rpt.setData(c);
		assertEquals(1, rpt.getRowData().size());
	}
}
