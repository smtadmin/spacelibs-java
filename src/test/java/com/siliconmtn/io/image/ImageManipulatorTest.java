package com.siliconmtn.io.image;

//Junit 5
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// JDK 11
import java.io.File;
import java.io.IOException;

/****************************************************************************
 * <b>Title</b>: ImageManipulatorTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> tests the image manipulation class
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 27, 2021
 * @updates:
 ****************************************************************************/
class ImageManipulatorTest {
	public static final String RESOURCE = "/images/image_rotate_test.jpg";
	
	// Members
	private String testImagePath;
	
	@BeforeEach
	void setUpBeforeEach() throws Exception {
		testImagePath = this.getClass().getResource(RESOURCE).getFile();
	}
	
	/**
	 * Test method for {@link com.siliconmtn.io.image.ImageManipulator#ImageManipulator(java.lang.String)}.
	 */
	@Test
	void testImageManipulatorString() throws Exception {
		assertDoesNotThrow(() -> {
			new ImageManipulator(RESOURCE);
		});
		
		assertThrows(IOException.class, () -> {
			File nullFile = null;
			new ImageManipulator(nullFile);
		});
		
		assertThrows(IOException.class, () -> {
			File f = new File("/some/bad/path/to/image.jpg");
			new ImageManipulator(f);
		});
		
		assertThrows(IOException.class, () -> {
			String nullFile = null;
			new ImageManipulator(nullFile);
		});
	}

	/**
	 * Test method for {@link com.siliconmtn.io.image.ImageManipulator#ImageManipulator(java.io.File)}.
	 */
	@Test
	void testImageManipulatorFile() throws Exception {
		assertDoesNotThrow(() -> {
			new ImageManipulator(new File(testImagePath));
		});
	}

	/**
	 * Test method for {@link com.siliconmtn.io.image.ImageManipulator#rotateImage(int)}.
	 */
	@Test
	void testRotateImageInt() throws Exception {
		ImageManipulator im = new ImageManipulator(new File(testImagePath));
		byte[] image = im.rotateImage(90);
		
		assertDoesNotThrow(() -> {
			im.rotateImage(90);
		});
		
		assertNotNull(image);
		assertTrue(image.length > 0);
	}
}
