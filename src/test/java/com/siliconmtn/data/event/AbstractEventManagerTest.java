package com.siliconmtn.data.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AbstractEventManagerTest {

	@Mock
	ApplicationEventPublisher publisher;

	@Captor
	ArgumentCaptor<PublisherEvent<String>> captor;

	class BaseEventManager extends AbstractEventManager<String> {

		public BaseEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher, Collection<String> data,
				long timeoutInMilliseconds) {
			super(applicationEventPublisher, data, timeoutInMilliseconds);
		}

		public BaseEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher,
				@NotNull Collection<String> data) {
			super(applicationEventPublisher, data);
		}

		public BaseEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher,
				long timeoutInMilliseconds) {
			super(applicationEventPublisher, timeoutInMilliseconds);
		}

		protected BaseEventManager(@NotNull ApplicationEventPublisher applicationEventPublisher) {
			super(applicationEventPublisher);
		}

		@Override
		public boolean validateData() {
			return this.data.size() == 2;
		}
	}

	@Test
	void defaultConstructorNull() {
		assertThrows(IllegalArgumentException.class, () -> new BaseEventManager(null));
	}

	@Test
	void defaultConstructor() {
		assertDoesNotThrow(() -> new BaseEventManager(publisher));
	}
	
	@Test
	void defaultConstructorWithTimeout() {
		int timeout = 12;
		BaseEventManager bem = new BaseEventManager(publisher, timeout);
		assertDoesNotThrow(() -> bem);
		assertEquals(timeout, bem.getTimeoutInMilliseconds());
	}

	@Test
	void defaultConstructorWithList() {
		List<String> strings = new ArrayList<>();
		BaseEventManager bem = new BaseEventManager(publisher, strings);
		assertDoesNotThrow(() -> bem);
		assertEquals(strings, bem.getData());
	}
	
	@Test
	void defaultConstructorWithListAndTimeout() {
		List<String> strings = new ArrayList<>();
		int timeout = 15;
		BaseEventManager bem = new BaseEventManager(publisher, strings, timeout);
		assertDoesNotThrow(() -> bem);
		assertEquals(strings, bem.getData());
		assertEquals(timeout, bem.getTimeoutInMilliseconds());
	}
	
	@Test
	void addItemNull() {
		String item = null;
		List<String> strings = new ArrayList<>();
		int timeout = 15;
		BaseEventManager bem = new BaseEventManager(publisher, strings, timeout);
		bem.addData(item);
		assertTrue(bem.getData().isEmpty());
	}
	
	@Test
	void addItemNotNull() {
		String item = "Test";
		List<String> strings = new ArrayList<>();
		int timeout = 15;
		BaseEventManager bem = new BaseEventManager(publisher, strings, timeout);
		bem.addData(item);
		assertFalse(bem.getData().isEmpty());
		assertEquals(item, bem.getData().iterator().next());
	}
	
	@Test
	void addItemNotNullNoValidateTimeout() throws InterruptedException {
		Mockito.doNothing().when(publisher).publishEvent(captor.capture());
		String item = "Test";
		List<String> strings = new ArrayList<>();
		int timeout = 15;
		BaseEventManager bem = new BaseEventManager(publisher, strings, timeout);
		bem.addData(item);
		assertFalse(bem.getData().isEmpty());
		assertEquals(item, bem.getData().iterator().next());
		assertFalse(bem.validateData());
		Thread.sleep(timeout * 5);
		assertFalse(captor.getValue().isSuccessful());
		assertTrue(captor.getValue().isTimeout()); 
		assertEquals(bem.getData(), captor.getValue().getPayload());
	}
	@Test
	void addItemNotNullValidate() {
		Mockito.doNothing().when(publisher).publishEvent(captor.capture());
		String item = "Test";
		List<String> strings = new ArrayList<>();
		BaseEventManager bem = new BaseEventManager(publisher, strings);
		bem.addData(item);
		assertFalse(bem.getData().isEmpty());
		assertEquals(item, bem.getData().iterator().next());
		String item2 = "Validated!";
		bem.addData(item2);
		assertFalse(bem.getData().isEmpty());
		assertTrue(bem.getData().contains(item));
		assertTrue(bem.getData().contains(item2));
		assertTrue(captor.getValue().isSuccessful());
		assertFalse(captor.getValue().isTimeout()); 
		assertEquals(bem.getData(), captor.getValue().getPayload());
	}
	
	@Test
	void setTimeout() {
		int timeout = 1234567788;
		List<String> strings = new ArrayList<>();
		BaseEventManager bem = new BaseEventManager(publisher, strings);
		assertNotEquals(timeout, bem.getTimeoutInMilliseconds());
		bem.setTimeout(timeout);
		assertEquals(timeout, bem.getTimeoutInMilliseconds());
	}

	@Test
	void removeDataNoMatch() {
		List<String> strings = new ArrayList<>();
		String item1 = "test";
		String item2 = "noMatch";
		BaseEventManager bem = new BaseEventManager(publisher, strings);
		bem.addData(item1);
		bem.removeData(item2);
		assertFalse(bem.getData().isEmpty());
	}
	
	@Test
	void removeDataMatch() {
		List<String> strings = new ArrayList<>();
		String item1 = "test";
		BaseEventManager bem = new BaseEventManager(publisher, strings);
		bem.addData(item1);
		bem.removeData(item1);
		assertTrue(bem.getData().isEmpty());
	}
}
