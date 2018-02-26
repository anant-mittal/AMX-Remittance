package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class AlreadyExistsException extends AbstractException {

    private static final long serialVersionUID = 1L;

    public AlreadyExistsException(ApiError error) {
		super(error);
	}

}
