package com.amx.jax.rbaac.error;

import com.amx.jax.exception.IExceptionEnum;

/**
 * The Enum AuthServiceError.
 */
public enum AuthServiceError implements IExceptionEnum {

	/** The already exist. */
	// RBAC error
	ALREADY_EXIST,

	/** The missing otp. */
	MISSING_OTP,

	/** The invalid otp. */
	INVALID_OTP,

	/** The otp limit exceeded. */
	OTP_LIMIT_EXCEEDED,

	/** The invalid or missing data. */
	INVALID_OR_MISSING_DATA,

	/** The invalid role definition. */
	INVALID_ROLE_DEFINITION,

	/** The invalid user details. */
	INVALID_USER_DETAILS,

	/** The Multiple users. */
	MULTIPLE_USERS,

	/** The User Not Active. */
	USER_NOT_ACTIVE_OR_DELETED,

	/** The user account locked. */
	USER_ACCOUNT_LOCKED,

	/** The unknown exception. */
	// MISC
	UNKNOWN_EXCEPTION;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exception.IExceptionEnum#getCode()
	 */
	public String getCode() {
		return this.toString();
	}

}
