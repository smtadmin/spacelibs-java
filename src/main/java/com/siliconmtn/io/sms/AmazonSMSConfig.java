package com.siliconmtn.io.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "smsConfig.amazonSMS") 
@ConfigurationPropertiesScan
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AmazonSMSConfig implements SMSConfig {

	private String awsRegion;
	private String accessKeyId;
	private String secretAccessKey;
}
