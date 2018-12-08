package com.amx.jax.stomp;

import com.amx.jax.tunnel.TunnelEvent;

public class StompTunnelEvent extends TunnelEvent {
	private static final long serialVersionUID = 1426912782817649062L;

	private String topic;
	private String user;
	private Object data;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
