package com.siliconmtn.data.report;

// JDK 1.8
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

// Apache POI 3.13
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

// Space Libs 1.x
import com.siliconmtn.data.format.BooleanUtil;
import com.siliconmtn.data.format.DateFormat;
import com.siliconmtn.data.format.NumberUtil;
import com.siliconmtn.data.format.DateFormat.DatePattern;
import com.siliconmtn.data.report.ExcelStyleFactory.Styles;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: ExcelReport.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: takes a passed map of data and builds an Excel file dynamically.
 * The constructor takes the headerMap, which is used for column headings and 
 * to align the rows (responses).</b> 
 * 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Feb 14, 2021
 * @updates
 ****************************************************************************/

public class ExcelReport extends AbstractReport {
	/**
	 * Apache Cell Types lack important types.  Assign a cell value tye for 
	 * each cell so we can format the cell as desired 
	 */
	public enum CellValueType {
		BOOLEAN, DOUBLE, DATE, INTEGER, STRING, TIMESTAMP;
	}
	
	/**
	 * Sets the maximum allowable rows per sheet
	 */
	public static final int MAX_ROWS_PER_SHEET = 64000;

	// Members
	private static final long serialVersionUID = 1l;
	protected transient Collection<Map<String, Object>> rowData;
	protected transient Map<String, String> headerMap;
	protected String titleText;
	protected transient Workbook wb;
	protected transient CellStyle headerStyle;
	protected transient CellStyle titleStyle;
	protected transient CellStyle bodyStyle;
	protected transient CellStyle dateStyle;
	protected transient CellStyle tempDateStyle;
	protected boolean expandColumnFlag;
	protected boolean displayDate;
	protected int maxRowsPerSheet = MAX_ROWS_PER_SHEET;

	/**
	 * Constructor utilizes the NoStyle formatting
	 * @param headerMap Assigns a map with various report styles
	 */
	public ExcelReport(Map<String, String> headerMap) {
		this(headerMap, ExcelStyleFactory.getExcelStyle(Styles.NO_STYLE));
	}
	
	/**
	 * Constructor utilizes the NoStyle formatting
	 * @param headerMap  Assigns a map with various report style
	 * @param s Style map to utilize
	 */
	public ExcelReport(Map<String, String> headerMap, Styles s) {
		this(headerMap, ExcelStyleFactory.getExcelStyle(s));
	}

	/**
	 * Excel report using the provided style
	 * @param headerMap Map of the id and name for each column heading
	 * @param style Style map to utilize
	 */
	public ExcelReport(Map<String, String> headerMap, ExcelStyleInterface style) {
		super();
		wb = new HSSFWorkbook();
		this.headerMap = headerMap;
		headerStyle = style.getHeadingStyle(wb);
		titleStyle = style.getTitleStyle(wb);
		bodyStyle =   style.getBodyStyle(wb);
		dateStyle = style.getDateStyle(wb);
		expandColumnFlag = style.getExpandColumnFlag();
		displayDate = style.displayDate();
		tempDateStyle = wb.createCellStyle();
		CreationHelper createHelper = wb.getCreationHelper();
		tempDateStyle.setDataFormat(createHelper.createDataFormat().getFormat(DatePattern.DATETIME_SLASH_SHORT.getPattern()));
	}

	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.SMTReportVO#generateReport()
	 */
	@Override
	public byte[] generateReport() throws IOException {
		createSheet(null, titleText, headerMap, rowData);
		return getBytes(wb);
	}

	/**
	 * Create a sheet in the Workbook using the passed args
	 * @param sheetName Name of the sheet
	 * @param title Sheet title
	 * @param header Header style map
	 * @param rows Row data for the sheet
	 */
	protected void createSheet(String sheetName, String title,Map<String, String> header, Collection<Map<String, Object>> rows) {
		Sheet s = buildSheet(sheetName, title, header);
		int count = 0;
		int sheetCount = 1;
		Row row;
		
		//loop the data rows
		for (Map<String, Object> lineData : rows) {
			// If the rows exceeds the max, create a new sheet.  Resize columns on 
			// initial sheet if requested
			if (count > maxRowsPerSheet) {
				resizeSheetColumns(s, header, rows.size());
				s = buildSheet(sheetName + " - " + sheetCount++, title, header);
			}

			//add the row data
			row = s.createRow(s.getPhysicalNumberOfRows());
			addRowData(row, lineData, header);
			count++;
		}

		resizeSheetColumns(s, header, rows.size());
	}
	
