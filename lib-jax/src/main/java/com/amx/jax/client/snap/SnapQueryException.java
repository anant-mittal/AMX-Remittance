package com.amx.jax.client.snap;

import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.exception.JaxSystemError;

public class SnapQueryException extends AmxApiException {

	private static final long serialVersionUID = 1L;

	public static enum SnapServiceCodes implements IExceptionEnum {
		INVALID_QUERY;

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return 30000 + this.ordinal();
		}
	}

	private boolean reportable = false;

	public SnapQueryException(AmxApiError error) {
		super(error);
	}

	public SnapQueryException() {
		super("UI Server error occured");
		this.setError(SnapServiceCodes.INVALID_QUERY);
	}

	public SnapQueryException(Exception e) {
		super(e);
		reportable = true;
		this.setError(SnapServiceCodes.INVALID_QUERY);
	}

	public SnapQueryException(SnapServiceCodes error) {
		super("UI Server error occured");
		this.setError(error);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return new SnapQueryException(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return SnapServiceCodes.valueOf(errorId);
	}

	public static <T> T evaluate(Exception e) {
		if (e instanceof JaxSystemError) {
			throw (JaxSystemError) e;
		} else if (e instanceof AbstractJaxException) {
			throw (AbstractJaxException) e;
		} else {
			throw new SnapQueryException(e);
		}
	}

	@Override
	public boolean isReportable() {
		return reportable;
	}

}
