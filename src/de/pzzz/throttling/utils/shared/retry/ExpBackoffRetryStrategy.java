package de.pzzz.throttling.utils.shared.retry;

import java.util.concurrent.TimeUnit;

public class ExpBackoffRetryStrategy extends WaitingLimitedRetryStrategy {
	private static final long serialVersionUID = 2576312967798901801L;
	private static final int DEFAULT_EXPBASE_SECONDS = 5;

	private long expBase = TimeUnit.SECONDS.toMillis(DEFAULT_EXPBASE_SECONDS);

	public ExpBackoffRetryStrategy() {
		super();
	}

	public ExpBackoffRetryStrategy(long expBase) {
		super();
		this.expBase = expBase;
	}

	public ExpBackoffRetryStrategy(int limit) {
		super(limit);
	}

	public ExpBackoffRetryStrategy(int limit, long expBase) {
		super(limit);
		this.expBase = expBase;
	}

	@Override
	public long getSleepMillis(RetryableRequest request) {
		return (long) (expBase * Math.pow(2, request.retriesMade()));
	}
}
