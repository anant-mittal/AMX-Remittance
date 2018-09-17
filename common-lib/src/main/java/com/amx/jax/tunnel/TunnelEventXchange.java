package com.amx.jax.tunnel;

public enum TunnelEventXchange {
	/**
	 * Event will be delivered to Only Component
	 */
	SHOUT_LISTNER("SG"),

	SEND_LISTNER("SG"),
	/**
	 * For Audit purpose only
	 */
	AUDIT("AUDIT");

	String queuePrefix;

	TunnelEventXchange(String queue) {
		this.queuePrefix = queue;
	}

	TunnelEventXchange() {
		this.queuePrefix = null;
	}

	public String getTopic(String topic) {
		return queuePrefix + "_T_" + topic;
	}

	public String getQueue(String topic) {
		return queuePrefix + "_Q_" + topic;
	}

	public String getStatusMap(String topic) {
		return queuePrefix + "_M_" + topic;
	}

	public String getEventMap(String topic) {
		return queuePrefix + "_E_" + topic;
	}

}