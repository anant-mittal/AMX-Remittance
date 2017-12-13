package com.amx.amxlib.error;

public enum JaxError {


	INVALID_CIVIL_ID, CUSTOMER_NOT_FOUND, INVALID_RANDOM_QUEST_SIZE, USER_VERFICATION_RECORD_NOT_FOUND,
	NULL_CUSTOMER_ID, BLACK_LISTED_CUSTOMER, MISSING_HOME_CONTACT_DETAILS, MISSING_LOCAL_CONTACT_DETAILS,
	MISSING_CONTACT_DETAILS, INVALID_CUSTOMER_REFERENCE, CUSTOMER__SIGNATURE_UNAVAILABLE,
	INVALID_INSURANCE_INDICATOR, OLD_EMOS_USER_NOT_FOUND, OLD_EMOS_USER_DELETED, OLD_EMOS_USER_DATA_EXPIRED, 
	USERNAME_ALREADY_EXISTS, ID_PROOFS_NOT_VALID, ID_PROOFS_IMAGES_NOT_FOUND, ID_PROOFS_SCAN_NOT_FOUND, 
	WRONG_PASSWORD, CUSTOMER_INACTIVE, NO_ID_PROOFS_AVAILABLE, ID_PROOF_EXPIRED, INVALID_OTP, USER_NOT_FOUND,
	INVALID_JSON_BODY, INCORRECT_SECURITY_QUESTION_ANSWER, USER_LOGIN_ATTEMPT_EXCEEDED, EXCHANGE_RATE_NOT_FOUND,
	NULL_APPLICATION_COUNTRY_ID,INVALID_EXCHANGE_AMOUNT,
	NULL_CURRENCY_ID,BENE_ACCOUNT_BLANK,INVALID_BENE_STATE,INVALID_BENE_DISTRICT,INVALID_BENE_CITY,DATA_NOT_FOUND,
	INVALID_BENE_COUNTRY,INVALID_BENE_BANK,INVALID_BENE_BANK_CNTRY, TRANSACTION_VALIDATION_FAIL;

	public String getCode() {
		return this.toString();
	}

}
