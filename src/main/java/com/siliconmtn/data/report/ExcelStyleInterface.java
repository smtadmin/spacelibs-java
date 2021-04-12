package com.siliconmtn.data.report;

//app libs
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/****************************************************************************
 * <b>Title</b>: ExcelStyleInterface.java 
 * <b>Project</b>: WebCrescendo 
 * <b>Description: </b> Style interface to be inherited by concrete styles
 * 
 * <b>Copyright:</b> Copyright (c) 2016
 * <b>Company:</b> Silicon Mountain Technologies
 * @author Ryan Riker
 * @version 2.0
 * @since Sep 9, 2016
 * @updates:
 ****************************************************************************/
public interface ExcelStyleInterface {

	/**
	 * Returns the styles for the excel header row
	 * @param wb Workbook to get the styles from
	 * @return Cellstyle for the provided workbook
	 */
	public CellStyle getHeadingStyle(Workbook wb);
	
	/**
	 * Returns the styles for the excel title
	 * @param wb Workbook to get the styles from
	 * @return Cellstyle for the provided workbook
	 */
	public CellStyle getTitleStyle(Workbook wb);
	
	/**
	 * Returns the styles for the body title
	 * @param wb Workbook to get the styles from
	 * @return Cellstyle for the provided workbook
	 */
	public CellStyle getBodyStyle(Workbook wb);
	
	/**
	 * Defines whether the columns should be expanded by default
	 * @return boolean of whether the columns should be expanded
	 */
	public boolean getExpandColumnFlag();
	
	/**
	 * Sets whether the columns should be expanded by default
	 * @param expandColumnFlag Determines if the excel columns should
	 * be expanded to fit
	 */
	public void setExpandColumnFlag(boolean expandColumnFlag);
	
	/**
	 * Defines whether the date should be displayed under the title row 
	 * @return Whether the date is displayed
	 */
	public boolean displayDate();
	
	/**
	 * Sets the flag to display the date on the report header
	 * @param displayDate Sets the display date
	 */
	public void setDisplayDate(boolean displayDate);

	/**
	 * Style of the date
	 * @param wb Workbook to get the styles from
	 * @return Cellstyle for the provided workbook
	 */
	public CellStyle getDateStyle(Workbook wb);

}
