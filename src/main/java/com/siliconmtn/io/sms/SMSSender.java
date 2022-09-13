package com.siliconmtn.io.sms;

import com.siliconmtn.io.mail.SMSMessageVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SMSSender {
	public abstract SMSMessageVO sendMessage(SMSMessageVO msg);
}
