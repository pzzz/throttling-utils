package de.pzzz.throttling.utils.wrapper;

import de.pzzz.throttling.utils.shared.retry.RetryableRequest;

public class RequestWithLoad<T> extends RetryableRequest {
	private static final long serialVersionUID = -8575096186032591550L;

	private T request;
	private int loadFactor = 1;

	public RequestWithLoad(T request) {
		this.request = request;
	}

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}

	public int getLoadFactor() {
		return loadFactor;
	}

	public void setLoadFactor(int loadFactor) {
		this.loadFactor = loadFactor;
	}
}
