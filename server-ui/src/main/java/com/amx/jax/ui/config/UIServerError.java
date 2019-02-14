package com.amx.jax.ui.config;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.ui.response.WebResponseStatus;

public class UIServerError extends AmxApiException {

	private static final long serialVersionUID = 1L;

	private boolean reportable = false;

	public UIServerError(AmxApiError error) {
		super(error);
	}

	public UIServerError() {
		super("UI Server error occured");
		this.setError(WebResponseStatus.UI_SERVER_ERROR);
	}

	public UIServerError(Exception e) {
		super(e);
		reportable = true;
		this.setError(WebResponseStatus.UI_SERVER_ERROR);
	}

	public UIServerError(WebResponseStatus error) {
		super("UI Server error occured");
		this.setError(error);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return new UIServerError(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return WebResponseStatus.valueOf(errorId);
	}

	public static <T> T evaluate(Exception e) {
		if (e instanceof JaxSystemError) {
			throw (JaxSystemError) e;
		} else if (e instanceof AbstractJaxException) {
			throw (AbstractJaxException) e;
		} else {
			throw new UIServerError(e);
		}
	}

	@Override
	public boolean isReportable() {
		return reportable;
	}

}
