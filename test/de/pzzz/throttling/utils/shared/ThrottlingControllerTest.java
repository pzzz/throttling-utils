package de.pzzz.throttling.utils.shared;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.pzzz.throttling.utils.shared.ThrottlingController;
import de.pzzz.throttling.utils.shared.ThrottlingException;
import de.pzzz.throttling.utils.shared.meters.RateMeter;

@ExtendWith(MockitoExtension.class)
public class ThrottlingControllerTest {

	@Test
	public void aboveLimit(@Mock RateMeter rate) {
		Mockito.lenient().when(rate.getRate()).thenReturn(0.6);
		ThrottlingController handler = new ThrottlingController(rate, 0.5);
		assertThrows(ThrottlingException.class, () -> {
			handler.checkThrottling();
		});
	}

	@Test
	public void onLimit(@Mock RateMeter rate) {
		Mockito.lenient().when(rate.getRate()).thenReturn(0.5);
		ThrottlingController handler = new ThrottlingController(rate, 0.5);
		assertThrows(ThrottlingException.class, () -> {
			handler.checkThrottling();
		});
	}

	@Test
	public void belowLimit(@Mock RateMeter rate) throws ThrottlingException {
		Mockito.lenient().when(rate.getRate()).thenReturn(0.4);
		ThrottlingController handler = new ThrottlingController(rate, 0.5);
		handler.checkThrottling();
		assertTrue(true);
	}
}
