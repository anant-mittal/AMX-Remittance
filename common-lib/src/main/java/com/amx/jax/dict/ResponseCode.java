package com.amx.jax.dict;

public enum ResponseCode {
	
	//BANK of OMAN
	TIME_EXCEEDS_FOR_OTP("IPAY0200079","TIME_EXCEEDS_FOR_OTP"), 
	CARD_NOT_REGISTERED_FOR_OTP("IPAY0200085","CARD_NOT_REGISTERED_FOR_OTP"), 
	BIN_RANGE_NOT_ENABLED("IPAY0100207",""),
	INVALID_CARD_NUMBER("Invalid card number","Invalid card number"),
	INVALID_CARD_NUMBER_DN("IPAY0100119","Transaction denied due to invalid card number"),
	BRAND_NOT_ENABLED("IPAY0100126","Brand not enabled"),
	INAVLID_RESULT_CODE("IPAY0100134","Transaction denied due to invalid Result Code"),
	MERCHANT_ID_MISMATCH("IPAY0100129","Transaction denied due to Merchant ID mismatch"),
	TERMINALT_ID_MISMATCH("IPAY0100130","Transaction denied due to Terminal ID mismatch"),
	VALIDATE_ORG_TRANX("IPAY0100142","Problem occurred while validating original transaction"),
	UNABLE_PROCESS_TRANX("IPAY0100160","Unable to process the transaction"),
	TRANX_NOT_PROCESS_INVALID_CAVV("IPAY0100170","Transaction Not Processed due to invalid CAVV"),
	TRANX_DENIED_INVALID_UDF3("IPAY0100066","Transaction denied due to invalid UDF3"),
	TRANX_DENIED_INVALID_CVV("IPAY0100073","Transaction denied due to invalid CVV"),
	TRANX_DENIED_INVALID_EXPIRY_YEAR("IPAY0100075","Transaction denied due to invalid expiry year"),
	TRANX_DENIED_INVALID_EXPIRY_MONTH("IPAY0100077","Transaction denied due to invalid expiry month"),
	TRANX_DENIED_INVALID_EXPIRY_DAY("IPAY0100079","Transaction denied due to invalid expiry day"),
	CARD_HOLDERNAME_NOT_PRESENT("IPAY0100081","Card holder name is not present"),
	EMPTY_OTP_NUMBER("IPAY0100092","Empty OTP number"),
	INVALID_OTP_NUMBER("IPAY0100093","Invalid OTP number"),
	TERMINAL_INACTIVE("IPAY0100095","Terminal inactive"),
	
	//BANK of BENEFIT
	BANK_DISCON("91","The bank is disconnected at the moment"),
    ISSUE_IN_CARD("05","The customer should contact the bank to resolve the Card issue"),
    CARD_EXPIRED("33","The customer card is expired"),
    INSUFFICIENT_FUNDS("51","Insufficient funds"),
    CUSTOMER_CARD_EXPIRED("54","The customer card is expired"),
    INCORRECT_PIN_NUMBER("55","Incorrect pin number"),
    EXCEEDS_WITHDRAW_LIMIT("61","The card exceeds withdrawal amount limit");
    
	//public static final ResponseCode DEFAULT = RES_CODE_IPAY0200079;

	private String responseCode;
	private String responseDesc;
	
	ResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	ResponseCode(String responseCode,String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	public String getResponseCode() {
		return this.responseCode;
	}
	
	public String getResponseDesc() {
		return this.responseDesc;
	}


}
