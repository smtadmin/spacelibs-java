package com.siliconmtn.data.report;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: NoStyleCellStyle.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the No Call Style formatter for the excel report
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
class NoStyleCellStyleTest {

	/**
	 * Test method for {@link com.siliconmtn.data.report.NoStyleCellStyle#buildStyleMap()}.
	 */
	@Test
	void testBuildStyleMap() {
		NoStyleCellStyle ncs = new NoStyleCellStyle();
		ncs.buildStyleMap();
		assertEquals(0, ncs.getStyleMap().size());
	}

	/**
	 * Test method for {@link com.siliconmtn.data.report.NoStyleCellStyle#NoStyleCellStyle()}.
	 */
	@Test
	void testNoStyleCellStyle() {
		NoStyleCellStyle ncs = new NoStyleCellStyle();
		assertNotNull(ncs);
	}

	/**
	 * tests that the displayDate gets set
	 * @throws Exception
	 */
	@Test
	void testSetDisplayDate() throws Exception {
		NoStyleCellStyle ncs = new NoStyleCellStyle();
		ncs.setDisplayDate(false);
		assertFalse(ncs.displayDate);
	}
}
