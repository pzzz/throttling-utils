package de.pzzz.throttling.utils;

import de.pzzz.throttling.utils.shared.retry.RetryableRequest;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

/**
 * Can process a response directly after it is received and before it is
 * forwarded to the caller.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 *
 * @param <T> Type of the response.
 */
public interface ResponseProcessor<T> {
	/**
	 * Does process the response, which could alter meta-data contained in the
	 * {@link ResponseWithThrottlingProbability} or {@link RetryableRequest}.
	 *
	 * @param request The response to process.
	 * @return
	 */
	ResponseWithThrottlingProbability<T> process(ResponseWithThrottlingProbability<T> response);
}
