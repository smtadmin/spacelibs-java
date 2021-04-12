package com.siliconmtn.io.image;

// JDK 11
import java.io.File;
import java.io.IOException;

// Space Libs 1.x
import com.siliconmtn.data.text.StringUtil;


/****************************************************************************
 * <b>Title</b>: ImageManipulator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Wrapper class that calls other classes that can transform
 * various aspects of an image.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 27, 2021
 * @updates:
 ****************************************************************************/
public class ImageManipulator {
	
	// Members
	private File image;
	
	/**
	 * Constructor requires the path to the image to be manipulated
	 * @param filePath Path to the file to be manipulated
	 * @throws IOException Error thrown trying to manipulate the image
	 */
	public ImageManipulator(String filePath) throws IOException {
		super();
		if (StringUtil.isEmpty(filePath)) throw new IOException("File path must be provided");
		this.image = new File(filePath);
	}

	/**
	 * Constructor requires the path to the image to be manipulated
	 * @param image Java file object with the path to the image
	 * @throws IOException Error thrown trying to rotate the image
	 */
	public ImageManipulator(File image) throws IOException {
		super();
		if (image == null || ! image.exists())  
			throw new IOException("File must exist");
		
		this.image = image;
	}
	
	/**
	 * Rotates the image
	 * @param degrees number of degrees to rotate in a clockwise direction
	 * @return rotated image in a byte array
	 * @throws IOException Error thrown trying to rotate the image
	 */
	public byte[] rotateImage(int degrees) throws IOException {
		Rotator ir = new Rotator(image);
		return ir.rotate(degrees);
	}
}
