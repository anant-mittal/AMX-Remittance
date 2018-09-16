package com.amx.jax.exception;

public enum AmxStatus implements IExceptionEnum {

	SUCCESS, FAIL, UNAUTORIZED;

	@Override
	public String getStatusKey() {
		return this.toString();
	}

	@Override
	public int getStatusCode() {
		return this.ordinal();
	}

}