	/**
	 * 	Making the cell more readable by expanding the cell widths. NOTE: This 
	 *  column resizing can be very costly on larger reports. Calculate a cost
	 *  based on rows*columns, and don't resize columns on larger reports.  
	 *  An 8500-row report was taking 14s to resize!
	 * @param s Sheet to resize
	 * @param header Header map for the sheet
	 * @param rowCount number of rows.  Used to determine the cost as resizing 
	 * takes a considerable amount of time
	 */
	protected void resizeSheetColumns(Sheet s, Map<String, String> header, int rowCount) {
		int cost = rowCount * s.getLastRowNum();
		
		if (expandColumnFlag && cost < 50000) {
			for (int i = 0; i < header.size(); i++)
				s.autoSizeColumn(i);
		}
	}

	/**
	 * Manage creating a sheet for the workbook.
	 * Will generate header row at the top of the sheet.
	 * @param sheetName Name of the sheet
	 * @param title Title of the sheet
	 * @param header Sheet header styles and info
	 * @return Excel sheet with the header and title
	 */
	private Sheet buildSheet(String sheetName, String title, Map<String, String> header) {
		//Create Excel Sheet inside the Workbook
		Sheet s = wb.createSheet();

		//name the sheet if desired
		if (!StringUtil.isEmpty(sheetName)) wb.setSheetName(wb.getSheetIndex(s), sheetName);

		//if requested add title row
		if (!StringUtil.isEmpty(title)) addTitleCell(s, title, header.keySet().size());

		// if requested, add current date
		if (displayDate) addDateRow(s, header.keySet().size());

		// add the header row
		addHeaderRow(s, header);

		return s;
	}

	/**
	 * Adds the header row to the excel spreadsheet
	 * @param s Excel sheet to update
	 * @param header Header styles and info to update
	 */
	protected void addHeaderRow(Sheet s, Map<String, String> header) {
		Row row = s.createRow(s.getPhysicalNumberOfRows());

		//Loop and set cell values for the header row.
		int cellCnt = 0;
		for (Map.Entry<String, String> entry : header.entrySet()) {
			Cell c = row.createCell(cellCnt++);
			c.setCellValue(entry.getValue());
			c.setCellStyle(headerStyle);
		}
	}

	/**
	 * iterates the row data and creates a Row on the Sheet for each one
	 * @param row Excel row object on a given sheet
	 * @param lineData Data to addd to the row
	 * @param header Header map to assign the data elements to the proper column
	 */
	private void addRowData(Row row, Map<String, Object> lineData, Map<String, String> header) {
		//loop across the headerMap and find the matching value for each column
		int cellCnt = 0;
		for(String code : header.keySet()) {
			Cell c = row.createCell(cellCnt++);
			Object value = lineData.get(code);
			
			// Convert nulls to empty so they display properly in the spreadsheet
			if (value == null) value = "";
			CellValueType type = setCellValue(value, c);
			setBodyCellStyle(c, type);
		}
	}

	/**
	 * Here to allow sub-classes to override and apply individual styling to a cell versus blanket styling. Last two 
	 * parameters to be utilized for customized logic around applying styles in subclasses.
	 * @param cell XSSF cell object
	 * @param type - the type of the cell value (Data Type)
	 */
	protected void setBodyCellStyle(Cell cell, CellValueType type) {
		if(CellValueType.DATE.equals(type) || CellValueType.TIMESTAMP.equals(type)) {
			cell.setCellStyle(dateStyle);
		} else {
			cell.setCellStyle(bodyStyle); 
		}

	} 

