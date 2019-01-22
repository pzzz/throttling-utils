package de.pzzz.throttling.utils.shared.meters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.pzzz.throttling.utils.shared.meters.FixedFrameMeter;

@ExtendWith(MockitoExtension.class)
public class FixedFrameMeterTest {

	@Test
	public void functionWithMockClock(@Mock Clock clock) {
		Mockito.lenient().when(clock.millis()).thenReturn((long) 0);
		FixedFrameMeter test = new FixedFrameMeter(3, 1, TimeUnit.MILLISECONDS, clock);
		assertEquals(0, test.getMark());
		test.mark();
		assertEquals(1, test.getMark());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 1);
		assertEquals(1, test.getMark());
		test.mark();
		assertEquals(2, test.getMark());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 2);
		assertEquals(2, test.getMark());
		test.mark();
		assertEquals(3, test.getMark());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 3);
		assertEquals(2, test.getMark());
		test.mark();
		assertEquals(3, test.getMark());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 4);
		assertEquals(2, test.getMark());
		test.mark();
		assertEquals(3, test.getMark());
	}

	@Test
	public void skipWithMockClock(@Mock Clock clock) {
		Mockito.lenient().when(clock.millis()).thenReturn((long) 0);
		FixedFrameMeter test = new FixedFrameMeter(3, 1, TimeUnit.MILLISECONDS, clock);
		assertEquals(0, test.getMark());
		test.mark();
		assertEquals(1, test.getMark());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 3);
		assertEquals(0, test.getMark());
		test.mark();
		assertEquals(1, test.getMark());
	}

	@Test
	public void rateWithMockClock(@Mock Clock clock) {
		Mockito.lenient().when(clock.millis()).thenReturn((long) 0);
		FixedFrameMeter test = new FixedFrameMeter(3, 1, TimeUnit.MILLISECONDS, clock);
		assertEquals(0, test.getRate());
		test.mark();
		assertEquals(((double)1)/3, test.getRate());
		test.mark(2);
		assertEquals(((double)3)/3, test.getRate());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 3);
		assertEquals(0, test.getRate());
	}

	@Test
	public void expiry(@Mock Clock clock) {
		Mockito.lenient().when(clock.millis()).thenReturn((long) 0);
		FixedFrameMeter test = new FixedFrameMeter(3, 1, TimeUnit.MILLISECONDS, clock);
		assertEquals(false, test.isExpired());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 3);
		assertEquals(false, test.isExpired());
		Mockito.lenient().when(clock.millis()).thenReturn((long) 4);
		assertEquals(true, test.isExpired());
	}
}
