package com.siliconmtn.io.mail;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.siliconmtn.io.mail.EmailMessageVO.RecipientType;

class EmailMessageVOTest {

	@Test
	void hasAttachmentsTest() {
		EmailMessageVO msg = new EmailMessageVO();
		assertFalse(msg.hasAttachments());
	}

	@Test
	void hasAttachmentsTrueTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.addAttachment("file", new byte[5]);
		assertTrue(msg.hasAttachments());
	}

	@Test
	void getFromEmailTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.setFromEmail("test@test.com");
		assertEquals("test@test.com", msg.getFromEmail());
	}

	@Test
	void getFromNoReplyTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.setFromEmail("test@test.com");
		assertEquals("test@test.com", msg.getReplyTo());
	}

	@Test
	void getReplyTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.setReplyTo("test@test.com");
		assertEquals("test@test.com", msg.getReplyTo());
	}

	@Test
	void addRecipientsNullTest() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipients(null, null));
	}

	@Test
	void addRecipientsEmptyTest() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipients(new ArrayList<>(), null));
	}

	@Test
	void addRecipientsNullTypeTest() {
		List<String> recps = new ArrayList<>();
		recps.add("test@test.com");
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipients(recps, null));
	}

	@Test
	void addRecipientsValidTypeTest() {
		List<String> recps = new ArrayList<>();
		recps.add("test@test.com");
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipients(recps, RecipientType.BCC));
		assertEquals(msg.getRecipients(RecipientType.BCC).length, recps.size());
		assertEquals(msg.getRecipients(RecipientType.BCC)[0], recps.get(0));
	}

	@Test
	void addRecipientTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.addRecipient("test@test.com");
		assertEquals("test@test.com", msg.getRecipients(RecipientType.TO)[0]);
	}

	@Test
	void addRecipientTestEmpty() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("", RecipientType.BCC));
		assertEquals(0, msg.getRecipients(RecipientType.TO).length);
	}

	@Test
	void addRecipientTestNoType() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("", null));
		assertEquals(0, msg.getRecipients(RecipientType.TO).length);
	}
	
	@Test
	void addRecipientTestValid() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("test@test.com", RecipientType.BCC));
		assertEquals(1, msg.getRecipients(RecipientType.BCC).length);
		assertEquals("test@test.com", msg.getRecipients(RecipientType.BCC)[0]);
	}

	@Test
	void addRecipientsTest() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addToRecipient("test@test.com"));
		assertDoesNotThrow(() -> msg.addCCRecipient("test@test.com"));
		assertDoesNotThrow(() -> msg.addBCCRecipient("test@test.com"));
		assertEquals(1, msg.getTo().length);
		assertEquals(1, msg.getCC().length);
		assertEquals(1, msg.getBCC().length);
	}
	
}
