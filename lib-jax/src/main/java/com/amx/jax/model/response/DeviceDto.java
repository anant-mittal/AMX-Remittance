package com.amx.jax.model.response;

import java.math.BigDecimal;

public class DeviceDto {

	BigDecimal registrationId;

	String deviceId;

	String deviceType;

	public BigDecimal getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(BigDecimal registrationId) {
		this.registrationId = registrationId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
