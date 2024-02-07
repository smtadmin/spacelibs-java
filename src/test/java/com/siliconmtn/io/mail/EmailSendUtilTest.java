package com.siliconmtn.io.mail;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(SpringExtension.class)
public class EmailSendUtilTest {

	@Mock
	JavaMailSender mailSender;
	
	@Mock
	MimeMessage mime;
	
	@InjectMocks
	EmailSendUtil util;
	
	
	@BeforeEach
	public void before() {
		Mockito.when(mailSender.createMimeMessage()).thenReturn(mime);
		util.DEFAULT_FROM_EMAIL = "test@test.com";
	}
	
	@Test
	public void exceptionTest() throws MessagingException {
		doThrow(new MessagingException()).when(mime).setReplyTo(ArgumentMatchers.any());
		assertThrows(MessagingException.class, () -> util.sendEmail("","",""));
	}
	
	@Test
	public void testSimple() {
		assertDoesNotThrow(()->util.sendEmail("other@test.com", "Test Email", "This is a test"));
	}
	
	@Test
	public void testComplex() {
		EmailMessageVO mail = new EmailMessageVO();
		mail.setFromEmail("test@test.com");
		mail.setReplyTo("other@test.com");
		mail.setMessage(new StringBuilder("This is a test"));
		mail.setSubject("Test Email");
		mail.addToRecipient("me@test.com");
		mail.addCCRecipient("you@test.com");
		mail.addBCCRecipient("them@test.com");
		mail.addAttachment("file", "test".getBytes());
		
		assertDoesNotThrow(()->util.sendEmail(mail));
	}
}
