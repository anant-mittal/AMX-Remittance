package com.amx.amxlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CivilIdOtpModel extends AbstractModel {

	private String otp;

	@JsonIgnore
	private String hashedOtp;

	private String otpPrefix = null;

	public String getOtpPrefix() {
		return otpPrefix;
	}

	public void setOtpPrefix(String otpPrefix) {
		this.otpPrefix = otpPrefix;
	}

	private String email;

	private String mobile;

	private Boolean isActiveCustomer;

	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private String lastName;

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

	public Boolean getIsActiveCustomer() {
		return isActiveCustomer;
	}

	public void setIsActiveCustomer(Boolean isActiveCustomer) {
		this.isActiveCustomer = isActiveCustomer;
	}

	@Override
	public String getModelType() {
		return "otp";
	}

}
