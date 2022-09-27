package com.siliconmtn.pulsar;

import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ConfigurationPropertiesScan
@Configuration
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TopicConfig {

	private String topicUri;
	private String key;
	private String name;
	private String subscriptionName;

	public String getUniqueSubscriptionName() {
		return String.format("%s-%s", subscriptionName, UUID.randomUUID().toString().replace("-", ""));
	}
}
