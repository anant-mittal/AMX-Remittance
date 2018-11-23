package com.amx.jax.broker;

import com.amx.jax.tunnel.TunnelEvent;

public class DBEvents extends TunnelEvent {
	private static final long serialVersionUID = 1426912782817649062L;

	private String priority;
	private String description;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Event [event_code=" + eventCode + ", priority=" + priority + ", description=" + description + ", data="
				+ data + "]";
	}

}
