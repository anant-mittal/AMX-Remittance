package com.amx.jax.stomp;

import com.amx.jax.tunnel.TunnelEvent;

public class StompTunnelEvent extends TunnelEvent {
	private static final long serialVersionUID = 1426912782817649062L;

	private String topic;
	private String httpSessionId;
	private Object data;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

}
