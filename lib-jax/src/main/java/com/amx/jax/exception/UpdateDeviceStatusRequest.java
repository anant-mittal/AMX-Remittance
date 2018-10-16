package com.amx.jax.exception;

import com.amx.jax.constants.DeviceState;

public class UpdateDeviceStatusRequest {

	DeviceState deviceState;

	String deviceInfo;

	public DeviceState getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

}
