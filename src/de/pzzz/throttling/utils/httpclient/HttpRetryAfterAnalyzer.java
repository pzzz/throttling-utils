package de.pzzz.throttling.utils.httpclient;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import de.pzzz.throttling.utils.ResponseProcessor;
import de.pzzz.throttling.utils.wrapper.ResponseWithThrottlingProbability;

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Retry-After
 * @author peter
 *
 */
public class HttpRetryAfterAnalyzer implements ResponseProcessor<CloseableHttpResponse> {
	private static Logger mLogger = Logger.getLogger(HttpRetryAfterAnalyzer.class.getName());

	@Override
	public ResponseWithThrottlingProbability<CloseableHttpResponse> process(
			ResponseWithThrottlingProbability<CloseableHttpResponse> response) {
		if (response.getResponse().containsHeader("Retry-After")) {
			for (Header header: response.getResponse().getHeaders("Retry-After")) {
				try {
					long waitSeconds = Long.parseLong(header.getValue());
					response.setRetryWaitMillis(TimeUnit.SECONDS.toMillis(waitSeconds));
				} catch (NumberFormatException e) {
					mLogger.log(Level.WARNING, "Unparseable Header: " + header.getValue());
				}
			}
		}
		return response;
	}
}
