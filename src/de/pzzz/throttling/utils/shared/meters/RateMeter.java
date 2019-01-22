package de.pzzz.throttling.utils.shared.meters;

/**
 * To be implemented by implementations that can return the rate.
 *
 * @author <a href="mailto:ps@pzzz.de">Peter Schuller</a>
 */
public interface RateMeter {
	/**
	 * Gives the current rate measured by this meter.
	 * @return
	 */
	double getRate();
	/**
	 * Returns a unit string used for human-readable logging of values.
	 * @return
	 */
	String rateUnit();
	/**
	 * Marks one occurrence at this meter.
	 */
	void mark();
	/**
	 * Marks occurrences at this meter.
	 * @param value
	 */
	void mark(long value);
	/**
	 * Returns the current mark used to compute the rate.
	 * @return
	 */
	long getMark();
	/**
	 * Checks whether this meter is expired and not valid any more.
	 * @return
	 */
	boolean isExpired();
}
