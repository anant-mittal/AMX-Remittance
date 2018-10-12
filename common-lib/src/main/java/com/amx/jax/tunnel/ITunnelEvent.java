package com.amx.jax.tunnel;

import java.io.Serializable;
import java.util.Map;

public class ITunnelEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String eventCode;
	protected Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

}