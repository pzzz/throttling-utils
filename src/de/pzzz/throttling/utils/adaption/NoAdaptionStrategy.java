package de.pzzz.throttling.utils.adaption;

public class NoAdaptionStrategy implements AdaptionStrategy {

	@Override
	public boolean isAdapt() {
		return false;
	}

	@Override
	public double adaptionStepSize() {
		return 0;
	}
}
