package com.siliconmtn.data.report;

//java 11.x
import java.util.HashMap;
import java.util.Map;

//App Libs - apache poi 5.x
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

/****************************************************************************
 * <b>Title</b>: StandardCellStyle.java 
 * <b>Project</b>: SpaceLibs-Java 
 * <b>Description: </b> This class builds the maps they a used to control the style
 * on the work sheet 
 * 
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
public class GreyHeadingBorderDataCellStyle extends AbstractExcelCellStyle {

	public GreyHeadingBorderDataCellStyle(){
		super();
		buildStyleMap();
	}

	/**
	 * builds the header style map
	 * @return
	 */
	private Map<String, Object> buildHeaderStyle(){
		Map<String, Object> headerStyleMap = new HashMap<>();
		headerStyleMap.put(FILL_FOREGROUND_COLOR, IndexedColors.GREY_25_PERCENT.index);
		headerStyleMap.put(FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);
		headerStyleMap.put(BOLD_WEIGHT, Boolean.TRUE);
		headerStyleMap.put(BORDER_RIGHT, BorderStyle.THIN);
		headerStyleMap.put(BORDER_LEFT, BorderStyle.THIN);
		return headerStyleMap;
	}
	/**
	 * builds the title style map
	 * @return
	 */
	private Map<String, Object> buildTitleStyle(){
		Map<String, Object> titleStyleMap = new HashMap<>();
		titleStyleMap.put(BOLD_WEIGHT, Boolean.TRUE);
		short t = 0;
		titleStyleMap.put(TEXT_WRAP,t);
		return titleStyleMap;
	}
	/**
	 * builds the body style map
	 * @return
	 */
	private Map<String, Object> buildBodyStyle(){
		Map<String, Object> bodyStyleMap = new HashMap<>();
		bodyStyleMap.put(BORDER_BOTTOM, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_TOP, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_RIGHT, BorderStyle.THIN);
		bodyStyleMap.put(BORDER_LEFT, BorderStyle.THIN);
		return bodyStyleMap;
	}
	
	/**
	 * builds the body style map
	 * @return
	 */
	private Map<String, Object> buildDateStyle(){
		return buildBodyStyle();
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
		styleMap.put(DATE_MAP, buildDateStyle());
		setStyleMap(styleMap);
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
