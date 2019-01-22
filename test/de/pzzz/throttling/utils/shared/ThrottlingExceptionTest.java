package de.pzzz.throttling.utils.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ThrottlingExceptionTest {

	@Test
	public void testJunit() {
		assertThrows(ThrottlingException.class, () -> {
			throw new ThrottlingException();
		});
	}
}
