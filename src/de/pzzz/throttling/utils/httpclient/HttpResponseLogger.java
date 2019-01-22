package de.pzzz.throttling.utils.httpclient;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.methods.CloseableHttpResponse;

import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

public class HttpResponseLogger implements ResponseProcessor<CloseableHttpResponse> {
	private final Logger logger;
	private final Level logLevel = Level.INFO;

	public HttpResponseLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public ResponseWithThrottlingProbability<CloseableHttpResponse> process(
			ResponseWithThrottlingProbability<CloseableHttpResponse> response) {
		logger.log(logLevel, "Got response code " + response.getResponse().getStatusLine().getStatusCode());
		return response;
	}
}
