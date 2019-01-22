package de.pzzz.throttling.utils.metrics;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;

import de.pzzz.throttling.utils.shared.meters.FixedFrameMeter;

public class FixedFrameGaugeSet implements MetricSet {
	private final FixedFrameMeter meter;

	public FixedFrameGaugeSet(FixedFrameMeter meter) {
		this.meter = meter;
	}

	@Override
	public Map<String, Metric> getMetrics() {
        final Map<String, Metric> gauges = new HashMap<>();
        gauges.put("rate", getRateGauge());
        gauges.put("mark", getMarkGauge());
		return gauges;
	}


	private Gauge<Double> getRateGauge() {
		return new Gauge<Double>() {
			@Override
			public Double getValue() {
				return meter.getRate();
			}
		};
	}

	private Gauge<Long> getMarkGauge() {
		return new Gauge<Long>() {
			@Override
			public Long getValue() {
				return meter.getMark();
			}
		};
	}
}
