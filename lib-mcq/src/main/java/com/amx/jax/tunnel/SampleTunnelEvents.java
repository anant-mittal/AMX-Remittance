package com.amx.jax.tunnel;

public enum SampleTunnelEvents implements ITunnelEventsDict {

	TEST_TOPIC(Names.TEST_TOPIC);

	public static final class Names {
		public static final String TEST_TOPIC = "TEST_TOPIC";
	}

	String eventName;

	SampleTunnelEvents(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String getEventCode() {
		return this.eventName;
	}
}
