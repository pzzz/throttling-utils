package de.pzzz.throttling.utils.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import de.pzzz.throttling.utils.RequestProcessor;
import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.ThrottlingUtilCallback;
import de.pzzz.throttling.utils.adaption.AdaptionStrategy;
import de.pzzz.throttling.utils.adaption.NoAdaptionStrategy;
import de.pzzz.throttling.utils.shared.meters.MeterConfig;
import de.pzzz.throttling.utils.shared.retry.NoRetryStrategy;
import de.pzzz.throttling.utils.shared.retry.RetryStrategy;

public class ThrottledHttpClientBuilder extends HttpClientBuilder {
	private HttpClientBuilder builder;
	private boolean logMetricsAtClose = true;
	private List<RequestProcessor<ThrottlingUtilCallback<CloseableHttpResponse>>> requestProcessors = new ArrayList<>();
	private List<ResponseProcessor<CloseableHttpResponse>> responseProcessors = new ArrayList<>();
	private AdaptionStrategy adaptionStrategy = new NoAdaptionStrategy();
	private RetryStrategy retryStrategy = new NoRetryStrategy();
	private MeterConfig meterConfig = MeterConfig.meter5minutes1second();

	public ThrottledHttpClientBuilder(HttpClientBuilder builder) {
		this.builder = builder;
	}

	@Override
	public CloseableHttpClient build() {
		CloseableHttpClient client = builder.build();
		ThrottledCloseableHttpClient result = new ThrottledCloseableHttpClient(client, logMetricsAtClose,
				adaptionStrategy, retryStrategy, meterConfig, getRequestProcessors(), getResponseProcessors());
		return result;
	}

	public void addRequestProcessor(RequestProcessor<ThrottlingUtilCallback<CloseableHttpResponse>> processor) {
		getRequestProcessors().add(processor);
	}

	public List<RequestProcessor<ThrottlingUtilCallback<CloseableHttpResponse>>> getRequestProcessors() {
		if (null == requestProcessors) {
			requestProcessors = new ArrayList<>();
		}
		return requestProcessors;
	}

	public void setRequestProcessors(
			List<RequestProcessor<ThrottlingUtilCallback<CloseableHttpResponse>>> requestProcessors) {
		this.requestProcessors = requestProcessors;
	}

	public void addResponseProcessor(ResponseProcessor<CloseableHttpResponse> processor) {
		getResponseProcessors().add(processor);
	}

	public List<ResponseProcessor<CloseableHttpResponse>> getResponseProcessors() {
		if (null == responseProcessors) {
			responseProcessors = new ArrayList<>();
		}
		return responseProcessors;
	}

	public void setResponseProcessors(List<ResponseProcessor<CloseableHttpResponse>> responseProcessors) {
		this.responseProcessors = responseProcessors;
	}

	public AdaptionStrategy getAdaptionStrategy() {
		return adaptionStrategy;
	}

	public void setAdaptionStrategy(AdaptionStrategy adaptionStrategy) {
		this.adaptionStrategy = adaptionStrategy;
	}

	public RetryStrategy getRetryStrategy() {
		return retryStrategy;
	}

	public void setRetryStrategy(RetryStrategy retryStrategy) {
		this.retryStrategy = retryStrategy;
	}

	public MeterConfig getMeterConfig() {
		return meterConfig;
	}

	public void setMeterConfig(MeterConfig meterConfig) {
		this.meterConfig = meterConfig;
	}

	public boolean isLogMetricsAtClose() {
		return logMetricsAtClose;
	}

	public void setLogMetricsAtClose(boolean logMetricsAtClose) {
		this.logMetricsAtClose = logMetricsAtClose;
	}
}
