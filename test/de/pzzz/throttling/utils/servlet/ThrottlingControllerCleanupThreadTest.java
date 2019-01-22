package de.pzzz.throttling.utils.servlet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.pzzz.throttling.utils.shared.ThrottlingController;
import de.pzzz.throttling.utils.shared.meters.FixedFrameMeter;

@ExtendWith(MockitoExtension.class)
public class ThrottlingControllerCleanupThreadTest {

	@Test
	public void cleanup(@Mock Clock clock, @Mock ServletContext context) {
		Map<String, ThrottlingController> map = new HashMap<>();
		Mockito.lenient().when(clock.millis()).thenReturn((long) 0);
		FixedFrameMeter meter = new FixedFrameMeter(3, 1, TimeUnit.MILLISECONDS, clock);
		ThrottlingController controller = new ThrottlingController(meter, 1);
		String idKey = "test";
		map.put(idKey, controller);
		Mockito.lenient().when(context.getAttribute(idKey)).thenReturn(map);

		ThrottlingControllerCleanupThread test = new ThrottlingControllerCleanupThread(context, idKey);
		assertTrue(map.containsKey(idKey));
		assertFalse(map.get(idKey).isExpired());
		test.run();
		assertTrue(map.containsKey(idKey));
		assertFalse(map.get(idKey).isExpired());

		Mockito.lenient().when(clock.millis()).thenReturn((long) 4);
		assertTrue(map.containsKey(idKey));
		assertTrue(map.get(idKey).isExpired());
		test.run();
		assertFalse(map.containsKey(idKey));
		assertTrue(controller.isExpired());
	}
}
