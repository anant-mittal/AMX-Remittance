package com.amx.jax;

public enum AppParam {

	PRINT_TRACK_BODY(false);

	boolean enabled;

	AppParam(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
