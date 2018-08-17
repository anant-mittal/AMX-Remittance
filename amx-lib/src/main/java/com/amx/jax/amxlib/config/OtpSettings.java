package com.amx.jax.amxlib.config;

import org.springframework.stereotype.Component;

@Component
public class OtpSettings {

	Integer maxValidateOtpAttempts;
	Integer maxSendOtpAttempts;
	Integer otpValidityTime;

	public OtpSettings(Integer maxValidateOtpAttempts,Integer maxSendOtpAttempts,Integer otpValidityTime) {
		this.maxValidateOtpAttempts=maxValidateOtpAttempts;
		this.maxSendOtpAttempts=maxSendOtpAttempts;
		this.otpValidityTime=otpValidityTime;
	}
	
	//default
	public OtpSettings() {

	}
	
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
