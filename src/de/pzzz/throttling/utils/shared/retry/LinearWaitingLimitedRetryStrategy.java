package de.pzzz.throttling.utils.shared.retry;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LinearWaitingLimitedRetryStrategy extends WaitingLimitedRetryStrategy {
	private static final long serialVersionUID = 4137838030058191587L;
	private static final int DEFAULT_WAIT_SECONDS = 5;

	private long waitMillies = TimeUnit.SECONDS.toMillis(DEFAULT_WAIT_SECONDS);

	public LinearWaitingLimitedRetryStrategy() {
		super();
	}

	public LinearWaitingLimitedRetryStrategy(long waitMillies) {
		super();
		this.waitMillies = waitMillies;
	}

	public LinearWaitingLimitedRetryStrategy(int limit) {
		super(limit);
	}

	public LinearWaitingLimitedRetryStrategy(int limit, long waitMillies) {
		super(limit);
		this.waitMillies = waitMillies;
	}

	@Override
	public long getSleepMillis(RetryableRequest request) {
		return new Date().getTime() - waitMillies;
	}
}
