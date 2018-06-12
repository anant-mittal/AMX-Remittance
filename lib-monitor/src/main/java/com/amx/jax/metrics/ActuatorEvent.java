package com.amx.jax.metrics;

import java.util.SortedMap;

import com.amx.jax.logger.AbstractAuditEvent;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

public class ActuatorEvent extends AbstractAuditEvent {

	private SortedMap<String, Gauge> gauges;
	private SortedMap<String, Counter> counters;
	private SortedMap<String, Histogram> histograms;
	private SortedMap<String, Meter> meters;
	private SortedMap<String, Timer> timers;

	public ActuatorEvent(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
			SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
		this.setGauges(gauges);
		this.counters = counters;
		this.histograms = histograms;
		this.meters = meters;
		this.timers = timers;

	}

	public SortedMap<String, Gauge> getGauges() {
		return gauges;
	}

	public void setGauges(SortedMap<String, Gauge> gauges) {
		this.gauges = gauges;
	}

	public SortedMap<String, Counter> getCounters() {
		return counters;
	}

	public void setCounters(SortedMap<String, Counter> counters) {
		this.counters = counters;
	}

	public SortedMap<String, Histogram> getHistograms() {
		return histograms;
	}

	public void setHistograms(SortedMap<String, Histogram> histograms) {
		this.histograms = histograms;
	}

	public SortedMap<String, Meter> getMeters() {
		return meters;
	}

	public void setMeters(SortedMap<String, Meter> meters) {
		this.meters = meters;
	}

	public SortedMap<String, Timer> getTimers() {
		return timers;
	}

	public void setTimers(SortedMap<String, Timer> timers) {
		this.timers = timers;
	}
}