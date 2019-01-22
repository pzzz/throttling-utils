package de.pzzz.throttling.utils.httpclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import de.pzzz.throttling.utils.CallbackThrottlingUtil;
import de.pzzz.throttling.utils.RequestProcessor;
import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.ThrottlingUtilCallback;
import de.pzzz.throttling.utils.adaption.AdaptionStrategy;
import de.pzzz.throttling.utils.shared.meters.MeterConfig;
import de.pzzz.throttling.utils.shared.retry.RetryStrategy;

public class ThrottledCloseableHttpClient extends CloseableHttpClient {
	private CallbackThrottlingUtil<CloseableHttpResponse> util;
	private final CloseableHttpClient client;
	private final boolean logMetricsAtClose;

	public ThrottledCloseableHttpClient(CloseableHttpClient client, boolean logMetricsAtClose,
			AdaptionStrategy adaptionStrategy, RetryStrategy retryStrategy, MeterConfig meterConfig,
			List<RequestProcessor<ThrottlingUtilCallback<CloseableHttpResponse>>> requestProcessors,
			List<ResponseProcessor<CloseableHttpResponse>> responseProcessors) {
		this.client = client;
		this.logMetricsAtClose = logMetricsAtClose;
		util = new CallbackThrottlingUtil<CloseableHttpResponse>(adaptionStrategy, retryStrategy, meterConfig);
		util.setRequestProcessors(requestProcessors);
		util.setResponseProcessors(responseProcessors);
	}

	@Override
	public HttpParams getParams() {
		return client.getParams();
	}

	@Override
	public ClientConnectionManager getConnectionManager() {
		return client.getConnectionManager();
	}

	@Override
	public void close() throws IOException {
		client.close();
		if (logMetricsAtClose) {
			util.logMetrics();
		}
	}

	@Override
	protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context)
			throws IOException, ClientProtocolException {
		throw new RuntimeException("Operation not supported!");
	}

	@Override
	public CloseableHttpResponse execute(final HttpHost target, final HttpRequest request, final HttpContext context)
			throws IOException, ClientProtocolException {
		return util.process(new ThrottlingUtilCallback<CloseableHttpResponse>() {
			@Override
			public CloseableHttpResponse call() throws ClientProtocolException, IOException {
				return client.execute(target, request, context);
			}
		});
	}

	@Override
	public CloseableHttpResponse execute(final HttpUriRequest request, final HttpContext context)
			throws IOException, ClientProtocolException {
		return util.process(new ThrottlingUtilCallback<CloseableHttpResponse>() {
			@Override
			public CloseableHttpResponse call() throws IOException {
				return client.execute(request, context);
			}
		});
	}

	@Override
	public CloseableHttpResponse execute(final HttpUriRequest request) throws IOException, ClientProtocolException {
		return util.process(new ThrottlingUtilCallback<CloseableHttpResponse>() {
			@Override
			public CloseableHttpResponse call() throws IOException {
				return client.execute(request);
			}
		});
	}

	@Override
	public CloseableHttpResponse execute(final HttpHost target, final HttpRequest request)
			throws IOException, ClientProtocolException {
		return util.process(new ThrottlingUtilCallback<CloseableHttpResponse>() {
			@Override
			public CloseableHttpResponse call() throws IOException {
				return client.execute(target, request);
			}
		});
	}
}
