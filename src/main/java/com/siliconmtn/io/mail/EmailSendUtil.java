package com.siliconmtn.io.mail;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.siliconmtn.data.text.StringUtil;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import jakarta.mail.MessagingException;

/**
 * <b>Title:</b> EmailSendUtil.java 
 * <b>Project:</b> Spacelibs
 * <b>Description:</b> Manages connecting to a mail server and sending it an email
 * Requires the following config values in the properties file:
 *  spring.mail.host
 *	spring.mail.port
 *	spring.mail.username
 *	spring.mail.password
 *	spring.mail.properties.mail.smtp.auth=true
 *	spring.mail.properties.mail.smtp.starttls.enable=true
 *  conf.default-email-from
 *
 * <b>Copyright:</b> 2024 
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Eric Damschroder
 * @version 1.0
 * @since Jan 31, 2024
 *
 */
@Log4j2
public class EmailSendUtil {
	
	@Value("${conf.default-email-from}")
	public String DEFAULT_FROM_EMAIL;
	
	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * Minimal email send method that uses the default from address,
	 * set in the properties at conf.default-email-from
	 * @param to
	 * @param subject
	 * @param content
	 * @throws MessagingException 
	 */
	public void sendEmail(String to, String subject, String content) throws MessagingException {
		EmailMessageVO email = new EmailMessageVO();
		email.addToRecipient(to);
		email.setSubject(subject);
		email.setMessage(new StringBuilder(content));
		sendEmail(email);
	}
	
	/**
	 * Convert the supplied EmailMessageVO to a MimeMessage and send it
	 * @param vo
	 * @throws MessagingException
	 */
	public void sendEmail(EmailMessageVO vo) throws MessagingException {
		// Use default from address if nothing is provided
		if (StringUtil.isEmpty(vo.getFromEmail())) 
			vo.setFromEmail(DEFAULT_FROM_EMAIL);
		log.info(vo);
		// Create our mime message and the helper that populates it
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
        	// Set the from and reply to.
            helper.setFrom(vo.getFromEmail());
	        helper.setReplyTo(vo.getReplyTo());

	        // Add all recipients to the email
	        for (String addr : vo.getTo()) helper.addTo(addr);
	        for (String addr : vo.getCC()) helper.addCc(addr);
	        for (String addr : vo.getBCC()) helper.addBcc(addr);
	        
	        // Set the subject and email content
	        helper.setSubject(vo.getSubject());
	        helper.setText(vo.getMessage().toString(), true);
	        
	        // Add all attachments
	        for (Entry<String, byte[]> a: vo.getAttachments().entrySet())
	            helper.addAttachment(a.getKey() , new ByteArrayResource(a.getValue()));
	        
	        // Send the message with the autowired mail sender
	        mailSender.send(message);
		} catch (MessagingException e) {
			// Log why the error failed and throw it again
		    log.error("Failed to send email", e);
		    throw e;
		}
	}

}
