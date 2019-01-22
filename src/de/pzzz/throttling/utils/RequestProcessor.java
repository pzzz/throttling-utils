package de.pzzz.throttling.utils;

import de.pzzz.throttling.utils.shared.retry.RetryableRequest;
import de.pzzz.throttling.utils.wrapper.RequestWithLoad;

/**
 * Can process a request before it is issued.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 *
 * @param <T> Type of the request.
 */
public interface RequestProcessor<T> {
	/**
	 * Does process the request, which could alter meta-data contained in the
	 * {@link RequestWithLoad} or {@link RetryableRequest}.
	 *
	 * @param request The request to process.
	 * @return
	 */
	RequestWithLoad<T> process(RequestWithLoad<T> request);
}
