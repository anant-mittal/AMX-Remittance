package com.amx.jax.exception;

public class InvalidJsonInputException extends AbstractJaxException {

	public InvalidJsonInputException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidJsonInputException(AmxApiError error) {
		super(error);
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
