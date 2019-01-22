package de.pzzz.throttling.utils.shared.retry;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WaitingLimitedRetryStrategy extends LimitedRetryStrategy {
	private static final long serialVersionUID = -7313801777528188101L;
	private static Logger mLogger = Logger.getLogger(WaitingLimitedRetryStrategy.class.getName());

	public WaitingLimitedRetryStrategy() {
		super();
	}

	public WaitingLimitedRetryStrategy(int limit) {
		super(limit);
	}

	@Override
	public boolean doRetry(RetryableRequest request, RetryableResponse response) {
		if (!super.doRetry(request, response)) {
			return false;
		}
		long sleepTime;
		if (null != request.getRetryWaitMillis()) {
			sleepTime = request.getRetryWaitMillis();
		} else {
			sleepTime = getSleepMillis(request);
		}
		try {
			mLogger.log(Level.INFO, "Retry waiting for " + sleepTime + "ms");
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			mLogger.log(Level.WARNING, "Retry aborted!", e);
			Thread.currentThread().interrupt();
		}
		mLogger.log(Level.INFO, "Retry finished waiting, will retry now...");
		return true;
	}

	public abstract long getSleepMillis(RetryableRequest request);
}
