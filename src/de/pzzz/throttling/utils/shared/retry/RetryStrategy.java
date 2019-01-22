package de.pzzz.throttling.utils.shared.retry;

import java.io.Serializable;

public interface RetryStrategy extends Serializable {
	boolean doRetry(RetryableRequest request, RetryableResponse response);
}
