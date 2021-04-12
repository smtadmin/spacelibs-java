package com.siliconmtn.io.image;

//JDK 11.x
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/****************************************************************************
 * <b>Title</b>: Rotator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> This class rotates an image by the provided degrees
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 27, 2021
 * @updates:
 ****************************************************************************/
class Rotator {
	
	// Members
	private File image;

	/**
	 * 
	 */
	Rotator(File image) {
		super();
		this.image = image;
	}
	
	/**
	 * Rotates an image
	 * @param degrees number of degrees to rotate in a clockwise direction
	 * @return
	 * @throws IOException Error if image can't be converted
	 */
	byte[] rotate(int degrees) throws IOException {
		
		BufferedImage bi = ImageIO.read(image);
		BufferedImage rotatedImage = performRotation(bi, degrees);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(rotatedImage, getImageType(), baos);
		return baos.toByteArray();
	}
	
	/**
	 * Performs the actual image rotation
	 * @param sourceImage
	 * @param degrees
	 * @return
	 */
	private BufferedImage performRotation(BufferedImage sourceImage, int degrees) {
		final double rads = Math.toRadians(degrees);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(sourceImage.getWidth() * cos + sourceImage.getHeight() * sin);
		final int h = (int) Math.floor(sourceImage.getHeight() * cos + sourceImage.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, sourceImage.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2.0, h / 2.0);
		at.rotate(rads,0, 0);
		at.translate(-sourceImage.getWidth() / 2.0, -sourceImage.getHeight() / 2.0);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(sourceImage,rotatedImage);
		
		return rotatedImage;
	}
	
	/**
	 * The the image extension from the file name.   Image extension.  "jpg" if not provided
	 * @return
	 */
	protected String getImageType() {
		
		String fileName = image.getName();
		if (fileName.contains(".")) return fileName.substring(fileName.indexOf('.') + 1);
		return "jpg";
	}

}
