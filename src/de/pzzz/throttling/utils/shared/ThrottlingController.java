package de.pzzz.throttling.utils.shared;

import de.pzzz.throttling.utils.shared.meters.RateMeter;

/**
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 */
public class ThrottlingController {
	private double limit;
	private final RateMeter meter;

	public ThrottlingController(RateMeter meter, double limit) {
		this.meter = meter;
		setLimit(limit);
	}

	public void checkThrottling() throws ThrottlingException {
		checkThrottling(1);
	}

	public void checkThrottling(int value) throws ThrottlingException {
		meter.mark(value);
		// use 0 to disable throttling limit
		if (0 < limit && meter.getRate() >= limit) {
			throw new ThrottlingException();
		}
	}

	public double getRate() {
		return meter.getRate();
	}

	public boolean isExpired() {
		return meter.isExpired();
	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		if (0 > limit) {
			throw new IllegalArgumentException("A limit has to be positive!");
		}
		this.limit = limit;
	}
}
