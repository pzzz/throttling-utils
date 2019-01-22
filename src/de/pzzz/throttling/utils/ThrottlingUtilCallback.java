package de.pzzz.throttling.utils;

import java.io.IOException;

/**
 * Callback that does the processing to obtain a response. Used by {@link CallbackThrottlingUtil}.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 *
 * @param <T> Type of the response.
 */
public interface ThrottlingUtilCallback<T> {
	T call() throws IOException;
}
