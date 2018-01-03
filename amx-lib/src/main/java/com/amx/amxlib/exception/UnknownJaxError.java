package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class UnknownJaxError extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownJaxError(ApiError error) {
		super(error);
	}

}
