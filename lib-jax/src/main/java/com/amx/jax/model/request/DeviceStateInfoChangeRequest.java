package com.amx.jax.model.request;

import com.amx.jax.constants.DeviceState;

public class DeviceStateInfoChangeRequest {

	DeviceState state;

	public DeviceState getState() {
		return state;
	}

	public void setState(DeviceState state) {
		this.state = state;
	}
}
