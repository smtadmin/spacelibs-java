package com.siliconmtn.data.report;

//jJDK 11.x
import java.util.HashMap;
import java.util.Map;

//apache poi 5.x
import org.apache.poi.ss.usermodel.BorderStyle;

/****************************************************************************
 * <b>Title</b>: BordersOnlyCellStyle.java 
 * <b>Project</b>: SpaceLibs-Java 
 * <b>Description: </b> Makes a default style were the cells have borders and 
 * columns are expanded.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
public class BordersOnlyCellStyle extends AbstractExcelCellStyle {

	public BordersOnlyCellStyle(){
		super();
		buildStyleMap();
	}
	
	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.AbstractExcelCellStyle#buildStyleMap()
	 */
	@Override
	public void buildStyleMap() {
		
		Map<String, Map<String, Object>> styleMap = new HashMap<>();
		styleMap.put(HEADER_MAP, buildHeaderStyle());
		styleMap.put(TITLE_MAP, buildTitleStyle());
		styleMap.put(BODY_MAP, buildBodyStyle());
		setStyleMap(styleMap);
		
	}

	/**
	 * generates title style
	 * @return
	 */
	private Map<String, Object> buildTitleStyle() {
		Map<String, Object> titleStyleMap = new HashMap<>();
		titleStyleMap.put(BORDER_BOTTOM, BorderStyle.THIN);
		titleStyleMap.put(BORDER_TOP, BorderStyle.THIN);
		titleStyleMap.put(BORDER_RIGHT, BorderStyle.THIN);
		titleStyleMap.put(BORDER_LEFT, BorderStyle.THIN);
		return titleStyleMap;
	}

	/**
	 * generates body style
	 * @return
	 */
	private Map<String, Object> buildBodyStyle() {
		Map<String, Object> bodyStyleMap = new HashMap<>();
		bodyStyleMap.put(BORDER_BOTTOM, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_TOP, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_RIGHT, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_LEFT, BorderStyle.THIN);
		return bodyStyleMap;
	}

	/**
	 * generates headerStyle
	 * @return
	 */
	private Map<String, Object> buildHeaderStyle() {
		Map<String, Object> headerStyleMap = new HashMap<>();
		headerStyleMap.put(BORDER_BOTTOM, BorderStyle.THIN);
		headerStyleMap.put(BORDER_TOP, BorderStyle.THIN);
		headerStyleMap.put(BORDER_RIGHT, BorderStyle.THIN);
		headerStyleMap.put(BORDER_LEFT, BorderStyle.THIN);
		return headerStyleMap;
	}

	/*
	 * (non-javadoc)
	 * @see com.smt.data.report.ExcelStyleInterface#setDisplayDate()
	 */
	@Override
	public void setDisplayDate(boolean displayDate) {
		this.displayDate = displayDate;
	}
}
