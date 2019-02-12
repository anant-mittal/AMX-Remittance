package com.amx.jax.rbaac.error;

import com.amx.jax.exception.IExceptionEnum;

/**
 * The Enum RbaacServiceError.
 */
public enum RbaacServiceError implements IExceptionEnum {

	//@formatter:off
	
	/** The already exist. */
	// RBAC error
	ALREADY_EXIST,

	/** The missing otp. */
	MISSING_OTP,

	/** The invalid otp. */
	INVALID_OTP,
	
	/** The invalid otp. */
	OTP_TIMED_OUT,
	
	INVALID_PARTNER_OTP,

	/** The otp limit exceeded. */
	OTP_LIMIT_EXCEEDED,

	/** The invalid or missing data. */
	INVALID_OR_MISSING_DATA,
	
	INVALID_OR_MISSING_CREDENTIALS,
	
	INVALID_OR_MISSING_PARTNER_IDENTITY,

	INVALID_OR_MISSING_TERMINAL_ID,

	INVALID_OR_MISSING_DEVICE_ID,

	/** The invalid role definition. */
	INVALID_ROLE_DEFINITION,

	/** The invalid permission. */
	INVALID_PERMISSION,

	/** The invalid access type scope. */
	INVALID_ACCESS_TYPE_SCOPE,

	/** The invalid user role mappings. */
	INVALID_USER_ROLE_MAPPINGS,

	/** The illegal user role mapping modification. */
	ILLEGAL_USER_ROLE_MAPPING_MODIFICATION,

	/** The invalid user details. */
	INVALID_USER_DETAILS,

	/** The Multiple users. */
	MULTIPLE_USERS,

	/** The User Not Active. */
	USER_NOT_ACTIVE_OR_DELETED,

	/** The user account locked. */
	USER_ACCOUNT_LOCKED,
	
	/** The invalid phone number. */
	INVALID_PHONE_NUMBER,

	/** The duplicate role. */
	DUPLICATE_ROLE,

	/** The invalid role. */
	INVALID_ROLE,

	/** The incompatible data type. */
	INCOMPATIBLE_DATA_TYPE,

	/** The unknown exception. */
	UNKNOWN_EXCEPTION,

	/**Device  errors **/
	CLIENT_ALREADY_REGISTERED, CLIENT_NOT_FOUND, BRANCH_SYSTEM_NOT_FOUND, 
	BRANCH_SYSTEM_NOT_ACTIVE, CLIENT_TOO_MANY_ACTIVE, DEVICE_CLIENT_INVALID, 
	CLIENT_NOT_ACTIVE, CLIENT_INVALID_PAIR_TOKEN, 
	CLIENT_INVALID_SESSION_TOKEN, CLIENT_ANOTHER_ALREADY_ACTIVE, 
	CLIENT_NOT_LOGGGED_IN, CLIENT_ALREADY_ACTIVE, CLIENT_EXPIRED_SESSION_TOKEN, 
	CLIENT_EXPIRED_VALIDATE_OTP_TIME,
	
	EMPLOYEE_NOT_FOUND;

	//@formatter:on

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.IExceptionEnum#getCode()
	 */
	public String getStatusKey() {
		return this.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.IExceptionEnum#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return this.ordinal();
	}

}
