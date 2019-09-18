package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.ITemplates.ITemplate;
import com.amx.jax.postman.model.Notipy.Channel;

public enum TemplatesMX implements ITemplate {

	CONTACT_US("ContactForm"),
	RESET_OTP("reset-otp", Channel.NOTIPY),
	RESET_OTP_SMS("reset-otp-sms", Channel.NOTIPY),

	// Contact Verification
	CONTACT_VERIFICATION_SMS("contact-verifiy-sms", Channel.NOTIPY),
	CONTACT_VERIFICATION_EMAIL("contact-verifiy-email", Channel.NOTIPY),

	SERVER_UP("health-server-up"),

	REMIT_RECEIPT("TransactionReceipt"), REMIT_RECEIPT2("TransactionReceipt2"),
	REMIT_RECEIPT_JASPER("TransactionReceipt_jasper", PDFConverter.JASPER, "TransactionReceipt.json"),
	REMIT_RECEIPT_COPY_JASPER("TransactionReceiptCopy_jasper", PDFConverter.JASPER, "TransactionReceipt.json"),
	REMIT_RECEIPT_JASPER_NO_HEADER("TransactionReceiptNoHeader_jasper", PDFConverter.JASPER, "TransactionReceipt.json"),
	REMIT_APPLICATION_RECEIPT_JASPER("ApplicationReceipt_jasper", PDFConverter.JASPER, "ApplicationReceipt.json"),

	REMIT_RECEIPT_COPY("TransactionReceiptCopy"), REMIT_STATMENT("TransactionList"),
	REMIT_STATMENT_EMAIL("RemittanceStatmentEmail"),
	REMIT_STATMENT_EMAIL_FILE("TransactionStatementHistory"),

	REG_SUC("RegistrationSuccessEmail"),

	TXN_CRT_SUCC("TransactionCreationSuccess"),
	PROFILE_CHANGE("AccoutDetailsUpdate"),

	// Rate Alert, PlaceOrder
	RATE_ALERT("place-order"), RATE_ALERT_COMPLETION("place-order-executed"),

	BRANCH_SEARCH_EMPTY("BranchSearchEmpty"), TEST("test"), BRANCH_FEEDBACK("trnx-feedback", Channel.FEED), TT("tt", Channel.FEED), EFT("eft", Channel.FEED),
	CASH("cash", Channel.FEED),

	CIVILID_EXPIRY("civilexpiry"), CIVILID_EXPIRED("civilexpired"),

	PARTIAL_REGISTRATION_EMAIL("PartialRegistrationCompletionEMail"), TRANSACTION_FAILURE("TransactionFailure"),

	PROMOTION_WINNER("PromotionWinner"), PROMOTION_COUPON("PromotionCoupon"),
	WANTIT_BUYIT_PROMOTION("WantITBuyItPromotionCoupon"),

	SERVER_PING("server-ping"),

	MARKETING_PUSH("mrkt-push", Channel.FEED),

	/// FC Templates
	FC_DELIVER_EMAIL_OTP("deliver-email-otp", Channel.NOTIPY), FC_DELIVER_SMS_OTP("deliver-sms-otp", Channel.NOTIPY),
	FC_ORDER_SUCCESS("order-success"),
	FC_KNET_SUCCESS("knet-success"),
	FXO_RECEIPT("FXO_RECEIPT", PDFConverter.JASPER, "fxo-receipt.json"),
	FXO_RECEIPT_BRANCH("FXO_RECEIPT_BRANCH", PDFConverter.JASPER, "fxo-receipt.json"),
	FXO_STATMENT("FxoTransactionList"),
	SUSPICIOUS_USER("suspicious-user"), PROFILE_CHANGE_SMS("profile-change-sms", Channel.ALERTY),
	EMAIL_CHANGE_OLD_EMAIL("EmailChangeOldEmail"),
	FINGERPRINT_LINKED_SUCCESS("FingerprintLinkedSuccess"),
	FINGERPRINT_DELINKED_SUCCESS("FingerprintDelinkedSuccess"),
	FINGERPRINT_DELINKED_ATTEMP_SUCCESS("FingerprintDelinkInCorrectAttem"),
	
	
	// GIG Policy Event Notification Templates
	POLICY_CONFIRMATION("PolicyConfirmation"),
	POLICY_OPTOUT_CUSTOMER("PolicyOptoutCustomer"),
	POLICY_OPTOUT_SYSTEM("PolicyOptoutSystem"),
	POLICY_EXPIRY_REMINDER("PolicyExpiryReminder"),
	POLICY_EXPIRED("PolicyExpired"),
	POLICY_PENDING_TRNX("PolicyPendingTrnx"),

	HOMESEND_TRANSACTION_FAILAURE("HomeSendTransactionFailure"),

	//Add Bene Templates
	BENE_SUCC("BeneCreationSuccess"),
	BENE_SUCC_SMS("bene-success-sms",Channel.NOTIPY),

	// Default add enums above this
	DEFAULT("default");

	String fileName;
	PDFConverter converter;
	String sampleJSON;
	boolean thymleaf = true;
	Channel channel = null;

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getHtmlFile() {
		return "html/" + getFileName();
	}

	@Override
	public String getSMSFile() {
		return "html/sms/" + getFileName();
	}

	@Override
	public String getJsonFile() {
		return "json/" + getFileName();
	}

	TemplatesMX(String fileName, PDFConverter converter, String sampleJSON, Channel channel) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
		this.channel = channel;
	}

	TemplatesMX(String fileName, PDFConverter converter, String sampleJSON) {
		this(fileName, converter, sampleJSON, null);
	}

	TemplatesMX(String fileName, PDFConverter converter) {
		this(fileName, converter, null, null);
	}

	TemplatesMX(String fileName, Channel channel) {
		this(fileName, null, null, channel);
	}

	TemplatesMX(String fileName) {
		this(fileName, null, null, null);
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

	@Override
	public Channel getChannel() {
		return channel;
	}

	public String toString() {
		return this.name();
	}

}
