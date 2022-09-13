package com.siliconmtn.data.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class InvalidDataExceptionTest {

	@Test
	void baseConstructorTest() {
		InvalidDataException ex = new InvalidDataException();
		assertNotNull(ex);
	}

	@Test
	void baseConstructorBothArgsTest() {
		Throwable t = new Throwable("Sample");
		InvalidDataException ex = new InvalidDataException("Error Occurred", t);
		assertEquals("Error Occurred", ex.getMessage());
		assertEquals(t, ex.getCause());
	}

	@Test
	void baseConstructorKeyArgTest() {
		InvalidDataException ex = new InvalidDataException("Error Occurred");
		assertEquals("Error Occurred", ex.getMessage());
	}

	@Test
	void baseConstructorThrowableArgTest() {
		Throwable t = new Throwable("Sample");
		InvalidDataException ex = new InvalidDataException(t);
		assertEquals(t, ex.getCause());
	}
}
