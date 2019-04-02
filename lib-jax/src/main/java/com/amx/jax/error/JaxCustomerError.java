package com.amx.jax.error;

import com.amx.jax.exception.IExceptionEnum;

public enum JaxCustomerError implements IExceptionEnum {

	SECURITY_QUESTION_REQUIRED, SECURITY_ANSWER_REQUIRED, ID_PROOF_REQUIRED, ID_PROOF_VERIFICATION_PENDING;

	@Override
	public String getStatusKey() {
		return this.toString();
	}

	@Override
	public int getStatusCode() {
		return 2000 + this.ordinal();
	}
}
