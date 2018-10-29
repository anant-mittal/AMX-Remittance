package com.amx.jax.model.response;

public class DevicePairOtpResponse {

	String otp;

	String termialId;

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
