package com.siliconmtn.data.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

class PublishEventTest {

	@Test
	void PublisherEventConstructor() {
		Object src = "Test Obj";
		PublisherEvent<String> event = new PublisherEvent<String>(src, true, true, Collections.emptyList());
		assertNotNull(event);
		assertEquals(event.getSource(), src);
		assertTrue(event.isSuccessful());
		assertTrue(event.isTimeout());
		assertNotNull(event.getPayload());
	}
}
