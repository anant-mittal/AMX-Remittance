package com.amx.jax.postman.model;

public enum Templates {

	RESET_OTP("reset-otp"), RESET_OTP_SMS("reset-otp-sms"), SERVER_UP("health-server-up"), REMIT_RECEIPT(
			"RemittanceReceiptReport");

	String fileName;

	public String getFileName() {
		return fileName;
	}

	Templates(String fileName) {
		this.fileName = fileName;
	}
}
