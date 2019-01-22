package de.pzzz.throttling.utils.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.pzzz.throttling.utils.shared.ThrottlingController;
import de.pzzz.throttling.utils.shared.ThrottlingException;
import de.pzzz.throttling.utils.shared.meters.FixedFrameMeter;

public class IpThrottlingServletFilter implements Filter {
	private static final String THROTTLING_CONTROLLERS = "de.pzzz.throttling.utils.controllers";

	private ScheduledExecutorService cleanupThreadExecutor;
	private Logger mLogger;

	private ServletContext context;
	private IpThrottlingServletFilterConfig config;

	private String idKey;

	/**
	 * Default constructor for adding filter via web.xml and init parameters.
	 */
	public IpThrottlingServletFilter() {
		config = new IpThrottlingServletFilterConfig();
	}

	/**
	 * Constructor for programmatically adding the filter with direct
	 * parameterization.
	 *
	 * @param config Configuration with customized parameters.
	 */
	public IpThrottlingServletFilter(IpThrottlingServletFilterConfig config) {
		this.config = config;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		initParams(filterConfig);
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat(filterConfig.getFilterName() + "-cleanup-thread-%d").build();
		cleanupThreadExecutor = Executors.newScheduledThreadPool(1, namedThreadFactory);
		Runnable cleanupThread = new ThrottlingControllerCleanupThread(context, idKey);
		cleanupThreadExecutor.scheduleWithFixedDelay(cleanupThread, config.getCleanupInterval(),
				config.getCleanupInterval(), config.getTimeUnit());
		mLogger = Logger.getLogger(config.getLoggerPrefix() + "." + filterConfig.getFilterName());
	}

	private void initParams(FilterConfig filterConfig) {
		context = filterConfig.getServletContext();
		if (null != filterConfig.getInitParameter("limit")) {
			config.setLimit(Double.parseDouble(filterConfig.getInitParameter("limit")));
		}
		if (null != filterConfig.getInitParameter("resolution")) {
			config.setResolution(Long.parseLong(filterConfig.getInitParameter("resolution")));
		}
		if (null != filterConfig.getInitParameter("duration")) {
			config.setDuration(Long.parseLong(filterConfig.getInitParameter("duration")));
		}
		if (null != filterConfig.getInitParameter("timeUnit")) {
			config.setTimeUnit(TimeUnit.valueOf(filterConfig.getInitParameter("timeUnit")));
		}
		if (null != filterConfig.getInitParameter("cleanupInterval")) {
			config.setCleanupInterval(Long.parseLong(filterConfig.getInitParameter("cleanupInterval")));
		}
		if (null != filterConfig.getInitParameter("infoHeaders")) {
			config.setInfoHeaders(Boolean.valueOf(filterConfig.getInitParameter("infoHeaders")));
		}
		if (null != filterConfig.getInitParameter("responseCode")) {
			config.setResponseCode(Integer.parseInt(filterConfig.getInitParameter("responseCode")));
		}
		if (null != filterConfig.getInitParameter("subIdKey")) {
			config.setIdSubKey(filterConfig.getInitParameter("subIdKey"));
		}
		if (null != filterConfig.getInitParameter("loggerPrefix")) {
			config.setLoggerPrefix(filterConfig.getInitParameter("loggerPrefix"));
		}
		if (null != filterConfig.getInitParameter("logLevel")) {
			config.setLogLevel(filterConfig.getInitParameter("logLevel"));
		}
		idKey = THROTTLING_CONTROLLERS;
		if (null != config.getIdSubKey()) {
			idKey += "." + config.getIdSubKey();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			HttpServletResponse httpResp = (HttpServletResponse) response;
			String remoteAddress = request.getRemoteAddr();
			Object oControllers = context.getAttribute(idKey);
			Map<String, ThrottlingController> controller;
			if (null == oControllers || !(oControllers instanceof Map<?, ?>)) {
				controller = new HashMap<>();
				context.setAttribute(idKey, controller);
			} else {
				controller = (Map<String, ThrottlingController>) oControllers;
			}
			ThrottlingController rate = controller.get(remoteAddress);
			if (null == rate) {
				// not yet initialized
				rate = new ThrottlingController(
						new FixedFrameMeter(config.getDuration(), config.getResolution(), config.getTimeUnit()),
						config.getLimit());
				controller.put(remoteAddress, rate);
			}
			boolean isThrottling = true;
			try {
				rate.checkThrottling();
				isThrottling = false;
			} catch (ThrottlingException e) {
				mLogger.log(config.getJulLogLevel(),
						"Got throttling for " + request.getRemoteAddr() + " at rate " + rate.getRate());
			}
			if (config.isInfoHeaders()) {
				// further info: https://tools.ietf.org/html/rfc6648
				httpResp.setHeader("Current-Request-Rate", Double.toString(rate.getRate()));
				httpResp.setHeader("Max-Request-Rate", Double.toString(rate.getLimit()));
			}
			if (isThrottling) {
				httpResp.sendError(config.getResponseCode());
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		cleanupThreadExecutor.shutdown();
	}
}
