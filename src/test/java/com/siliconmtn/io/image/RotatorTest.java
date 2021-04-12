package com.siliconmtn.io.image;

//Junit 5
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title</b>: RotatorTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> CHANGE ME!!
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 27, 2021
 * @updates:
 ****************************************************************************/
class RotatorTest {

	/**
	 * Test method for {@link com.siliconmtn.io.image.Rotator#Rotator(java.io.File)}.
	 */
	@Test
	void testRotator() throws Exception {
		Rotator r = new Rotator(new File("/hello.png"));
		assertNotNull(r);
	}

	/**
	 * Test method for {@link com.siliconmtn.io.image.Rotator#getImageType()}.
	 */
	@Test
	void testGetImageType() throws Exception {
		Rotator r = new Rotator(new File("/hello.png"));
		assertEquals("png", r.getImageType());
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.image.Rotator#getImageType()}.
	 */
	@Test
	void testGetImageTypeNoExt() throws Exception {
		Rotator r = new Rotator(new File("/hello"));
		assertEquals("jpg", r.getImageType());
	}

}
