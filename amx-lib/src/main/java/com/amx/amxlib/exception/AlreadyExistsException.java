package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class AlreadyExistsException extends AbstractException {

	public AlreadyExistsException(ApiError error) {
		super(error);
		// TODO Auto-generated constructor stub
	}

}
