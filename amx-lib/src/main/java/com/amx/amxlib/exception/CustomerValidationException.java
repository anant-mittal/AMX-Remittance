package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class CustomerValidationException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerValidationException(String errorMessage, String errorCode) {
		super(errorMessage, errorCode);
	}

	public CustomerValidationException(ApiError error) {
		super(error);
	}

}
