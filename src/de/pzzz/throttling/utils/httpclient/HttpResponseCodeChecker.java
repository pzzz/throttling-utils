package de.pzzz.throttling.utils.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

public class HttpResponseCodeChecker implements ResponseProcessor<CloseableHttpResponse> {
	private static final int DEFAULT_CODE = 429;
	private static final double DEFAULT_PROBABILITY = 1.0;
	private Map<Integer, Double> probabilityCodes;

	public static HttpResponseCodeChecker getDefaultConfig() {
		HttpResponseCodeChecker res = new HttpResponseCodeChecker();
		res.mapCodeProbability(DEFAULT_CODE, DEFAULT_PROBABILITY);
		return res;
	}

	@Override
	public ResponseWithThrottlingProbability<CloseableHttpResponse> process(
			ResponseWithThrottlingProbability<CloseableHttpResponse> response) {
		int httpStatusCode = response.getResponse().getStatusLine().getStatusCode();
		if (getProbabilityCodes().containsKey(httpStatusCode)) {
			response.setThrottlingProbability(getProbabilityCodes().get(httpStatusCode));
		}
		return response;
	}

	public void mapCodeProbability(int httpStatusCode, double throttlingProbability) {
		getProbabilityCodes().put(httpStatusCode, throttlingProbability);
	}

	public Map<Integer, Double> getProbabilityCodes() {
		if (null == probabilityCodes) {
			probabilityCodes = new HashMap<>();
		}
		return probabilityCodes;
	}

	public void setProbabilityCodes(Map<Integer, Double> probabilityCodes) {
		this.probabilityCodes = probabilityCodes;
	}
}
