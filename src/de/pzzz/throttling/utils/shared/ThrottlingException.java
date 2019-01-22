package de.pzzz.throttling.utils.shared;

/**
 * Exception that is thrown when a throttling is active.
 *
 * @author Peter Schuller ps@pzzz.de
 */
public class ThrottlingException extends Exception {
	private static final long serialVersionUID = -7087620568310543874L;

	/**
	 * Inherited constructor.
	 */
	public ThrottlingException() {
		super();
	}

	/**
	 * Inherited constructor.
	 *
	 * @param message Message of the Exception
	 */
	public ThrottlingException(final String message) {
		super(message);
	}

	/**
	 * Inherited constructor.
	 *
	 * @param cause Cause of the Exception
	 */
	public ThrottlingException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Inherited constructor.
	 *
	 * @param message Message of the Exception
	 * @param cause Cause of the Exception
	 */
	public ThrottlingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Inherited constructor.
	 *
	 * @param message Message of the Exception
	 * @param cause Cause of the Exception
	 * @param enableSuppression Whether the Exception can be suppressed
	 * @param writableStackTrace Whether the StackTrace is writable
	 */
	public ThrottlingException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
