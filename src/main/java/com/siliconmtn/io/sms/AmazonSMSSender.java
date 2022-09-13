package com.siliconmtn.io.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siliconmtn.io.aws.AWSSnsManager;
import com.siliconmtn.io.mail.SMSMessageVO;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Log4j2
@Service
public class AmazonSMSSender extends SMSSender {
	public enum AWSSMSAttribute {awsRegion, accessKeyId, secretAccessKey}

	@Autowired
	AmazonSMSConfig config;

	@Override
	public SMSMessageVO sendMessage(SMSMessageVO msg) {
		if(msg != null) {
			try {
				SnsClient snsClient = new AWSSnsManager(config.getAccessKeyId(), config.getSecretAccessKey(), Region.of(config.getAwsRegion())).buildClient();
	            msg = sendMessage(msg, snsClient);
	        } catch (SnsException e) {
	            log.error(e.awsErrorDetails().errorMessage());
	            msg.setResult(e);
	        } catch(Exception e) {
	        	log.error(e.getMessage());
	            msg.setResult(e);
	        }
		}
		return msg;
	}

	public SMSMessageVO sendMessage(SMSMessageVO msg, SnsClient snsClient) {
		if(msg != null && snsClient != null) {
			PublishRequest request = PublishRequest.builder()
	            .message(msg.getMessage())
	            .phoneNumber(msg.getPhoneNumber())
	            .build();
	
			PublishResponse result = snsClient.publish(request);
	        log.debug(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
	        msg.setResult(result);
		}
		return msg;
	}
}
