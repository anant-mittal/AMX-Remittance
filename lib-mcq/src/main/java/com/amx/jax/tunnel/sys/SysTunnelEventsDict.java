package com.amx.jax.tunnel.sys;

import com.amx.jax.tunnel.ITunnelEventsDict;

public enum SysTunnelEventsDict implements ITunnelEventsDict {

	SHARED_CONFIG_UPDATE(Names.SHARED_CONFIG_UPDATE);

	public static final class Names {
		public static final String SHARED_CONFIG_UPDATE = "SHARED_CONFIG_UPDATE";
	}

	String eventName;

	SysTunnelEventsDict(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String getEventCode() {
		return this.eventName;
	}
}
