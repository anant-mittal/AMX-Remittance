package com.amx.jax.exception;

import org.slf4j.Logger;

import com.amx.jax.error.JaxError;

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
		} else if (e instanceof AmxApiException) {
			throw (AmxApiException) e;
		} else {
			throw new JaxSystemError(e);
		}
	}

	public static <T> T evaluate(Logger logger, Exception e) {
		if (e instanceof AbstractJaxException) {
			throw (AbstractJaxException) e;
		} else if (e instanceof AmxApiException) {
			throw (AmxApiException) e;
		} else {
			logger.error("JaxSystemError : ", e);
			throw new JaxSystemError(e);
		}
	}

}
