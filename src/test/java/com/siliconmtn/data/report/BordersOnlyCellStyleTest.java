package com.siliconmtn.data.report;

// Junit 5
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: BordersOnlyCellStyleTest.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Tests the border styling fo rthe excel conversion
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 14, 2021
 * @updates:
 ****************************************************************************/
class BordersOnlyCellStyleTest {

	@Test
	void testBuildStyleMap() {
		BordersOnlyCellStyle ncs = new BordersOnlyCellStyle();
		ncs.buildStyleMap();
		assertEquals(3, ncs.getStyleMap().size());
	}

	@Test
	void testBordersOnlyCellStyle() {
		BordersOnlyCellStyle ncs = new BordersOnlyCellStyle();
		assertNotNull(ncs);
	}

	/**
	 * tests that the displayDate gets set
	 * @throws Exception
	 */
	@Test
	void testSetDisplayDate() throws Exception {
		BordersOnlyCellStyle ncs = new BordersOnlyCellStyle();
		ncs.setDisplayDate(false);
		assertFalse(ncs.displayDate);
	}

}
