package com.siliconmtn.data.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

/****************************************************************************
 * <b>Title:</b> PublishEventTest.java <br>
 * <b>Project:</b> spacelibs-java <br>
 * <b>Description:</b> Contains tests for the PublishEvent Class<br>
 * <b>Copyright:</b> Copyright (c) 2023 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Billy Larsen
 * @version 1.x
 * @since Feb 14, 2023
 *        <b>updates:</b>
 * 
 ***************************************************************************
 */
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
