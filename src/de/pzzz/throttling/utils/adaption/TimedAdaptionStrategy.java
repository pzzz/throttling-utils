package de.pzzz.throttling.utils.adaption;

import java.time.Clock;

public class TimedAdaptionStrategy implements AdaptionStrategy {
	private long nextAdaption;
	private final long adaptionTime;
	private final double adaptionStepSize;
	private final Clock clock;

	public TimedAdaptionStrategy(long adaptionTime, double adaptionStepSize) {
		this(adaptionTime, adaptionStepSize, Clock.systemUTC());
	}


	public TimedAdaptionStrategy(long adaptionTime, double adaptionStepSize, Clock clock) {
		this.adaptionTime = adaptionTime;
		this.adaptionStepSize = adaptionStepSize;
		this.clock = clock;
		nextAdaption = clock.millis() + adaptionTime;
	}

	@Override
	public boolean isAdapt() {
		long now = clock.millis();
		if (now > nextAdaption) {
			nextAdaption = now + adaptionTime;
			return true;
		}
		return false;
	}

	@Override
	public double adaptionStepSize() {
		return adaptionStepSize;
	}
}
