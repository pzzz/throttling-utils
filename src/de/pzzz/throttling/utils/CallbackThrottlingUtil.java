package de.pzzz.throttling.utils;

import java.io.IOException;

import de.pzzz.throttling.utils.adaption.AdaptionStrategy;
import de.pzzz.throttling.utils.shared.meters.MeterConfig;
import de.pzzz.throttling.utils.shared.retry.RetryStrategy;

/**
 * {@link ThrottlingUtilController} that is generic only for the response type. A request is not
 * necessary but instead replaced by a callback.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 *
 * @param <T> Type of the response.
 */
public class CallbackThrottlingUtil<T> extends ThrottlingUtilController<ThrottlingUtilCallback<T>, T> {

	/**
	 * Default constructor just calling super.
	 *
	 * @param adaptionStrategy Used {@link AdaptionStrategy}
	 * @param retryStrategy Used {@link RetryStrategy}
	 * @param meterConfig Used {@link MeterConfig}
	 */
	public CallbackThrottlingUtil(final AdaptionStrategy adaptionStrategy, final RetryStrategy retryStrategy,
			final MeterConfig meterConfig) {
		super(adaptionStrategy, retryStrategy, meterConfig);
	}

	@Override
	public T doProcess(ThrottlingUtilCallback<T> request) throws IOException {
		return request.call();
	}
}
