package com.amx.jax.rbaac.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDto {

	BigDecimal registrationId;

	String deviceId;

	String deviceType;

	String deviceSecret;

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

	public String getDeviceSecret() {
		return deviceSecret;
	}

	public void setDeviceSecret(String deviceSecret) {
		this.deviceSecret = deviceSecret;
	}

}