	/**
	 * This method adds a cell to the top of the report with the title string info inside it
	 * @param s Excel sheet to add the title
	 * @param title Title verbiage to be added
	 * @param headerMapSize size of the header map (merging columns)
	 */
	protected void addTitleCell(Sheet s,String title, int headerMapSize) {
		Row r = s.createRow(s.getPhysicalNumberOfRows());

		//fill it with the title string.
		Cell c = r.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(this.titleStyle);

		//merge it the length of the report.
		s.addMergedRegion(new CellRangeAddress(0,0,0,headerMapSize-1));
	}

	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.SMTReportVO#setData(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setData(Object rows) {
		if (rows == null) this.rowData = new ArrayList<>();
		else if (rows instanceof Collection)
			this.rowData = (Collection<Map<String, Object>>) rows;
	}
	
	/**
	 * Gets the row data passed in
	 * @return Collection of maps
	 */
	public Collection<Map<String, Object>> getRowData() {
		return rowData;
	}

	/**
	 * This method adds a cell to the top of the report with the title string info inside it
	 * @param s Excel sheet to add the date row
	 * @param headerMapSize size of the header map (merging columns)
	 */
	protected void addDateRow(Sheet s, int headerMapSize) {
		int numRows = s.getPhysicalNumberOfRows();
		Row r = s.createRow(numRows);

		//fill it with the title string.
		Cell c = r.createCell(0);
		c.setCellValue(DateFormat.toFormattedString(DatePattern.DATE_SLASH, new Date()));
		c.setCellStyle(this.bodyStyle);

		//merge it the length of the report.
		s.addMergedRegion(new CellRangeAddress(numRows,numRows,0,headerMapSize-1));
	}

	/**
	 * sets the boolean flag and sets the title string so a title cell can be triggered
	 * and added to the report before generation. 
	 * @param titleText Title of the document
	 */
	public void setTitle(String titleText) {
		this.titleText = titleText;
	}
	
	/**
	 * Returns the title of the Report
	 * @return Title of the report
	 */
	public String getTitle() {
		return this.titleText;
	}

	/**
	 * static helper method reusable by all Excel reports - turns a POI Workbook
	 * into a byte[] to be streamed back to the client.
	 * This is public because it's static; 3rd party classes can use it to serialize custom-build Workbooks.
	 * @param wb Excel workbook
	 * @return byte array of the workbook
	 * @throws IOException is thrown converting form the workbook toa  byte array
	 */
	public byte[] getBytes(Workbook wb) throws IOException {
		if (wb == null) return new byte[0];
		int estSize = 0;
		Iterator<Sheet> sIter = wb.sheetIterator();
		while(sIter.hasNext()) {
			Sheet s = sIter.next();
			estSize += s.getLastRowNum() * 5000; //figure 5kb for each row of data...a best guess.
		}

		//check against empty Sheets.
		estSize = Math.max(estSize, 100000);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(estSize)) {
			wb.write(baos);
			return baos.toByteArray();
		}
	}

	/**
	 * Determines the cell type based upon the cell data and sets cell value appropriately
	 * @param value Value to store in the cell
	 * @param c XSSF cell object
	 * @return Cell or data type of the value
	 */
	public CellValueType setCellValue(Object value, Cell c) {
		CellValueType cvt = CellValueType.STRING;
		if (value == null) value = "";

		// Determines the type of data nd csts it into the cell
		if (Pattern.matches("^[+-]?\\d*\\.?\\d+$", value.toString())) {
			Number num = NumberUtil.getNumber(value.toString());
			if (num instanceof Long) {
				c.setCellValue(num.longValue());
				cvt = CellValueType.INTEGER;
			} else {
				c.setCellValue(num.doubleValue());
				cvt = CellValueType.DOUBLE;
			}
		} else if (value instanceof Boolean) {
			c.setCellValue(BooleanUtil.toBoolean(value));
			cvt = CellValueType.BOOLEAN;
		} else if (value instanceof Timestamp) {
			c.setCellValue((Timestamp)value);
			cvt = CellValueType.TIMESTAMP;
		} else if (value instanceof Date) {
			c.setCellValue((Date)value);
			cvt = CellValueType.DATE;
		} else {
			if (Pattern.matches("^(\\d{4}-\\d{2}-\\d{2}).*", value + "")) {
				Date d = value.toString().length() > 10 ? 
						DateFormat.formatDate(DatePattern.ISO_SHORT, value + "") : 
						DateFormat.formatDate(DatePattern.DATE_DASH, value + "");
				
				c.setCellValue(d);
				cvt = CellValueType.DATE;
			} else {
				c.setCellValue(StringUtil.defaultString(value.toString()));
			}
		}
		
		return cvt;
	}
	
	/**
	 * Sets the max rows per sheet up to the maximum value for Excel.  If greater,
	 * max is reduced to the max allowable.  A minimum of 10 rows per sheet
	 * @param max max rows per sheet.  Must be less than the MAX_ROWS_PER_SHEET value
	 */
	public void setMaxRowsPerSheet(int max) {
		max = max < 10 ? 10 : max;
		maxRowsPerSheet = max > MAX_ROWS_PER_SHEET ? MAX_ROWS_PER_SHEET : max;
	}
	
	/**
	 * Retrieves the valueof the max rows per sheet
	 * @return max muber of rows per sheet
	 */
	public int getMaxRowsPerSheet() {
		return maxRowsPerSheet;
	}
	
	/**
	 * Returns the workbook
	 * @return Retreives the workbook created by this report
	 */
	public Workbook getWorkbook() {
		return wb;
	}
}