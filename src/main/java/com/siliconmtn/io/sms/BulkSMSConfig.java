package com.siliconmtn.io.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "smsconfig.bulksms")
@ConfigurationPropertiesScan
@PropertySource("classpath:application.properties")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BulkSMSConfig implements SMSConfig {
	String username;
	String password;
	String smsurl;
}
