package com.siliconmtn.io.sms;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.siliconmtn.io.http.SMTHttpConnectionManager;
import com.siliconmtn.io.mail.SMSMessageVO;

@ExtendWith(MockitoExtension.class)
class BulkSMSSenderTest {

	@Mock
	SMTHttpConnectionManager mgr;

	@Mock
	BulkSMSConfig config;

	@InjectMocks
	BulkSMSSender sender;
	
	@Test
	void testNullSMS() {
		sender.config = config;
		sender.mgr = mgr;
		assertNull(sender.sendMessage(null));
	}
	
	@Test
	void testSendMessage() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("12606020305");
		msg.setMessage("Hello World Test");
		sender.config = config;
		sender.mgr = mgr;
		SMSMessageVO msgRes = sender.sendMessage(msg);
		assertNotNull(msgRes);
	}
	
	@Test
	void testSendNoConfigMessage() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("12606020305");
		msg.setMessage("Hello World Test");
		sender.mgr = mgr;
		SMSMessageVO msgRes = sender.sendMessage(msg);
		assertNotNull(msgRes);
	}
	
	@Test
	void testSendNoMgrMessage() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("12606020305");
		msg.setMessage("Hello World Test");
		sender.config = config;
		SMSMessageVO msgRes = sender.sendMessage(msg);
		assertNotNull(msgRes);
	}
}
