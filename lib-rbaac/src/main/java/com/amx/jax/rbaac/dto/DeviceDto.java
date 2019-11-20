package com.amx.jax.rbaac.dto;

import java.math.BigDecimal;

import com.amx.jax.AmxConstants;
import com.amx.jax.constant.DeviceState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDto {

	BigDecimal registrationId;

	BigDecimal termialId;

	String deviceId;

	String deviceType;

	String deviceSecret = AmxConstants.SHH_DONT_TELL_ANYONE;

	String pairToken;

	String status;
	
	DeviceState state;

	public BigDecimal getRegistrationId() {
		return registrationId;
	}

	public DeviceState getState() {
		return state;
	}

	public void setState(DeviceState state) {
		this.state = state;
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

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceSecret() {
		return deviceSecret;
	}

	public void setDeviceSecret(String deviceSecret) {
		this.deviceSecret = deviceSecret;
	}

	public BigDecimal getTermialId() {
		return termialId;
	}

	public void setTermialId(BigDecimal termialId) {
		this.termialId = termialId;
	}

}
