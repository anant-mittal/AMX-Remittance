package com.amx.jax.auth.error;

import com.amx.jax.exception.IExceptionEnum;

public enum JaxError implements IExceptionEnum {

	// RBAC error
	ALREADY_EXIST,
	VALIDATE_OTP_LIMIT_EXCEEDED,
	MISSING_OTP,
	INVALID_OTP,
	INVALID_DATA,
	INVALID_EMPLOYEE_DETAILS,
	INVALID_ROLE_DEFINITION,
	INVALID_USER_DETAILS;

	public String getCode() {
		return this.toString();
	}

}
