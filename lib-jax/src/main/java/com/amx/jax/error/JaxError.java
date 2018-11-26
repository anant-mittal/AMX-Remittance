package com.amx.jax.error;

import com.amx.jax.exception.IExceptionEnum;

public enum JaxError implements IExceptionEnum {

	INVALID_CIVIL_ID,
	CUSTOMER_NOT_FOUND,
	INVALID_RANDOM_QUEST_SIZE,
	USER_VERFICATION_RECORD_NOT_FOUND,
	NULL_CUSTOMER_ID,
	BLACK_LISTED_CUSTOMER,
	BLACK_LISTED_BENEFICIARY,
	BLACK_LISTED_ARABIC_BENEFICIARY,
	MISSING_HOME_CONTACT_DETAILS,
	MISSING_LOCAL_CONTACT_DETAILS,
	MISSING_CONTACT_DETAILS,
	INVALID_CUSTOMER_REFERENCE,
	CUSTOMER__SIGNATURE_UNAVAILABLE,
	INVALID_INSURANCE_INDICATOR,
	OLD_EMOS_USER_NOT_FOUND,
	OLD_EMOS_USER_DELETED,
	OLD_EMOS_USER_DATA_EXPIRED,
	USERNAME_ALREADY_EXISTS,
	ID_PROOFS_NOT_VALID,
	ID_PROOFS_IMAGES_NOT_FOUND,
	ID_PROOFS_SCAN_NOT_FOUND,
	WRONG_PASSWORD,
	WRONG_PASSWORDS_ATTEMPTS,
	CUSTOMER_INACTIVE,
	NO_ID_PROOFS_AVAILABLE,
	ID_PROOF_EXPIRED,
	INVALID_OTP,
	OTP_NOT_VALIDATED,
	USER_NOT_FOUND,
	USER_NOT_REGISTERED,
	CUSTOMER_NOT_REGISTERED_ONLINE,
	CUSTOMER_NOT_REGISTERED_BRANCH,
	CUSTOMER_NOT_ACTIVE_BRANCH,
	DUPLICATE_CUSTOMER_NOT_ACTIVE_BRANCH,
	CUSTOMER_ACTIVE_BRANCH,
	CUSTOMER_NOT_ACTIVE_ONLINE,
	CUSTOMER_ACTIVE_ONLINE,
	INVALID_JSON_BODY,
	INCORRECT_SECURITY_QUESTION_ANSWER,
	USER_LOGIN_ATTEMPT_EXCEEDED,
	EXCHANGE_RATE_NOT_FOUND,
	NULL_APPLICATION_COUNTRY_ID,
	INVALID_EXCHANGE_AMOUNT,
	NULL_CURRENCY_ID,
	BENE_ACCOUNT_BLANK,
	INVALID_BENE_STATE,
	INVALID_BENE_DISTRICT,
	INVALID_BENE_CITY,
	DATA_NOT_FOUND,
	REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL,
	COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
	TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
	INVALID_BENE_COUNTRY,
	INVALID_BENE_BANK,
	INVALID_BENE_BANK_CNTRY,
	RECORD_NOT_FOUND,
	NULL_CHECK,
	ACCOUNT_TYPE_UPDATE,
	INVALID_NATIONALITY,
	TRANSACTION_VALIDATION_FAIL,
	ACCOUNT_LENGTH,
	INVALID_MOB_TELE,
	UPDATE_BENE_NEEDED,
	NO_OF_TRANSACTION_LIMIT_EXCEEDED,

	/* Maximum amount allowed for transaction */
	TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED,
	TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_NEW_BENE,
	NEW_BENEFICIARY_TRANSACTION_TIME_LIMIT,

	/* Maximum amount allowed for transaction for perticular bene */
	TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE,
	PG_ERROR,
	UNKNOWN_JAX_ERROR,
	MISSING_OTP,
	SEND_OTP_LIMIT_EXCEEDED,
	VALIDATE_OTP_LIMIT_EXCEEDED,
	OTP_EXPIRED,
	TRANSACTION_HISTORY_NOT_FOUND,
	BENEFICIARY_LIST_NOT_FOUND,
	BENEFICIARY_COUNTRY_LIST_NOT_FOUND,
	NO_RECORD_FOUND,
	INCORRECT_LENGTH,
	ALREADY_EXIST,
	JAX_SYSTEM_ERROR,
	USER_DATA_VERIFICATION_PENDING,
	USER_DATA_VERIFICATION_PENDING_REG,
	BANK_BRANCH_NOT_FOUND,
	INVALID_BANK_ACCOUNT_NUM_LENGTH,
	DUPLICATE_BENE_BANK_ACCOUNT,
	DUPLICATE_BENE_CASH_ACCOUNT,
	SERVICE_PROVIDER_LIST_NOT_FOUND,
	AGENT_BANK_LIST_NOT_FOUND,
	AGENT_BRANCH_LIST_NOT_FOUND,
	BANK_ID_NOT_PRESENT,
	JAX_FIELD_VALIDATION_FAILURE,
	REMITTANCE_SERVICE_NOT_AVAILABLE,

