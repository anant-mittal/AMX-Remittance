package com.amx.amxlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CivilIdOtpModel extends AbstractModel {

	private String mOtp;

	private String eOtp;

	@JsonIgnore
	private String mHashedOtp;

	@JsonIgnore
	private String eHashedOtp;

	private String mOtpPrefix = null;
	
	private String eOtpPrefix = null;

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

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String otp) {
		this.mOtp = otp;
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

	public String getHashedmOtp() {
		return mHashedOtp;
	}

	public void setHashedmOtp(String hashedOtp) {
		this.mHashedOtp = hashedOtp;
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

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public String getHashedeOtp() {
		return eHashedOtp;
	}

	public void setHashedeOtp(String eHashedOtp) {
		this.eHashedOtp = eHashedOtp;
	}

	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

}
