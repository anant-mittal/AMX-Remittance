package com.amx.jax.event;

import com.amx.jax.tunnel.ITunnelEvents;

public enum AmxTunnelEvents implements ITunnelEvents {
	CIVIL_ID_EXPIRY, CUSTOMER_BIRTHDATE_GREETING, TEST_EVENT;
	@Override
	public String getEventCode() {
		return null;
	}
}
