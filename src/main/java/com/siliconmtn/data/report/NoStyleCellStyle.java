package com.siliconmtn.data.report;

// JDK 11.x
import java.util.HashMap;
import java.util.Map;

/****************************************************************************
 * <b>Title</b>: NoStyleCellStyle.java 
 * <b>Project</b>: SpaceLibs-Java 
 * <b>Description: </b> produces an empty map, this allows the factory to produce
 * an empty style.  
 * 
 * <b>Copyright:</b> Copyright (c) 2016
 * <b>Company:</b> Silicon Mountain Technologies
 * @author James Camire
 * @version 1.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
public class NoStyleCellStyle extends AbstractExcelCellStyle {
	
	public NoStyleCellStyle(){
		super();
		this.expandColumnFlg = false;
		buildStyleMap();
	}

	/* (non-Javadoc)
	 * @see com.siliconmtn.data.report.AbstractExcelCellStyle#buildStyleMap()
	 */
	@Override
	public void buildStyleMap() {
		//empty map sent intentionally
		Map<String, Map<String, Object>> styleMap = new HashMap<>();
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
