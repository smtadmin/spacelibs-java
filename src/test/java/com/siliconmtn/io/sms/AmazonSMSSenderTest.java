package com.siliconmtn.io.sms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.siliconmtn.io.mail.SMSMessageVO;

import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

class AmazonSMSSenderTest {

	SnsClient client;
	AmazonSMSConfig config;
	PublishRequest req;
	PublishResponse res;
	SdkHttpResponse httpRes;
	
	@BeforeEach
	public void setup(){
		client = mock(SnsClient.class);
		req = mock(PublishRequest.class);
		res = mock(PublishResponse.class);
		httpRes = mock(SdkHttpResponse.class);

		config = new AmazonSMSConfig(Region.US_GOV_WEST_1.id(), "keyId", "secret");
	}
	
	@Test
	void testNullSMS() {
		AmazonSMSSender sender = new AmazonSMSSender();
		sender.config = config;
		assertNull(sender.sendMessage(null));
	}
	
	@Test
	void testSendMessage() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("1231231234");
		msg.setMessage("Hello World");

		AmazonSMSSender sender = new AmazonSMSSender();
		sender.config = config;
		SMSMessageVO msgRes = sender.sendMessage(msg);
		assertNotNull(msgRes);
	}
	
	@Test
	void testSendNoAttributesMessage() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("1231231234");
		msg.setMessage("Hello World");

		AmazonSMSSender sender = new AmazonSMSSender();
		SMSMessageVO msgRes = sender.sendMessage(msg);
		assertNotNull(msgRes);
	}
	
	@Test
	void testSendMessageWithClient() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("1231231234");
		msg.setMessage("Hello World");

		when(client.publish(any(PublishRequest.class))).thenReturn(res);
		when(res.sdkHttpResponse()).thenReturn(httpRes);
		when(httpRes.statusCode()).thenReturn(200);
		AmazonSMSSender sender = new AmazonSMSSender();
		sender.config = config;
		SMSMessageVO msgRes = sender.sendMessage(msg, client);
		assertNotNull(msgRes);
	}
	
	@Test
	void testSendMessageWithClientNullMessage() {
		AmazonSMSSender sender = new AmazonSMSSender();
		sender.config = config;
		SMSMessageVO msgRes = sender.sendMessage(null, client);
		assertNull(msgRes);
	}
	
	@Test
	void testSendMessageWithClientNullClient() {
		SMSMessageVO msg = new SMSMessageVO();
		msg.setPhoneNumber("1231231234");
		msg.setMessage("Hello World");

		AmazonSMSSender sender = new AmazonSMSSender();
		sender.config = config;
		SMSMessageVO msgRes = sender.sendMessage(msg, null);
		assertEquals(msgRes, msg);
		assertNull(msg.getResult());
	}
}
