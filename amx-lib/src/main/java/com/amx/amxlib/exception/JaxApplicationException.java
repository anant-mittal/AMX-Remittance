package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class JaxApplicationException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxApplicationException(ApiError error) {
		super(error);
	}

}
