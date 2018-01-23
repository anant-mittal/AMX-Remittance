package com.amx.jax.postman.model;

public enum Templates {

	RESET_OTP("reset-otp"), 
	RESET_OTP_SMS("reset-otp-sms"), 
	SERVER_UP("health-server-up"), 
	REMIT_RECEIPT("RemittanceReceiptReport"), 
	REMIT_STATMENT("RemittanceStatment"), 
	REG_SUC("RegistrationSuccessEmail"), 
	SERVER_PING("server-ping"),
	TXN_CRT_SUCC("TransactionCreationSuccess"),
	PROFILE_CHANGE("AccoutDetailsUpdate");

	String fileName;

	public String getFileName() {
		return fileName;
	}

	Templates(String fileName) {
		this.fileName = fileName;
	}
}

