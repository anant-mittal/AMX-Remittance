package com.amx.amxlib.exception;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public class JaxSystemError extends AmxApiException {

	private static final long serialVersionUID = 1L;

	public JaxSystemError(AmxApiError error) {
		super(error);
	}

	public JaxSystemError() {
		super("System error occured");
		this.setError(JaxError.JAX_SYSTEM_ERROR);
	}

	public JaxSystemError(Exception e) {
		super(e);
		this.setError(JaxError.JAX_SYSTEM_ERROR);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return new JaxSystemError(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return JaxError.JAX_SYSTEM_ERROR;
	}

	@Override
	public boolean isReportable() {
		return true;
	}

	public static <T> T evaluate(Exception e) {
		if (e instanceof AbstractJaxException) {
			throw (AbstractJaxException) e;
		} else {
			throw new JaxSystemError(e);
		}
	}

}
