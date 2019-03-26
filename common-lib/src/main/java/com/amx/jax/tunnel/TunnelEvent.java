package com.amx.jax.tunnel;

public class TunnelEvent implements ITunnelEvent {

	private static final long serialVersionUID = 1L;

	protected String eventCode;

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

}