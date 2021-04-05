package com.siliconmtn.data.report;

//java 11.x
import java.util.Map;

//app libs
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;

// Space Libs 1.x
import com.siliconmtn.data.format.BooleanUtil;
import com.siliconmtn.data.format.NumberUtil;

/****************************************************************************
 * <b>Title</b>: AbstractExcelCellStyle.java 
 * <b>Project</b>: SpaceLibs-Java 
 * <b>Description: </b> controls taking the maps made in the concrete classes
 * and turning them into true cell styles
 * 
 * <b>Copyright:</b> Copyright (c) 2016
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
public abstract class AbstractExcelCellStyle implements ExcelStyleInterface {

	private Map<String, Map<String, Object>> styleMap;
	public static final String FILL_FOREGROUND_COLOR = "fillForegroundColor";
	public static final String FILL_PATTERN = "fillPattern";

	public static final String BORDER_BOTTOM = "borderBottom";
	public static final String BORDER_TOP = "borderTop";
	public static final String BORDER_RIGHT = "borderRight";
	public static final String BORDER_LEFT = "borderLeft";
	public static final String TEXT_WRAP = "testWrap";

	public static final String BOLD_WEIGHT = "boldWeight";
	public static final String FONT_COLOR = "fontColor";

	public static final String HEADER_MAP = "headerMap";
	public static final String TITLE_MAP = "titleMap";
	public static final String BODY_MAP = "bodyMap";
	public static final String DATE_MAP = "dateMap";

	protected boolean expandColumnFlg = true;
	protected boolean displayDate = false;
	
	/**
	 * controls building the cell style
	 * @param innerStyleMap Styling for the workbook
	 * @param wb Excel workbook
	 */
	private CellStyle buildStyle(Map<String, Object> innerStyleMap, Workbook wb) {

		CellStyle style = wb.createCellStyle();

		setFont(style, innerStyleMap, wb);
		setBorder(style, innerStyleMap);
		setCellColor(style, innerStyleMap);

		return style;

	}

	/**
	 * sets sell color styles
	 * @param style
	 * @param innerStyleMap
	 */
	private void setCellColor(CellStyle style, Map<String, Object> innerStyleMap) {


		if(innerStyleMap.get(FILL_FOREGROUND_COLOR) != null){
			style.setFillForegroundColor((short) NumberUtil.toInt(innerStyleMap.get(FILL_FOREGROUND_COLOR) + ""));
		}
		
		if(innerStyleMap.get(FILL_PATTERN) != null){
			style.setFillPattern((FillPatternType)innerStyleMap.get(FILL_PATTERN));
		}

	}

	/**
	 * sets cell border styles
	 * @param style Cell style for the border
	 * @param headerMap Style map for the workbook
	 */
	private void setBorder(CellStyle style, Map<String, Object> innerStyleMap) {

		if(innerStyleMap.get(BORDER_BOTTOM) != null){
			style.setBorderBottom((BorderStyle)innerStyleMap.get(BORDER_BOTTOM));
		}

		if(innerStyleMap.get(BORDER_TOP) != null){
			style.setBorderTop((BorderStyle)innerStyleMap.get(BORDER_TOP));
		}

		if(innerStyleMap.get(BORDER_RIGHT) != null){
			style.setBorderRight((BorderStyle)innerStyleMap.get(BORDER_RIGHT));
		}

		if(innerStyleMap.get(BORDER_LEFT) != null){
			style.setBorderLeft((BorderStyle)innerStyleMap.get(BORDER_LEFT));
		}


		if(innerStyleMap.get(TEXT_WRAP) != null){
			style.setWrapText(true);
		}
	}

	/**
	 * sets font styles
	 * @param style Cell style for the border
	 * @param innerStyleMap Style map for the workbook
	 * @param wb excel workbook
	 */
	protected void setFont(CellStyle style, Map<String, Object> innerStyleMap, Workbook wb) {

		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		
		if(innerStyleMap.get(BOLD_WEIGHT) != null){
			font.setBold(BooleanUtil.toBoolean(innerStyleMap.get(BOLD_WEIGHT)));
		}
		
		if (innerStyleMap.get(FONT_COLOR)!= null){
			font.setColor(Short.parseShort(innerStyleMap.get(FONT_COLOR) + ""));
		}

		style.setFont(font);


	}

	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#getFooterStyle()
	 */
	@Override
	public CellStyle getTitleStyle(Workbook wb) {
		if (styleMap.containsKey(TITLE_MAP)){
			return buildStyle(styleMap.get(TITLE_MAP), wb);
		}else{
			return wb.createCellStyle();
		}
	}

	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#getBodyStyle()
	 */
	@Override
	public CellStyle getBodyStyle(Workbook wb) {
		if (styleMap.containsKey(BODY_MAP)){
			return buildStyle(styleMap.get(BODY_MAP), wb);
		}else{
			return wb.createCellStyle();
		}
	}


	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#getBodyStyle()
	 */
	@Override
	public CellStyle getDateStyle(Workbook wb) {
		CellStyle dateStyle;
		if (styleMap.containsKey(DATE_MAP)){
			dateStyle = buildStyle(styleMap.get(DATE_MAP), wb);
		} else {
			dateStyle = wb.createCellStyle();
		}
		
		dateStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));
		return dateStyle;
	}
	
	
	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#getHeadingStyle()
	 */
	@Override
	public CellStyle getHeadingStyle(Workbook wb) {
		if (styleMap.containsKey(HEADER_MAP)){
			return buildStyle(styleMap.get(HEADER_MAP), wb);
		} else {
			return wb.createCellStyle();
		}
	}

	/**
	 * @return the styleMap
	 */
	public Map<String, Map<String, Object>> getStyleMap() {
		return styleMap;
	}

	/**
	 * Sets the style map
	 * @param styleMap the styleMap to set
	 */
	public void setStyleMap(Map<String, Map<String, Object>> styleMap) {
		this.styleMap = styleMap;
	}
	
	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#getExpandColumnFlg()
	 */
	@Override
	public boolean getExpandColumnFlag() {
		return expandColumnFlg;
	}
	
	/*
	 * (non-javadoc)
	 * @see com.smt.data.report.ExcelStyleInterface#setExpandColumnFlag(boolean)
	 */
	@Override
	public void setExpandColumnFlag(boolean expandColumnFlg) {
		this.expandColumnFlg = expandColumnFlg;
	}

	/*
	 * (non-Javadoc)
	 * @see com.siliconmtn.data.report.ExcelStyleInterface#displayDate()
	 */
	@Override
	public boolean displayDate() {
		return displayDate;
	}
	
	/**
	 * Abstract method that builds the styles
	 */
	public abstract void buildStyleMap();

}
