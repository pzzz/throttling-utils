package de.pzzz.throttling.utils.shared.meters;

public class FixedOccurrenceMeter implements RateMeter {
	private final Long[] values;
	private final int size;

	private long currentMark = 0;
	private int currentIndex = 0;

	public FixedOccurrenceMeter(final int size) {
		this.size = size;
		this.values = new Long[this.size];
	}

	@Override
	public void mark() {
		mark(1);
	}

	@Override
	public synchronized void mark(long value) {
		increaseIndex();
        if (null != values[currentIndex]) {
        	currentMark -= values[currentIndex];
		}
        currentMark += value;
        values[currentIndex] = value;
	}

    private void increaseIndex() {
    	int target = currentIndex + 1;
    	if (target >= size) {
    		target -= size;
    	}
    	currentIndex = target;
    }

	@Override
	public double getRate() {
		return ((double) getMark()) / size;
	}

	@Override
	public synchronized long getMark() {
		return currentMark;
	}

	@Override
	public synchronized boolean isExpired() {
		return false;
	}

	@Override
	public String rateUnit() {
		return "average of " + size;
	}
}
