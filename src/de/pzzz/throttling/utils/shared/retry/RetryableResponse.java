package de.pzzz.throttling.utils.shared.retry;

public abstract class RetryableResponse {
	private Long retryWaitMillis;

	public Long getRetryWaitMillis() {
		return retryWaitMillis;
	}

	public void setRetryWaitMillis(Long retryWaitMillis) {
		this.retryWaitMillis = retryWaitMillis;
	}
}
