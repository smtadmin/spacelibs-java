package com.siliconmtn.io.sms;

import javax.naming.ConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.api.EndpointRequestException;

public class SMSFactory {

	@Autowired
	protected AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	@Autowired
    private ApplicationContext applicationContext;
	
	/**
	 * Checks the parserMapper property in the application's config file for the parser associated with
	 * the passed classname.methodname key.
	 * @param controllerName the classname.methodname combo key for the parser we are looking for
	 * @param attributes map of attributes from endpoint other than the body
	 * @return ParserIntfc that will be used to parse the request body into ValidationDTOs
	 * @throws ConfigurationException 
	 * @throws EndpointRequestException When unable to create an instance of the controller name
	 */
	public SMSSender smsDispatcher(String beanName) throws ConfigurationException {
		
		if(StringUtil.isEmpty(beanName)) {
			return null;
		}
        try {
            SMSSender parser = (SMSSender) applicationContext.getBean(beanName);
            autowireCapableBeanFactory.autowireBean(parser);
            return parser;
        } catch (Exception e) {
            throw new ConfigurationException();
        }
    }
}
