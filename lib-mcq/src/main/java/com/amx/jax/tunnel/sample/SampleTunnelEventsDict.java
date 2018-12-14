package com.amx.jax.tunnel.sample;

import com.amx.jax.tunnel.ITunnelEventsDict;

public enum SampleTunnelEventsDict implements ITunnelEventsDict {

	TEST_TOPIC(Names.TEST_TOPIC);

	public static final class Names {
		public static final String TEST_TOPIC = "TEST_TOPIC";
	}

	String eventName;

	SampleTunnelEventsDict(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String getEventCode() {
		return this.eventName;
	}
}
