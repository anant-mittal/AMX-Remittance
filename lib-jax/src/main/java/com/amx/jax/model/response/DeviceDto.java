package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeviceDto {

	BigDecimal registrationId;

	String deviceId;

	String deviceType;
	
	String pairToken;
	
	@JsonIgnore
	String status;

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

	public String getPairToken() {
		return pairToken;
	}

	public void setPairToken(String pairToken) {
		this.pairToken = pairToken;
	}

	public String getStatus() {
		return status;
	}

	@JsonIgnore
	public void setStatus(String status) {
		this.status = status;
	}

}
