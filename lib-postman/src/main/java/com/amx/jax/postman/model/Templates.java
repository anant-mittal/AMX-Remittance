package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;

public enum Templates {

	CONTACT_US("ContactForm"), RESET_OTP("reset-otp"), RESET_OTP_SMS("reset-otp-sms"), SERVER_UP(
			"health-server-up"), REMIT_RECEIPT("TransactionReceipt"), REMIT_RECEIPT2(
					"TransactionReceipt2"), REMIT_RECEIPT_JASPER("TransactionReceipt_jasper", PDFConverter.JASPER,
							"TransactionReceipt.json"), REMIT_RECEIPT_COPY_JASPER("TransactionReceiptCopy_jasper",
									PDFConverter.JASPER, "TransactionReceipt.json"),

	REMIT_RECEIPT_COPY("TransactionReceiptCopy"), REMIT_STATMENT("TransactionList"), REMIT_STATMENT_EMAIL(
			"RemittanceStatmentEmail"), REMIT_STATMENT_EMAIL_FILE("TransactionStatementHistory"), REG_SUC(
					"RegistrationSuccessEmail"), SERVER_PING(
							"server-ping"), TXN_CRT_SUCC("TransactionCreationSuccess"), PROFILE_CHANGE(
									"AccoutDetailsUpdate"), RATE_ALERT("RateAlert"), BRANCH_SEARCH_EMPTY(
											"BranchSearchEmpty"), TEST("test"), BRANCH_FEEDBACK("BranchTemplate"),

	BRANCH_SEARCH_EMPTY("BranchSearchEmpty"), TEST("test"), BRANCH_FEEDBACK("branch-template"), BRANCH_FEEDBACK_JSON(
			"json/branch-template"),

	CIVILID_EXPIRY_JSON("json/civilexpiry"), CIVILID_EXPIRY("civilexpiry"), CIVILID_EXPIRED(
			"civilexpired"), PARTIAL_REGISTRATION_EMAIL(
					"PartialRegistrationCompletionEMail"), TRANSACTION_FAILURE("TransactionFailure"),

	PROMOTION_WINNER("PromotionWinner"),
	PROMOTION_COUPON("PromotionCoupon"),

	SERVER_PING_JSON("json/server-ping"), SERVER_PING("server-ping");
	
	String fileName;
	PDFConverter converter;
	String sampleJSON;
	boolean thymleaf = true;
	boolean thymleafJson = false;

	public String getFileName() {
		return fileName;
	}

	Templates(String fileName, PDFConverter converter, String sampleJSON) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.fileName.startsWith("json/")) {
			this.thymleafJson = true;
		}
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
	}

	Templates(String fileName, PDFConverter converter) {
		this(fileName, converter, null);
	}

	Templates(String fileName, String sampleJSON) {
		this(fileName, null, sampleJSON);
	}

	Templates(String fileName) {
		this(fileName, null, null);
	}

	public PDFConverter getConverter() {
		return converter;
	}

	public String getSampleJSON() {
		if (sampleJSON == null) {
			return this.fileName + ".json";
		}
		return sampleJSON;
	}

	public boolean isThymleaf() {
		return thymleaf;
	}

	public boolean isThymleafJson() {
		return thymleafJson;
	}

}
