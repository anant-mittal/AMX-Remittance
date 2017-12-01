package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AbstractException;

public class InvalidInputException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String getErrorCode() {
		return JaxError.INVALID_CIVIL_ID.getCode();
	}

}
