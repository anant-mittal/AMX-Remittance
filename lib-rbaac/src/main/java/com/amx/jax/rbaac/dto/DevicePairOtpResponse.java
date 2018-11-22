package com.amx.jax.rbaac.dto;

import java.math.BigDecimal;

public class DevicePairOtpResponse {

	String otp;

	String termialId;

	BigDecimal deviceRegId;

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

	String sessionPairToken;

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
}
