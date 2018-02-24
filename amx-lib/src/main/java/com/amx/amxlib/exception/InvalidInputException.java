package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class InvalidInputException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(ApiError error) {
		super(error);
	}

}
