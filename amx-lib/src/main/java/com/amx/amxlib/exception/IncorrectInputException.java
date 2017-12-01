package com.amx.amxlib.exception;

import com.amx.amxlib.model.response.ApiError;

public class IncorrectInputException extends AbstractException{

	public IncorrectInputException(ApiError error) {
		super(error);
		// TODO Auto-generated constructor stub
	}

	
}
