package com.siliconmtn.pulsar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TopicConfigTest {

	@Test
	void getUniqueSubscriptionNameTest() {
		TopicConfig config = new TopicConfig();
		config.setSubscriptionName("test");
		assertNotEquals("test", config.getUniqueSubscriptionName());
		assertTrue(config.getUniqueSubscriptionName().startsWith("test"));
		assertEquals(37, config.getUniqueSubscriptionName().length());
	}

	@Test
	void getSiloedTopicNoSilo() {
		TopicConfig config = new TopicConfig();
		config.setTopicUri("tenant/namespace/topic");
		assertEquals(config.getTopicUri(), config.getSiloedTopic(null));
		assertEquals(config.getTopicUri(), config.getSiloedTopic(""));
		assertEquals(config.getTopicUri(), config.getSiloedTopic(null, "_"));
		assertEquals(config.getTopicUri(), config.getSiloedTopic(null, null));
	}

	@Test
	void getSiloedTopicWithSilo() {
		TopicConfig config = new TopicConfig();
		config.setTopicUri("tenant/namespace/topic");
		String silo = "test";
		String siloedTopic = config.getSiloedTopic(silo);
		assertNotEquals(config.getTopicUri(), siloedTopic);
		assertTrue(siloedTopic.startsWith(config.getTopicUri()));
		assertTrue(siloedTopic.endsWith(silo));
		assertEquals(TopicConfig.DEFAULT_SEPARATOR, siloedTopic.replaceAll(config.getTopicUri(), "").replaceAll(silo, ""));
	}

	@Test
	void getSiloedTopicWithSiloAndSepartor() {
		TopicConfig config = new TopicConfig();
		config.setTopicUri("tenant/namespace/topic");
		String sep = "--";
		String silo = "test";
		String siloedTopic = config.getSiloedTopic(silo, sep);
		assertNotEquals(config.getTopicUri(), siloedTopic);
		assertTrue(siloedTopic.startsWith(config.getTopicUri()));
		assertTrue(siloedTopic.endsWith(silo));
		assertEquals(sep, siloedTopic.replaceAll(config.getTopicUri(), "").replaceAll(silo, ""));
	}
}
