package com.amx.jax.rbaac.dto;

import java.math.BigDecimal;

import com.amx.jax.constant.DeviceState;
import com.amx.jax.dict.UserClient.ClientType;

public class DevicePairOtpResponse {

	String otp;

	String termialId;
	String empId;

	BigDecimal deviceRegId;

	DeviceState deviceState;

	String sessionPairToken;

	ClientType deviceType;

	public BigDecimal getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(BigDecimal deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	public String getTermialId() {
		return termialId;
	}

	public void setTermialId(String termialId) {
		this.termialId = termialId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getSessionPairToken() {
		return sessionPairToken;
	}

	public void setSessionPairToken(String sessionPairToken) {
		this.sessionPairToken = sessionPairToken;
	}

	public DeviceState getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public ClientType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(ClientType deviceType) {
		this.deviceType = deviceType;
	}
}
