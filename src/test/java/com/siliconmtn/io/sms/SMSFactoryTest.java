package com.siliconmtn.io.sms;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.naming.ConfigurationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.siliconmtn.io.mail.SMSMessageVO;

@ExtendWith(SpringExtension.class)
class SMSFactoryTest {

	@InjectMocks
	private SMSFactory smsFactory;
		
	/**
	 * Test the parser factory to ensure it functions as desired
	 * @throws Exception
	 */
	@Test
	void testParserFactory() throws Exception {
		SMSFactory fact = new SMSFactory();
		fact.autowireCapableBeanFactory = mock(AutowireCapableBeanFactory.class);
		
		TestSender t = new TestSender();

		ApplicationContext ctx = mock(ApplicationContext.class);
		when(ctx.getBean(TestSender.class.getCanonicalName())).thenReturn(t);
		when(ctx.getBean("com.fake.class.fakeMethod")).thenThrow(NoSuchBeanDefinitionException.class);

		ReflectionTestUtils.setField(fact, "applicationContext", ctx);

		assertNull(fact.smsDispatcher(null));

		assertNotNull(fact.smsDispatcher(TestSender.class.getCanonicalName()));
		assertThrows(ConfigurationException.class, () -> fact.smsDispatcher("com.fake.class.fakeMethod"));
		
	}
	
	class TestSender extends SMSSender {
		@Override
		public SMSMessageVO sendMessage(SMSMessageVO msg) {return msg;}
	}
}
