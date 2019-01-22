package de.pzzz.throttling.utils.adaption;

public interface AdaptionStrategy {
	boolean isAdapt();
	double adaptionStepSize();
}
