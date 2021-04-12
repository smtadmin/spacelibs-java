package com.siliconmtn.io;

// JDK 11.x
import java.io.BufferedOutputStream;
import java.io.IOException;

// JEEE 8.x
import javax.servlet.http.HttpServletResponse;

// SpaceLibs 1.x
import com.siliconmtn.data.report.AbstractReport;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: StreamUtil.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Stream utilities that wrap common stream activities 
 * into a series of helper methods
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Feb 17, 2021
 * @updates:
 ****************************************************************************/
public class StreamUtil {

	/**
	 * 
	 */
	public StreamUtil() {
		super();
	}

	/**
	 * Streams a generic report back to the client.  
	 * This is basically a helper method for getting reports off the server in a generic way.
	 * @param report The report to stream back to the user
	 * @param response Standard HttpServletResponse object
	 * @throws IOException Error thrown trying to Stream the report
	 */
	public final void streamReport(AbstractReport report, HttpServletResponse response) throws IOException {

		// Get the binary document
		byte[] b = report.generateReport();
		
		//set content type if passed
		response.setContentType(report.getContentType());

		// Set the name of the file if it is set.  Add the header tags for the name and type
		if (! StringUtil.isEmpty(report.getFileName())) {
			String attachment = (report.isHeaderAttachment()) ? "attachment; " : "";
			response.setHeader("Content-Disposition", attachment + "filename=\"" + report.getFileName() + "\"");
		}
		response.setHeader("Cache-Control",  "private, max-age=0"); //do not add "no-cache" here, IE 8 hates it!

		// Set the length of the content
		response.setContentLength(b.length);
		
		// Spool the data back
		try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
			bos.write(b);
		}
	}
}
