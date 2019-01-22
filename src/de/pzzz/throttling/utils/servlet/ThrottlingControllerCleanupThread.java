package de.pzzz.throttling.utils.servlet;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import de.pzzz.throttling.utils.shared.ThrottlingController;

public class ThrottlingControllerCleanupThread implements Runnable {
	private String idKey;
	private ServletContext context;

	public ThrottlingControllerCleanupThread(ServletContext context, String idKey) {
		this.context = context;
		this.idKey = idKey;
	}

	@Override
	public void run() {
		Object oControllers = context.getAttribute(idKey);
		if (null != oControllers && oControllers instanceof Map<?, ?>) {
			@SuppressWarnings("unchecked")
			Map<String, ThrottlingController> controller = (Map<String, ThrottlingController>) oControllers;
			for (Entry<String, ThrottlingController> entry: controller.entrySet()) {
				if (entry.getValue().isExpired()) {
					controller.remove(entry.getKey());
				}
			}
		}
	}
}
