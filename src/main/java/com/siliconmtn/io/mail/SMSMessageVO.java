package com.siliconmtn.io.mail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/****************************************************************************
 * <b>Title</b>: SMSMessageVO.java<p/>
 * <b>Description: Holds the variables unique to an sms message.
 * By extending MessageVO it can be dropped to a JMS queue as is.</b> 
 * <p/>
 * <b>Copyright:</b> Copyright (c) 2013<p/>
 * <b>Company:</b> Silicon Mountain Technologies<p/>
 * @author Eric Damschroder
 * @version 1.0
 * @since Mar 6, 2013
 ****************************************************************************/

@Getter
@Setter
public class SMSMessageVO implements Serializable {
	private static final long serialVersionUID = -7597493301787479973L;
	private String phoneNumber;
	private String message;
	private Object result;
}
