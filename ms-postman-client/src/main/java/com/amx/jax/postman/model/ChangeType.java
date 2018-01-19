package com.amx.jax.postman.model;

public enum ChangeType {

	PASSWORD_CHANGE("reset-otp"), 
	SECURITY_QUESTION_CHANGE("reset-otp-sms"), 
	IMAGE_CHANGE("health-server-up"), 
	MOBILE_CHANGE("RemittanceReceiptReport");

	String fileName;

	public String getFileName() {
		return fileName;
	}

	ChangeType(String fileName) {
		this.fileName = fileName;
	}
}

