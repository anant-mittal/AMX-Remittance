package com.amx.jax.model.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

public class VerifyCustomerContactRequest {

	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid Mobile No")
	String mobile;
	
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid Tele Prefix")
	String telPrefix;
	
	@Email
	String email;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelPrefix() {
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
