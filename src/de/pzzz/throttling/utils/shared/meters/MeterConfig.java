package de.pzzz.throttling.utils.shared.meters;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class MeterConfig {
	private long duration;
	private long resolution;
	private TimeUnit unit;
	private Clock clock;

	public static MeterConfig meter1minute1second() {
		return new MeterConfig(TimeUnit.MINUTES.toSeconds(1));
	}

	public static MeterConfig meter5minutes1second() {
		return new MeterConfig(TimeUnit.MINUTES.toSeconds(5));
	}

	public static MeterConfig meter15minutes1second() {
		return new MeterConfig(TimeUnit.MINUTES.toSeconds(15));
	}

	public static MeterConfig meter1hour1second() {
		return new MeterConfig(TimeUnit.HOURS.toSeconds(1));
	}

	public static MeterConfig meter24hours1minute() {
		return new MeterConfig(TimeUnit.HOURS.toSeconds(24), TimeUnit.MINUTES.toSeconds(1));
	}

	public MeterConfig(long duration) {
		this(duration, 1);
	}

	public MeterConfig(long duration, long resolution) {
		this(duration, resolution, TimeUnit.SECONDS);
	}

	public MeterConfig(long duration, long resolution, TimeUnit unit) {
		this(duration, resolution, unit, Clock.systemUTC());
	}

	public MeterConfig(long duration, long resolution, TimeUnit unit, Clock clock) {
		super();
		this.duration = duration;
		this.resolution = resolution;
		this.unit = unit;
		this.clock = clock;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getResolution() {
		return resolution;
	}

	public void setResolution(long resolution) {
		this.resolution = resolution;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}
}
