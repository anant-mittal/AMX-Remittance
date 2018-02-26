package com.amx.jax.metrics;

import com.codahale.metrics.annotation.Gauge;

public class LoggedInUsersMetrics {

	@Gauge
	public int getAvailableConnections() {
		return 5;
	}

	@Gauge
	public int getLeasedConnections() {
		return 5;
	}

	@Gauge
	public int getPendingConnections() {
		return 8;
	}

}
