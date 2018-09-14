package com.amx.jax.event;

import com.amx.jax.tunnel.ITunnelEvents;

public enum AmxTunnelEvents implements ITunnelEvents {
	CIVIL_ID_EXPIRY, CUSTOMER_BIRTHDATE_GREETING, XRATE_BEST_RATE_CHANGE, TEST_EVENT,
	// tests events
	PING_INIT, PING_SINGLE, PING_MULTIPLE;

	public static final class Names {
		public static final String CIVIL_ID_EXPIRY = "CIVIL_ID_EXPIRY";
		public static final String CUSTOMER_BIRTHDATE_GREETING = "CUSTOMER_BIRTHDATE_GREETING";
		public static final String XRATE_BEST_RATE_CHANGE = "XRATE_BEST_RATE_CHANGE";
		public static final String TEST_EVENT = "TEST_EVENT";
		public static final String PING_INIT = "PING_SINGLE";
		public static final String PING_SINGLE = "PING_SINGLE";
		public static final String PING_MULTIPLE = "PING_MULTIPLE";
	}

	@Override
	public String getEventCode() {
		return this.name();
	}
}
