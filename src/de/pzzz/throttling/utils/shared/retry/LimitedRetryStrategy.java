package de.pzzz.throttling.utils.shared.retry;

public class LimitedRetryStrategy implements RetryStrategy {
	private static final long serialVersionUID = 4137838030058191587L;
	private static final int DEFAULT_LIMIT = 3;

	private int limit = DEFAULT_LIMIT;

	public LimitedRetryStrategy() {}

	public LimitedRetryStrategy(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean doRetry(RetryableRequest request, RetryableResponse response) {
		return request.retriesMade() < limit;
	}

}
