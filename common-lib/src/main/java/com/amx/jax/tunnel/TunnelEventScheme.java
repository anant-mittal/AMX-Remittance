package com.amx.jax.tunnel;

public enum TunnelEventScheme {
	ONCE_PER_COMPONENT, AUDIT("AUDIT");

	String queuePrefix;

	TunnelEventScheme(String queue) {
		this.queuePrefix = queue;
	}

	TunnelEventScheme() {
		this.queuePrefix = null;
	}

	public String getTopic(String topic) {
		return queuePrefix + "_T_" + topic;
	}

	public String getQueue(String topic) {
		return queuePrefix + "_Q_" + topic;
	}

}
