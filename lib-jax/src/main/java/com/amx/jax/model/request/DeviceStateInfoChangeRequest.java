package com.amx.jax.model.request;

import javax.validation.constraints.NotNull;

import com.amx.jax.constants.DeviceState;

public class DeviceStateInfoChangeRequest {

	@NotNull
	DeviceState state;

	public DeviceState getState() {
		return state;
	}

	public void setState(DeviceState state) {
		this.state = state;
	}
}
