package de.pzzz.throttling.utils.servlet;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.pzzz.throttling.utils.shared.ThrottlingController;

@ExtendWith(MockitoExtension.class)
public class IpThrottlingServletFilterTest {

	@Test
	public void doTest(@Mock ServletContext context, @Mock FilterConfig config) throws ServletException {
		Map<String, ThrottlingController> map = new HashMap<>();
		Mockito.lenient().when(context.getAttribute("de.pzzz.throttling.utils.controllers")).thenReturn(map); //IpThrottlingServletFilter.THROTTLING_CONTROLLERS
		Mockito.lenient().when(config.getServletContext()).thenReturn(context);
		Mockito.lenient().when(config.getInitParameter("limit")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("resolution")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("duration")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("timeUnit")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("cleanupInterval")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("infoHeaders")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("responseCode")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("subIdKey")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("loggerPrefix")).thenReturn(null);
		Mockito.lenient().when(config.getInitParameter("logLevel")).thenReturn(null);
		IpThrottlingServletFilter filter = new IpThrottlingServletFilter();
		filter.init(config);
		filter.destroy();
		assertTrue(true);
	}
}
