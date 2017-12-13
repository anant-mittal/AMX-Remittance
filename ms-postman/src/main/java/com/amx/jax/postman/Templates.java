package com.amx.jax.postman;

public enum Templates {
	
	RESET_OTP("reset-otp"), REMIT_RECEIPT("RemittanceReceiptReport");

	String fileName;

	public String getFileName() {
		return fileName;
	}

	Templates(String fileName) {
		this.fileName = fileName;
	}
}
