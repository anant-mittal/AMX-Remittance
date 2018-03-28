package com.amx.jax.postman.model;

public enum Templates {

	CONTACT_US("ContactForm"),
	RESET_OTP("reset-otp"), 
	RESET_OTP_SMS("reset-otp-sms"), 
	SERVER_UP("health-server-up"), 
	REMIT_RECEIPT("TransactionReceipt"), 
	REMIT_RECEIPT2("TransactionReceipt2"), 
	REMIT_RECEIPT_COPY("TransactionReceiptCopy"), 
	REMIT_STATMENT("TransactionList"), 
	REMIT_STATMENT_EMAIL("RemittanceStatmentEmail"),
	REMIT_STATMENT_EMAIL_FILE("TransactionStatementHistory"),
	REG_SUC("RegistrationSuccessEmail"), 
	SERVER_PING("server-ping"),
	TXN_CRT_SUCC("TransactionCreationSuccess"),
	PROFILE_CHANGE("AccoutDetailsUpdate"),
	RATE_ALERT("RateAlert"),
	TEST("test");

	String fileName;

	public String getFileName() {
		return fileName;
	}
	
	Templates(String fileName) {
		this.fileName = fileName;
	}
}
