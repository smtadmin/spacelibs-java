package com.siliconmtn.io.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siliconmtn.io.http.SMTHttpConnectionManager;
import com.siliconmtn.io.http.SMTHttpConnectionManager.HttpConnectionType;
import com.siliconmtn.io.mail.SMSMessageVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BulkSMSSender extends SMSSender {

	enum BulkSMSAttribute {username, password, message, msisdn, smsurl}

	@Autowired
	BulkSMSConfig config;

	@Autowired
	SMTHttpConnectionManager mgr;

	@Override
	public SMSMessageVO sendMessage(SMSMessageVO msg) {

		if(msg != null && config != null && mgr != null) {
			Map<String, Object> smsParams = new HashMap<>();
			
			smsParams.put(BulkSMSAttribute.username.name(), config.getUsername());
			smsParams.put(BulkSMSAttribute.password.name(), config.getPassword());
			smsParams.put(BulkSMSAttribute.msisdn.name(), msg.getPhoneNumber());
			smsParams.put(BulkSMSAttribute.message.name(), msg.getMessage());

			try {
				byte[] data = mgr.getRequestData(config.getSmsurl(), mgr.convertPostData(smsParams), HttpConnectionType.POST);
				if(data != null) {
					msg.setResult(new String(data));
				}
			} catch (IOException e) {
				msg.setResult(e);
				log.error("Unable to send SMS Message", e);
			}
		}
		return msg;
	}

}
