package com.siliconmtn.io.sms;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	public void setup() throws IOException{
//		config = new BulkSMSConfig("smtsms", "smtrul3s", "http://usa.bulksms.com:5567/eapi/submission/send_sms/2/2.0");
//		sender = new BulkSMSSender();
//		mgr = mock(SMTHttpConnectionManager.class);
//		when(mgr.convertPostData(anyMap())).thenReturn("data".getBytes());
//		when(mgr.getRequestData(eq(config.smsurl), anyMap(), eq(HttpConnectionType.POST))).thenReturn("0|IN_PROGRESS|2163000027\n".getBytes());
	}
	
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
