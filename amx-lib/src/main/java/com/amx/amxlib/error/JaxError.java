package com.amx.amxlib.error;

public enum JaxError {

    INVALID_CIVIL_ID, 
    CUSTOMER_NOT_FOUND, 
    INVALID_RANDOM_QUEST_SIZE, 
    USER_VERFICATION_RECORD_NOT_FOUND, 
    NULL_CUSTOMER_ID, 
    BLACK_LISTED_CUSTOMER, 
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
    USER_NOT_FOUND, 
    USER_NOT_REGISTERED, 
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
    SERVICE_PROVIDER_LIST_NOT_FOUND,
    AGENT_BANK_LIST_NOT_FOUND,
    AGENT_BRANCH_LIST_NOT_FOUND,
    BANK_ID_NOT_PRESENT,
    JAX_FIELD_VALIDATION_FAILURE;

    public String getCode() {
        return this.toString();
    }

}
