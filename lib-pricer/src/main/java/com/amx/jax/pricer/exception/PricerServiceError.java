package com.amx.jax.pricer.exception;

import com.amx.jax.exception.IExceptionEnum;

/**
 * The Enum PricerServiceError.
 */
public enum PricerServiceError implements IExceptionEnum {

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

	/** The duplicate role. */
	DUPLICATE_ROLE,

	/** The invalid role. */
	INVALID_ROLE,

	/** The incompatible data type. */
	INCOMPATIBLE_DATA_TYPE,

	/** The unknown exception. */
	// MISC
	UNKNOWN_EXCEPTION;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.pricer.exception.IExceptionEnum#getCode()
	 */
	public String getStatusKey() {
		return this.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.pricer.exception.IExceptionEnum#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return this.ordinal();
	}

}
