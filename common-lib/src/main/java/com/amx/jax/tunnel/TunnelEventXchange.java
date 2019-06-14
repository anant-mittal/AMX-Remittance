package com.amx.jax.tunnel;

import com.amx.jax.AppParam;

public enum TunnelEventXchange {
	/**
	 * Event will be delivered to ALL Components
	 * 
	 * @see com.amx.jax.tunnel.ITunnelService#shout(String, Object)
	 */
	SHOUT_LISTNER("SD"),

	/**
	 * Events deleivered by
	 * 
	 * @see com.amx.jax.tunnel.ITunnelService#send(String, Object)
	 * 
	 */
	SEND_LISTNER("SH"),

	/**
	 * Only one worker will start working
	 * 
	 */
	TASK_WORKER("TW"),

	/**
	 * For Audit purpose only
	 */
	AUDIT("AD");

	String queuePrefix;

	TunnelEventXchange(String queue) {
		this.queuePrefix = queue;
	}

	TunnelEventXchange() {
		this.queuePrefix = null;
	}

	public String getTopic(String topic) {
		return AppParam.APP_ENV.getValue() + "_" + queuePrefix + "_T_" + topic;
	}

	public String getQueue(String topic) {
		return AppParam.APP_ENV.getValue() + "_" + queuePrefix + "_Q_" + topic;
	}

	public String getStatusMap(String topic) {
		return AppParam.APP_ENV.getValue() + "_" + queuePrefix + "_M_" + topic;
	}

	public String getEventMap(String topic) {
		return AppParam.APP_ENV.getValue() + "_" + queuePrefix + "_E_" + topic;
	}

}
