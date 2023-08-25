package com.siliconmtn.data.parser;

// JDK 11.x
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

// Space Libs 1.x
import com.siliconmtn.data.format.DateFormat.DatePattern;

/****************************************************************************
 * <b>Title</b>: TestDataBeanVO.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> This class is used for testing only
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 28, 2021
 * @updates:
 ****************************************************************************/
public class TestDataBeanVO extends BeanDataVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Members
	private String name;
	private int someNumber;
	private List<String> names = new ArrayList<>();
	private String[] arrNames;
	private DatePattern datePattern;
	private Date currDate;
	
	/**
	 * 
	 */
	public TestDataBeanVO() {
		super();
	}

	/**
	 * @param req
	 */
	public TestDataBeanVO(HttpServletRequest req) {
		super(req);
		super.populateData(req);
	}
	
	/**
	 * @param req
	 */
	public TestDataBeanVO(HttpServletRequest req, String suffix) {
		super(req);
		super.populateData(req, suffix);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the someNumber
	 */
	public int getSomeNumber() {
		return someNumber;
	}

	/**
	 * @return the names
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * @return the arrNames
	 */
	public String[] getArrNames() {
		return arrNames;
	}

	/**
	 * @return the datePattern
	 */
	public DatePattern getDatePattern() {
		return datePattern;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param someNumber the someNumber to set
	 */
	public void setSomeNumber(int someNumber) {
		this.someNumber = someNumber;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}

	/**
	 * @param arrNames the arrNames to set
	 */
	public void setArrNames(String[] arrNames) {
		this.arrNames = arrNames;
	}

	/**
	 * @param datePattern the datePattern to set
	 */
	public void setDatePattern(DatePattern datePattern) {
		this.datePattern = datePattern;
	}

	/**
	 * @return the currDate
	 */
	public Date getCurrDate() {
		return currDate;
	}

	/**
	 * @param currDate the currDate to set
	 */
	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

}
