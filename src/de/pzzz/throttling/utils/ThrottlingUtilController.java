package de.pzzz.throttling.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pzzz.throttling.utils.adaption.AdaptionStrategy;
import de.pzzz.throttling.utils.shared.meters.MeterConfig;
import de.pzzz.throttling.utils.shared.meters.RateMeter;
import de.pzzz.throttling.utils.shared.retry.RetryStrategy;
import de.pzzz.throttling.utils.wrapper.RequestWithLoad;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

public abstract class ThrottlingUtilController<T, V> implements HasMeters {
	private static final double DEFAULT_DETECTIONTHRESHOLD = 0.5;
	private static Logger mLogger = Logger.getLogger(ThrottlingUtilController.class.getName());

	/**
	 * Strategy to adapt processors implementing {@link IsAdaptable}.
	 */
	private AdaptionStrategy adaptionStrategy;
	/**
	 * Strategy to retry responses that have been throttled.
	 */
	private RetryStrategy retryStrategy;
	/**
	 * Pipeline of processors to execute before a request is issued.
	 */
	private List<RequestProcessor<T>> requestProcessors = new ArrayList<>();
	/**
	 * Pipeline of processors to execute after a response is received.
	 */
	private List<ResponseProcessor<V>> responseProcessors = new ArrayList<>();
	/**
	 * Threshold that needs to be exceeded by a processed responses
	 * {@link ResponseWithThrottlingProbability#getThrottlingProbability()} to count
	 * the response as active throttling.
	 */
	private double detectionThreshold = DEFAULT_DETECTIONTHRESHOLD;
	/**
	 * Default configuration for processors that is also used to take response times.
	 */
	private MeterConfig meterConfig;

	/**
	 * Constructs a new controller with the given configuration.
	 *
	 * @param adaptionStrategy
	 * @param retryStrategy
	 * @param config
	 */
	public ThrottlingUtilController(final AdaptionStrategy adaptionStrategy, final RetryStrategy retryStrategy,
			final MeterConfig config) {
		this.adaptionStrategy = adaptionStrategy;
		this.retryStrategy = retryStrategy;
		this.meterConfig = config;
	}

	/**
	 * Method to be called externally when issuing a request.
	 * Does include preprocessing with the pipeline in {@link #getRequestProcessors()}.
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public V process(T request) throws IOException {
		RequestWithLoad<T> wrappedRequest = new RequestWithLoad<T>(request);
		for (RequestProcessor<T> processor: getRequestProcessors()) {
			processor.process(wrappedRequest);
		}
		return postProcess(wrappedRequest);
	}

	private V postProcess(RequestWithLoad<T> request) throws IOException {
		long before = meterConfig.getClock().millis();
		mLogger.log(Level.INFO, "Executing request");
		V response = doProcess(request.getRequest());
		long took = meterConfig.getClock().millis() - before;
		ResponseWithThrottlingProbability<V> wrappedResponse = new ResponseWithThrottlingProbability<V>(response, took);
		for (ResponseProcessor<V> processor: getResponseProcessors()) {
			processor.process(wrappedResponse);
		}
		if (detectionThreshold < wrappedResponse.getThrottlingProbability()) {
			for (RequestProcessor<T> processor: getRequestProcessors()) {
				if (processor instanceof IsAdaptable) {
					((IsAdaptable) processor).setThrottlingDetected(adaptionStrategy.adaptionStepSize());
				}
			}
			for (ResponseProcessor<V> processor: getResponseProcessors()) {
				if (processor instanceof IsAdaptable) {
					((IsAdaptable) processor).setThrottlingDetected(adaptionStrategy.adaptionStepSize());
				}
			}
			if (retryStrategy.doRetry(request, wrappedResponse)) {
				request.countRetry();
				if (wrappedResponse.getResponse() instanceof Closeable) {
					((Closeable) wrappedResponse.getResponse()).close();
				}
				mLogger.log(Level.INFO, "Retrying request");
				return postProcess(request);
			}
		} else if (adaptionStrategy.isAdapt()) {
			mLogger.log(Level.INFO, "Adapting limits with factor " + adaptionStrategy.adaptionStepSize());
			for (RequestProcessor<T> processor: getRequestProcessors()) {
				if (processor instanceof IsAdaptable) {
					((IsAdaptable) processor).increaseLimits(adaptionStrategy.adaptionStepSize());
				}
			}
			for (ResponseProcessor<V> processor: getResponseProcessors()) {
				if (processor instanceof IsAdaptable) {
					((IsAdaptable) processor).increaseLimits(adaptionStrategy.adaptionStepSize());
				}
			}
		}
		return wrappedResponse.getResponse();
	}

	/**
	 * To be implemented to actually issue the client. Is called internally after
	 * all pre-processing is done.
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public abstract V doProcess(T request) throws IOException;

	/**
	 * Adds a {@link RequestProcessor} to the pre-processing pipeline {@link #requestProcessors}.
	 *
	 * @param processor
	 */
	public void addRequestProcessor(RequestProcessor<T> processor) {
		getRequestProcessors().add(processor);
	}

