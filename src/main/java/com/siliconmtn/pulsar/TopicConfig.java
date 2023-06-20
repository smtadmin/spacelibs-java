package com.siliconmtn.pulsar;

import java.util.UUID;

import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import com.siliconmtn.data.text.StringUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/****************************************************************************
 * <b>Title:</b> TopicConfig.java <br>
 * <b>Project:</b> spacelibs-java <br>
 * <b>Description:</b> Manages Topic Config for Pulsar Endpoints.<br>
 * <b>Copyright:</b> Copyright (c) 2022 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Billy Larsen
 * @version 1.0
 * @since Jun 14, 2023
 * <b>updates:</b>
 * Added support for keyed endpoint configuration
 * 
 ****************************************************************************/
@ConfigurationPropertiesScan
@Configuration
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TopicConfig {

	public static final String DEFAULT_SEPARATOR = "_";
	private String topicUri;
	private String key;
	private String name;
	private String subscriptionName;

	/**
	 * Helper method that returns a unique Subscription name.  Topic Subs must have unique names in Pulsar.
	 * @return
	 */
	public String getUniqueSubscriptionName() {
		return String.format("%s-%s", subscriptionName, UUID.randomUUID().toString().replace("-", ""));
	}

	/**
	 * Helper method for simplifying a siloed topic URL Request.  Utilizes {DEFAULT_SEPARATOR} as the 
	 * separator between topic and silo.
	 * @return
	 */
	public String getSiloedTopic(String silo) {
		return getSiloedTopic(silo, DEFAULT_SEPARATOR);
	}

	/**
	 * Handles logic for appending topicUri, separator and silo.  If silo is empty or null, returns just
	 * the topicUri.  Separator is defaulted to {DEFAULT_SEPARATOR} if not specified.
	 * 
	 * @return
	 */
	public String getSiloedTopic(String silo, String separator) {
		StringBuilder topic = new StringBuilder(100);
		topic.append(topicUri);
		if(!StringUtils.isEmpty(silo)) {
			topic.append(StringUtil.defaultString(separator, DEFAULT_SEPARATOR)).append(StringUtil.defaultString(silo));
		}

		return topic.toString();
	}
}
