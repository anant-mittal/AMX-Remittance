package com.amx.jax.ui.config;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public class UIServerError extends AmxApiException {

	public enum UIServerCodes implements IExceptionEnum {
		UI_SERVER_ERROR;

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return this.ordinal();
		}

	}

	private static final long serialVersionUID = 1L;

	public UIServerError(AmxApiError error) {
		super(error);
	}

	public UIServerError() {
		super("UI Server error occured");
		this.setError(UIServerCodes.UI_SERVER_ERROR);
	}

	public UIServerError(Exception e) {
		super(e);
		this.setError(UIServerCodes.UI_SERVER_ERROR);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return new UIServerError(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return UIServerCodes.UI_SERVER_ERROR;
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
		return true;
	}

}
