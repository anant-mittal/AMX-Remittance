package com.amx.jax;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonPropertyOrder({ "param", "enabled" })
public enum AppParam {

	PRINT_TRACK_BODY(false);

	@JsonProperty("_id")
	String param;

	boolean enabled;

	AppParam(boolean enabled) {
		this.param = this.name();
		this.enabled = enabled;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
