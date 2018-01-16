package com.amx.jax.config;

import org.springframework.stereotype.Component;

@Component
public class OtpSettings {

	Integer maxValidateOtpAttempts;
	Integer maxSendOtpAttempts;
	Integer otpValidityTime;

	public Integer getMaxValidateOtpAttempts() {
		return maxValidateOtpAttempts;
	}

	public void setMaxValidateOtpAttempts(Integer maxValidateOtpAttempts) {
		this.maxValidateOtpAttempts = maxValidateOtpAttempts;
	}

	public Integer getMaxSendOtpAttempts() {
		return maxSendOtpAttempts;
	}

	public void setMaxSendOtpAttempts(Integer maxSendOtpAttempts) {
		this.maxSendOtpAttempts = maxSendOtpAttempts;
	}

	public Integer getOtpValidityTime() {
		return otpValidityTime;
	}

	public void setOtpValidityTime(Integer otpValidityTime) {
		this.otpValidityTime = otpValidityTime;
	}

	public static final String getType() {
		return "OTP_SETTINGS";
	}

	@Override
	public String toString() {
		return "OtpSettings [maxValidateOtpAttempts=" + maxValidateOtpAttempts + ", maxSendOtpAttempts="
				+ maxSendOtpAttempts + ", otpValidityTime=" + otpValidityTime + "]";
	}
}
