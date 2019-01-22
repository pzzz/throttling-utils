package de.pzzz.throttling.utils;

import de.pzzz.throttling.utils.adaption.AdaptionStrategy;

/**
 * Can adapt it's behavior to increase limits or to react to a detected
 * throttling.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 */
public interface IsAdaptable {
	/**
	 * Reacts to the detection of throttling for the current request by decreasing any limit in this class
	 *.
	 * @param adaptionStepSize see {@link AdaptionStrategy#adaptionStepSize()}
	 */
	void setThrottlingDetected(double adaptionStepSize);
	/**
	 * Is used to increase any limit in this class.
	 *
	 * @param adaptionStepSize see {@link AdaptionStrategy#adaptionStepSize()}
	 */
	void increaseLimits(double adaptionStepSize);
}
