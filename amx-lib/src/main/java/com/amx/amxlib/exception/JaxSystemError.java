package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.ApiError;

public class JaxSystemError extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxSystemError(ApiError error) {
		super(error);
	}
	
	public JaxSystemError() {
		super("System error occured");
		this.setError(JaxError.JAX_SYSTEM_ERROR);
	}

}
