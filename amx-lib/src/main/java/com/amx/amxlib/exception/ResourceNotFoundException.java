package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class ResourceNotFoundException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(ApiError error) {
		super(error);
	}

}
