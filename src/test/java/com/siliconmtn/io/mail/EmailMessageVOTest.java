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
		msg.addAttachment(new byte[5]);
		assertTrue(msg.hasAttachments());
	}

	@Test
	void getFromEmailDefaultTest() {
		EmailMessageVO msg = new EmailMessageVO();
		assertEquals(EmailMessageVO.DEFAULT_FROM_EMAIL, msg.getFromEmail());
	}

	@Test
	void getFromEmailTest() {
		EmailMessageVO msg = new EmailMessageVO();
		msg.setFromEmail("test@test.com");
		assertEquals("test@test.com", msg.getFromEmail());
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
		assertEquals(msg.getRecipients(RecipientType.TO)[0], "test@test.com");
	}

	@Test
	void addRecipientTestEmpty() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("", RecipientType.BCC));
		assertEquals(msg.getRecipients(RecipientType.TO).length, 0);
	}

	@Test
	void addRecipientTestNoType() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("", null));
		assertEquals(msg.getRecipients(RecipientType.TO).length, 0);
	}
	
	@Test
	void addRecipientTestValid() {
		EmailMessageVO msg = new EmailMessageVO();
		assertDoesNotThrow(() -> msg.addRecipient("test@test.com", RecipientType.BCC));
		assertEquals(msg.getRecipients(RecipientType.BCC).length, 1);
		assertEquals(msg.getRecipients(RecipientType.BCC)[0], "test@test.com");
	}
}
