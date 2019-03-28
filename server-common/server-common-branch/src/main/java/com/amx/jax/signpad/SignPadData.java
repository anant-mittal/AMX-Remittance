package com.amx.jax.signpad;

import java.io.Serializable;

import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignPadData implements Serializable {
	private static final long serialVersionUID = -6489044552822849830L;
	FileSubmitRequestModel signature;
	DeviceStatusInfoDto stateData;
	DeviceState deviceState;
	long updatestamp;

	public long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}

	public DeviceState getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}

	public DeviceStatusInfoDto getStateData() {
		return stateData;
	}

	public void setStateData(DeviceStatusInfoDto stateData) {
		this.stateData = stateData;
	}

	public FileSubmitRequestModel getSignature() {
		return signature;
	}

	public void setSignature(FileSubmitRequestModel signature) {
		this.signature = signature;
	}
}