	/* validation errors */
	VALIDATION_MINIMUM_LENGTH_MOBILE,
	VALIDATION_MAXIMUM_LENGTH_MOBILE,
	VALIDATION_LENGTH_MOBILE,
	VALIDATION_NOT_NULL,

	INVALID_BANK_IFSC, INVALID_BANK_SWIFT,
	BANK_SWIFT_EMPTY,

	BANK_BRANCH_SEARCH_EMPTY,
	INVALID_INPUT,
	ALREADY_EXIST_EMAIL,
	ALREADY_EXIST_MOBILE,

	/* place order */
	PLACE_ORDER_ID_NOT_FOUND,
	PLACE_ORDER_NOT_ACTIVE_OR_EXPIRED,
	PLACE_ORDER_EXPIRED,
	PO_BOTH_PAY_RECEIVED_AMT_NULL,
	PO_BOTH_PAY_RECEIVED_AMT_VALUE,

	INVALID_MOBILE_NUMBER,
	BLACK_LISTED_EXISTING_CIVIL_ID,
	EXCHANGE_RATE_CHANGED,
	ADDTIONAL_FLEX_FIELD_REQUIRED,
	// Offsite customer
	BLANK_CIVIL_ID,
	BLANK_EMPLOYEE_ID,
	INVALID_EMPLOYEE,
	EMPLOYEE_EMAIL_ID_NOT_AVAILABLE,
	EMPLOYEE_OTP_ATTEMPT_EXCEEDED,
	EMPTY_FIELD_CONDITION,
	WRONG_FIELD_CONDITION,
	EMPTY_ID_TYPE_LIST,
	EMPTY_ARTICLE_LIST,
	EMPTY_DESIGNATION_LIST,
	EMPTY_INCOME_RANGE,
	EMPTY_EMPLOYMENT_TYPE,
	EMPTY_PROFESSION_LIST,
	BRANCH_SYSTEM_NOT_FOUND,
	BRANCH_SYSTEM_NOT_ACTIVE,
	EXISTING_CIVIL_ID, EXISTING_BEDOUIN_ID, EXISTING_GCC_ID,
	EXISTING_PASSPORT, IMAGE_NOT_AVAILABLE, INVALID_CUSTOMER, SIGNATURE_NOT_AVAILABLE,
	CITY_NOT_AVAILABLE,
	INVALID_LANGUAGE_ID,
	// signature pad
	CLIENT_NOT_FOUND,
	CLIENT_NOT_ACTIVE, CLIENT_NOT_LOGGGED_IN, CLIENT_ALREADY_REGISTERED, CLIENT_INVALID_PAIR_TOKEN,
	CLIENT_INVALID_SESSION_TOKEN,
	CLIENT_ALREADY_ACTIVE, CLIENT_EXPIRED_SESSION_TOKEN, CLIENT_EXPIRED_VALIDATE_OTP_TIME, CLIENT_TOO_MANY_ACTIVE,
	CLIENT_ANOTHER_ALREADY_ACTIVE,

	/** FS Applcication **/
	FS_APPLIATION_CREATION_FAILED,
	FS_SHIPPING_ADDRESS_CREATION_FAILED,
	BLANK_COUNTRY_BRANCH,
	BLANK_CUSTOMER_ID,
	BLANK_LANGUAGE_ID,
	BLANK_COMPANY_ID,
	NULL_APPLICATION_ID,
	INVALID_APPLICATION_COUNTRY_ID,
	INVALID_CURRENCY_ID,
	INVALID_COMPANY_ID,
	INVALID_COUNTRY_BRANCH,
	FC_SALE_TIME_SLOT_SETUP_MISSING,
	INVALID_COLLECTION_DOCUMENT_NO,
	FC_CURRENCY_RATE_IS_NOT_AVAILABLE,
	FC_CURRENCY_DELIVERY_CHARGES_NOT_FOUND,
	INVALID_PAYMENT_MODE,
	PAYMENT_UPDATION_FAILED,
	FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND;

	@Deprecated
	public String getCode() {
		return this.toString();
	}

	@Override
	public String getStatusKey() {
		return this.toString();
	}

	@Override
	public int getStatusCode() {
		return 1000 + this.ordinal();
	}

}
