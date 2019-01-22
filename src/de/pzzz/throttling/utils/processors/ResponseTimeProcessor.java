package de.pzzz.throttling.utils.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pzzz.throttling.utils.HasMeters;
import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.shared.meters.FixedOccurrenceMeter;
import de.pzzz.throttling.utils.shared.meters.RateMeter;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

public class ResponseTimeProcessor<T> implements ResponseProcessor<T>, HasMeters {
	private static Logger mLogger = Logger.getLogger(RequestRateLimiter.class.getName());

	private final RateMeter meter;
	private final RateMeter shortMeter;
	private final double detectionOffset;
	private final double probabilityMultiplier;

	public ResponseTimeProcessor(int historySize, int comparisonSize, double detectionOffset,
			double probabilityMultiplier) {
		this.meter = new FixedOccurrenceMeter(historySize);
		this.shortMeter = new FixedOccurrenceMeter(comparisonSize);
		this.detectionOffset = detectionOffset;
		this.probabilityMultiplier = probabilityMultiplier;
	}

	@Override
	public ResponseWithThrottlingProbability<T> process(ResponseWithThrottlingProbability<T> response) {
		meter.mark(response.getResponseTime());
		shortMeter.mark(response.getResponseTime());
		double longRate = meter.getMark();
		double shortRate = shortMeter.getMark();
		if (longRate + longRate * detectionOffset < shortRate) {
			mLogger.log(Level.WARNING, "Detected throttling due to response time. Long average: " + meter.getRate()
					+ " Short rate: " + shortMeter.getRate() + " valued with multiplier " + probabilityMultiplier);
			response.multiplyThrottlingProbability(probabilityMultiplier);
		}
		return response;
	}

	@Override
	public Map<String, RateMeter> getMeters() {
		Map<String, RateMeter> result = new HashMap<>();
		result.put(RequestRateLimiter.class.getName() + ".rate", meter);
		result.put(RequestRateLimiter.class.getName() + ".shortRate", meter);
		return result;
	}
}
