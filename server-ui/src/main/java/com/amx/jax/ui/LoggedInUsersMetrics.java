package com.amx.jax.ui;

import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Gauge;

@Component
public class LoggedInUsersMetrics {

	@Gauge
	public int getTotalUsers() {
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
