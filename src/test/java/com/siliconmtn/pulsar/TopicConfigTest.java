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
}
