package de.pzzz.throttling.utils.wrapper;

import java.io.Serializable;

import de.pzzz.throttling.utils.shared.retry.RetryableResponse;

public class ResponseWithThrottlingProbability<T> extends RetryableResponse implements Serializable {
	private static final long serialVersionUID = 7805329296603011964L;

	private T response;
	private long responseTime;
	private double throttlingProbability = 0.0;

	public ResponseWithThrottlingProbability(T response, long responseTime) {
		this.response = response;
		this.responseTime = responseTime;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public double getThrottlingProbability() {
		return throttlingProbability;
	}

	public void setThrottlingProbability(double throttlingProbability) {
		if (throttlingProbability < 0 || throttlingProbability > 1) {
			throw new IllegalArgumentException("Throttling probability needs to be between 0 and 1! (Trying to set "
					+ throttlingProbability + ")");
		}
		this.throttlingProbability = throttlingProbability;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public void addThrottlingProbability(double throttlingProbability) {
		if (throttlingProbability < 0 || throttlingProbability > 1) {
			throw new IllegalArgumentException("Throttling probability needs to be between 0 and 1! (Trying to add "
					+ throttlingProbability + ")");
		}
		this.throttlingProbability += throttlingProbability;
		if (this.throttlingProbability > 1) {
			this.throttlingProbability = 1;
		}
	}

	public void multiplyThrottlingProbability(double throttlingProbability) {
		if (throttlingProbability < 0 || throttlingProbability > 1) {
			throw new IllegalArgumentException(
					"Throttling probability needs to be between 0 and 1! (Trying to multiply by "
							+ throttlingProbability + ")");
		}
		this.throttlingProbability *= throttlingProbability;
		if (this.throttlingProbability > 1) {
			this.throttlingProbability = 1;
		}
		if (0 == this.throttlingProbability) {
			this.throttlingProbability = throttlingProbability;
		}
	}
}
