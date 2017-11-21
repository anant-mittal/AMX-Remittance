package com.amx.jax.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CivilIdOtpModel extends AbstractModel {

	private String otp;

	@JsonIgnore
	private String hashedOtp;

	private String email;

	private String mobile;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHashedOtp() {
		return hashedOtp;
	}

	public void setHashedOtp(String hashedOtp) {
		this.hashedOtp = hashedOtp;
	}

}
