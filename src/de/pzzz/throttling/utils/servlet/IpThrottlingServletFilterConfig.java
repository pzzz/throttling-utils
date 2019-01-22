package de.pzzz.throttling.utils.servlet;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Data container for the configuration of an {@link IpThrottlingServletFilter}.
 */
public class IpThrottlingServletFilterConfig implements Serializable {
	private static final long serialVersionUID = 3719848455397318695L;
	private static final double DEFAULT_LIMIT = 10;
	private static final int DEFAULT_RESOLUTION = 1;
	private static final int DEFAULT_DURATION_MINUTES = 1;
	private static final int DEFAULT_CLEANUPINTERVAL_MINUTES = 5;
	private static final int DEFAULT_RESPONSECODE = 429;

	private double limit = DEFAULT_LIMIT;
	private long resolution = DEFAULT_RESOLUTION;
	private long duration = TimeUnit.MINUTES.toSeconds(DEFAULT_DURATION_MINUTES);
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private long cleanupInterval = TimeUnit.MINUTES.toSeconds(DEFAULT_CLEANUPINTERVAL_MINUTES);
	private boolean infoHeaders = true;
	private int responseCode = DEFAULT_RESPONSECODE;
	private String idSubKey;
	private String loggerPrefix = IpThrottlingServletFilter.class.getName();
	private String logLevel = Level.WARNING.getName();

	/**
	 * @return The maximum allowed request rate.
	 */
	public double getLimit() {
		return limit;
	}

	/**
	 * @param limit The maximum allowed request rate.
	 */
	public void setLimit(double limit) {
		this.limit = limit;
	}

	/**
	 * @return The resolution at which the request rate is calculated.
	 */
	public long getResolution() {
		return resolution;
	}

	/**
	 * @param resolution The resolution at which the request rate is calculated.
	 */
	public void setResolution(long resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return The duration over which the request rate is calculated.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration The duration over which the request rate is calculated.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return The time unit in which the intervals are given.
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @param timeUnit The time unit in which the intervals are given.
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	/**
	 * @return The interval in which expired rates are removed.
	 */
	public long getCleanupInterval() {
		return cleanupInterval;
	}

	/**
	 * @param cleanupInterval The interval in which expired rates are removed.
	 */
	public void setCleanupInterval(long cleanupInterval) {
		this.cleanupInterval = cleanupInterval;
	}

	/**
	 * @return Whether the client should be provided with information on allowed and
	 *         current request rate via headers.
	 */
	public boolean isInfoHeaders() {
		return infoHeaders;
	}

	/**
	 * @param infoHeaders Whether the client should be provided with information on
	 *                    allowed and current request rate via headers.
	 */
	public void setInfoHeaders(boolean infoHeaders) {
		this.infoHeaders = infoHeaders;
	}

	/**
	 * @return Which response code is sent to the client if the throttling becomes
	 *         active.
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode Which response code is sent to the client if the
	 *                     throttling becomes active.
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return A key that is used to store the rates in the {@link ServletContext}.
	 *         Must be set if different {@link IpThrottlingServletFilter}s are used
	 *         in parallel.
	 */
	public String getIdSubKey() {
		return idSubKey;
	}

	/**
	 * @param idSubKey A key that is used to store the rates in the
	 *                 {@link ServletContext}. Must be set if different
	 *                 {@link IpThrottlingServletFilter}s are used in parallel.
	 */
	public void setIdSubKey(String idSubKey) {
		this.idSubKey = idSubKey;
	}

	/**
	 * @return Prefix to the filters name, used to build the logger name used by the
	 *         {@link IpThrottlingServletFilter}.
	 */
	public String getLoggerPrefix() {
		return loggerPrefix;
	}

	/**
	 * @param Prefix to the filters name, used to build the logger name used by the
	 *               {@link IpThrottlingServletFilter}.
	 */
	public void setLoggerPrefix(String loggerPrefix) {
		this.loggerPrefix = loggerPrefix;
	}

	/**
	 * @return {@link Level} at which throttling of a client is logged.
	 */
	public String getLogLevel() {
		return logLevel;
	}

	public Level getJulLogLevel() {
		return Level.parse(logLevel);
	}

	/**
	 * @param logLevel {@link Level} at which throttling of a client is logged.
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
}
