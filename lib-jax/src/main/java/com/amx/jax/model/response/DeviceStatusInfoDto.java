package com.amx.jax.model.response;

import com.amx.jax.constants.DeviceState;
import com.amx.jax.constants.DeviceStateDataType;

public class DeviceStatusInfoDto {

	DeviceState deviceState;

	IDeviceStateData stateData;

	DeviceStateDataType stateDataType;

	public DeviceState getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}


	public DeviceStateDataType getStateDataType() {
		return stateDataType;
	}

	public void setStateDataType(DeviceStateDataType stateDataType) {
		this.stateDataType = stateDataType;
	}

	public IDeviceStateData getStateData() {
		return stateData;
	}

	public void setStateData(IDeviceStateData stateData) {
		this.stateData = stateData;
	}
}
