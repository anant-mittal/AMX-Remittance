package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class LimitExeededException extends AbstractException {

	public LimitExeededException(ApiError error) {
		super(error);
		// TODO Auto-generated constructor stub
	}

}
