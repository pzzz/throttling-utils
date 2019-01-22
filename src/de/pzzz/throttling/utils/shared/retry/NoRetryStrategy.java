package de.pzzz.throttling.utils.shared.retry;

public class NoRetryStrategy implements RetryStrategy {
	private static final long serialVersionUID = -4020733064122190391L;

	@Override
	public boolean doRetry(RetryableRequest request, RetryableResponse response) {
		return false;
	}
}
