package de.pzzz.throttling.utils;

import java.util.Map;

import de.pzzz.throttling.utils.shared.meters.RateMeter;

/**
 * To be implemented by processors that measure rates.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 */
public interface HasMeters {
	/**
	 * Returns all meters mapped to their identifier.
	 *
	 * @return
	 */
	Map<String, RateMeter> getMeters();
}
