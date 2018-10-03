package com.amx.jax.event;

import com.amx.jax.tunnel.ITunnelEvents;

public enum AmxTunnelEvents implements ITunnelEvents {
	CIVIL_ID_EXPIRY, CUSTOMER_BIRTHDATE_GREETING, XRATE_BEST_RATE_CHANGE, TRNX_BENE_CREDIT,
	// tests events
	TEST_EVENT, PING_SEND, PING_SHOUT, PING_TASK;

	public static final class Names {
		public static final String CIVIL_ID_EXPIRY = "CIVIL_ID_EXPIRY";
		public static final String CUSTOMER_BIRTHDATE_GREETING = "CUSTOMER_BIRTHDATE_GREETING";
		public static final String XRATE_BEST_RATE_CHANGE = "XRATE_BEST_RATE_CHANGE";
		public static final String TEST_EVENT = "TEST_EVENT";
		public static final String PING_SEND = "PING_SEND";
		public static final String PING_SHOUT = "PING_SHOUT";
		public static final String PING_TASK = "PING_TASK";
		public static final String TRNX_BENE_CREDIT = "TRNX_BENE_CREDIT";
	}

	@Override
	public String getEventCode() {
		return this.name();
	}
}
