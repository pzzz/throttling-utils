package de.pzzz.throttling.utils.shared.meters;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class FixedFrameMeter implements RateMeter {
	private final long resolution;
	private final long duration;
	private final TimeUnit unit;
	private final Long[] values;
	private final int size;

	private final Clock clock;
    private long lastTick;

	private long currentMark = 0;
	private int currentIndex = 0;

	public FixedFrameMeter(final long duration) {
		this(duration, 1);
	}

	public FixedFrameMeter(final MeterConfig config) {
		this(config.getDuration(), config.getResolution(), config.getUnit(), config.getClock());
	}

	public FixedFrameMeter(final long duration, final long resolution) {
		this(duration, resolution, TimeUnit.SECONDS);
	}

	public FixedFrameMeter(final long duration, final long resolution, final TimeUnit unit) {
		this(duration, resolution, unit, Clock.systemUTC());
	}

	public FixedFrameMeter(final long duration, final long resolution, final TimeUnit unit, final Clock clock) {
		this.resolution = unit.toMillis(resolution);
		this.duration = duration;
		this.unit = unit;
		//TODO: improve calculation! adapt either duration or resolution
		this.size = (int) (unit.toMillis(this.duration) / this.resolution);
		this.values = new Long[size];
        this.clock = clock;
        this.lastTick = this.clock.millis();
	}

	@Override
	public void mark() {
		mark(1);
	}

	@Override
	public synchronized void mark(long value) {
        skipIfNecessary();
        if (null != values[currentIndex]) {
        	values[currentIndex] = values[currentIndex] + value;
		} else {
			values[currentIndex] = value;
		}
        currentMark += value;
	}

    private synchronized void skipIfNecessary() {
        final long newTick = clock.millis();
        long age = newTick - lastTick;
        while (age >= resolution) {
        	skipNextValue();
        	age -= resolution;
        	lastTick += resolution;
        }
    }

    private void skipNextValue() {
    	int nextIndex = getNextIndex();
    	if (null != values[nextIndex]) {
    		currentMark -= values[nextIndex];
    	}
    	values[nextIndex] = 0L;
    	currentIndex = nextIndex;
    }

    private int getNextIndex() {
    	int target = currentIndex + 1;
    	if (target >= size) {
    		target -= size;
    	}
    	return target;
    }

	@Override
	public double getRate() {
		return ((double) getMark()) / duration;
	}

	@Override
	public synchronized long getMark() {
        skipIfNecessary();
		return currentMark;
	}

	@Override
	public synchronized boolean isExpired() {
        final long newTick = clock.millis();
        long age = newTick - lastTick;
		return age > unit.toMillis(duration);
	}

	@Override
	public String rateUnit() {
		return "x/" + unit;
	}
}
