package com.amx.amxlib.exception;

public class InvalidJsonInputException extends AbstractException {

	public InvalidJsonInputException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorCode() {
		return "INVALID_JSON_BODY";
	}

}
