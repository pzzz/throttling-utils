package de.pzzz.throttling.utils.wrapper;

import org.junit.jupiter.api.Test;

public class ResponseWithThrottlingProbabilityTest {

	@Test
	public void testSetProbability() {
		ResponseWithThrottlingProbability<Void> resp = new ResponseWithThrottlingProbability<Void>(null, 0);
		resp.setThrottlingProbability(0);
		resp.setThrottlingProbability(1);
		resp.addThrottlingProbability(0);
		resp.addThrottlingProbability(1);
		resp.multiplyThrottlingProbability(0);
		resp.multiplyThrottlingProbability(1);
	}
}