	/**
	 * Gets the pre-processing pipeline {@link #requestProcessors}. Ensures that an
	 * empty list is returned if no pipeline exists.
	 *
	 * @return
	 */
	public List<RequestProcessor<T>> getRequestProcessors() {
		if (null == requestProcessors) {
			requestProcessors = new ArrayList<>();
		}
		return requestProcessors;
	}

	public void setRequestProcessors(List<RequestProcessor<T>> requestProcessors) {
		this.requestProcessors = requestProcessors;
	}

	/**
	 * Adds a {@link ResponseProcessor} to the post-processing pipeline {@link #responseProcessors}.
	 *
	 * @param processor
	 */
	public void addResponseProcessor(ResponseProcessor<V> processor) {
		getResponseProcessors().add(processor);
	}

	/**
	 * Gets the post-processing pipeline {@link #responseProcessors}. Ensures that an
	 * empty list is returned if no pipeline exists.
	 *
	 * @return
	 */
	public List<ResponseProcessor<V>> getResponseProcessors() {
		if (null == responseProcessors) {
			responseProcessors = new ArrayList<>();
		}
		return responseProcessors;
	}

	public void setResponseProcessors(List<ResponseProcessor<V>> responseProcessors) {
		this.responseProcessors = responseProcessors;
	}

	public double getDetectionThreshold() {
		return detectionThreshold;
	}

	public void setDetectionThreshold(double detectionThreshold) {
		this.detectionThreshold = detectionThreshold;
	}

	public MeterConfig getMeterConfig() {
		return meterConfig;
	}

	/**
	 * Can be used to dump all metrics collected in the processing pipelines in log
	 * messages. Issues per metric one log line with its {@link RateMeter#getRate()}
	 * and one with its mark {@link RateMeter#getMark()}.
	 */
	public void logMetrics() {
		for (Entry<String, RateMeter> entry : getMeters().entrySet()) {
			mLogger.log(Level.INFO,
					entry.getKey() + ".rate: " + entry.getValue().getRate() + " " + entry.getValue().rateUnit());
			mLogger.log(Level.INFO, entry.getKey() + ".mark: " + entry.getValue().getMark());
		}
	}

	@Override
	public Map<String, RateMeter> getMeters() {
		Map<String, RateMeter> result = new HashMap<>();
		for (RequestProcessor<T> processor : getRequestProcessors()) {
			if (processor instanceof HasMeters) {
				result.putAll(((HasMeters) processor).getMeters());
			}
		}
		for (ResponseProcessor<V> processor: getResponseProcessors()) {
			if (processor instanceof HasMeters) {
				result.putAll(((HasMeters) processor).getMeters());
			}
		}
		return result;
	}
}
