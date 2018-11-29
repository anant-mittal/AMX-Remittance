package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.ITemplates.ITemplate;

public enum TemplatesMX implements ITemplate {

	CONTACT_US("ContactForm"), RESET_OTP("reset-otp"), RESET_OTP_SMS("reset-otp-sms"), SERVER_UP("health-server-up"),

	REMIT_RECEIPT("TransactionReceipt"), REMIT_RECEIPT2("TransactionReceipt2"), REMIT_RECEIPT_JASPER(
			"TransactionReceipt_jasper", PDFConverter.JASPER, "TransactionReceipt.json"),
	REMIT_RECEIPT_COPY_JASPER(
			"TransactionReceiptCopy_jasper", PDFConverter.JASPER, "TransactionReceipt.json"),

	REMIT_RECEIPT_COPY("TransactionReceiptCopy"), REMIT_STATMENT("TransactionList"), REMIT_STATMENT_EMAIL(
			"RemittanceStatmentEmail"),
	REMIT_STATMENT_EMAIL_FILE("TransactionStatementHistory"), REG_SUC(
			"RegistrationSuccessEmail"),
	TXN_CRT_SUCC(
			"TransactionCreationSuccess"),
	PROFILE_CHANGE("AccoutDetailsUpdate"),

	FXO_RECEIPT("FXO_RECIEPT", PDFConverter.JASPER),

	// Rate Alert, PlaceOrder
	RATE_ALERT("place-order"), RATE_ALERT_COMPLETION("place-order-executed"),

	BRANCH_SEARCH_EMPTY("BranchSearchEmpty"), TEST("test"), BRANCH_FEEDBACK("trnx-feedback"), BRANCH_FEEDBACK_JSON(
			"json/trnx-feedback"),

	CIVILID_EXPIRY("civilexpiry"), CIVILID_EXPIRED("civilexpired"),

	PARTIAL_REGISTRATION_EMAIL("PartialRegistrationCompletionEMail"), TRANSACTION_FAILURE("TransactionFailure"),

	PROMOTION_WINNER("PromotionWinner"), PROMOTION_COUPON("PromotionCoupon"),

	SERVER_PING("server-ping");

	String fileName;
	PDFConverter converter;
	String sampleJSON;
	boolean thymleaf = true;

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getHtmlFile() {
		return "html/" + fileName;
	}

	@Override
	public String getJsonFile() {
		return "json/" + fileName;
	}

	TemplatesMX(String fileName, PDFConverter converter, String sampleJSON) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
	}

	TemplatesMX(String fileName, PDFConverter converter) {
		this(fileName, converter, null);
	}

	TemplatesMX(String fileName, String sampleJSON) {
		this(fileName, null, sampleJSON);
	}

	TemplatesMX(String fileName) {
		this(fileName, null, null);
	}

	@Override
	public PDFConverter getConverter() {
		return converter;
	}

	@Override
	public String getSampleJSON() {
		if (sampleJSON == null) {
			return this.fileName + ".json";
		}
		return sampleJSON;
	}

	@Override
	public boolean isThymleaf() {
		return thymleaf;
	}

	public String toString() {
		return this.name();
	}

}
