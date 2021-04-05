package com.siliconmtn.io;

// JDK 11.x
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// JEE 7.x
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/****************************************************************************
 * <b>Title:</b> ServletOutputStreamMock.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> This is a mock of the JEE ServletOutputStream and is intended
 * to be used when testing the response object and the output stream is needed.  This
 * class extends the ServletOutputStream and wraps around a ByteArrayOutputStream
 * to hold and store the data
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 31, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class ServletOutputStreamMock extends ServletOutputStream {
	
	// Members
	private ByteArrayOutputStream baos;
	protected WriteListener listener = null;
	
	/**
	 * Constructs this wrapper class with an empty output stream
	 */
	public ServletOutputStreamMock() {
		this(new ByteArrayOutputStream());
	}
	
	/**
	 * Wraps the ByteArrayOutputStream into a ServletOutputStream
	 * @param baos ByeArray to load into the data
	 */
	public ServletOutputStreamMock(ByteArrayOutputStream baos) {
		super();
		this.baos = baos;
	}

	/* (non-javadoc)
	 * @see javax.servlet.ServletOutputStream#isReady()
	 */
	@Override
	public boolean isReady() {
		return false;
	}
	
	/**
	 * Returns the current size of the buffer.
	 * @return Size of the buffer in integers
	 */
	public int size() {
		if (baos == null || baos.toByteArray() == null) return 0;
		else return baos.toByteArray().length;
	}

	/* (non-javadoc)
	 * @see javax.servlet.ServletOutputStream#setWriteListener(javax.servlet.WriteListener)
	 */
	@Override
	public void setWriteListener(WriteListener listener) {
		this.listener = listener;
	}

	/* (non-javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		baos.write(b);
	}

	/**
	 * Writes len bytes from the specified byte array starting at offset off to this ByteArrayOutputStream.
	 * @param b
	 * @param off the start offset in the data.
	 * @param len the number of bytes to write.
	 */
	public void write​(byte[] b, int off, int len) {
		baos.write(b, off, len);
	}
	
	/**
	 * Writes the complete contents of the specified byte array to this ByteArrayOutputStream.
	 * @param b
	 * @throws IOException
	 */
	public void write​(byte[] b) throws IOException {
		baos.writeBytes(b);
	}
	
	/**
	 * Writes the complete contents of the specified byte array to this ByteArrayOutputStream.
	 * @param b The Data
	 * @throws IOException if b is null or can't write the data
	 */
	public void writeBytes(byte[] b) throws IOException {
		baos.writeBytes(b);
	}
	
	/**
	 * Creates a newly allocated byte array. Its size is the current size of this 
	 * output stream and the valid contents of the buffer have been copied into it.
	 * @return the current contents of this output stream, as a byte array.
	 */
	public byte[] toByteArray() {
		return baos.toByteArray();
	}
	
	/*
	 * (non-javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (baos.toByteArray() == null) return "";
		return new String(baos.toByteArray());
	}
}
