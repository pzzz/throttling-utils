package de.pzzz.throttling.utils.shared.retry;

import java.io.Serializable;

public abstract class RetryableRequest implements Serializable {
	private static final long serialVersionUID = 8865279698130841510L;

	private int retries = 0;
	private Long retryWaitMillis;

	public int retriesMade() {
		return retries;
	}

	public void countRetry() {
		retries++;
	}

	public Long getRetryWaitMillis() {
		return retryWaitMillis;
	}

	public void setRetryWaitMillis(long retryWaitMillis) {
		this.retryWaitMillis = retryWaitMillis;
	}
}
