package de.pzzz.throttling.utils.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pzzz.throttling.utils.HasMeters;
import de.pzzz.throttling.utils.IsAdaptable;
import de.pzzz.throttling.utils.RequestProcessor;
import de.pzzz.throttling.utils.shared.meters.FixedFrameMeter;
import de.pzzz.throttling.utils.shared.meters.MeterConfig;
import de.pzzz.throttling.utils.shared.meters.RateMeter;
import de.pzzz.throttling.utils.wrapper.RequestWithLoad;

public class RequestRateLimiter<T> implements RequestProcessor<T>, IsAdaptable, HasMeters {
	private static Logger mLogger = Logger.getLogger(RequestRateLimiter.class.getName());

	private final RateMeter meter;
	private double limit = 0;

	public RequestRateLimiter(RateMeter meter) {
		this.meter = meter;
	}

	public RequestRateLimiter(MeterConfig config) {
		this.meter = new FixedFrameMeter(config);
	}

	public RequestRateLimiter(RateMeter meter, double limit) {
		this.meter = meter;
		this.limit = limit;
	}

	public RequestRateLimiter(MeterConfig config, double limit) {
		this.meter = new FixedFrameMeter(config);
		this.limit = limit;
	}

	@Override
	public RequestWithLoad<T> process(RequestWithLoad<T> request) {
		if (0 < limit && null != meter) {
			while (meter.getRate() >= limit) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					mLogger.log(Level.WARNING, "Processing of request " + request + " aborted!", e);
					Thread.currentThread().interrupt();
				}
			}
		}
		meter.mark(request.getLoadFactor());
		return request;
	}

	@Override
	public void setThrottlingDetected(double adaptionStepSize) {
		mLogger.log(Level.WARNING, "Throttling detected at rate " + meter.getRate());
		limit = meter.getRate();
		limit -= limit * adaptionStepSize;
	}

	@Override
	public void increaseLimits(double adaptionStepSize) {
		limit += limit * adaptionStepSize;
	}

	@Override
	public Map<String, RateMeter> getMeters() {
		Map<String, RateMeter> result = new HashMap<>();
		result.put(RequestRateLimiter.class.getName(), meter);
		return result;
	}
}
