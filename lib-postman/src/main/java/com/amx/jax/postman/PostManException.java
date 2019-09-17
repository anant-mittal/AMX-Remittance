package com.amx.jax.postman;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;

public class PostManException extends AmxApiException {

	private static final long serialVersionUID = -4630097170366238398L;

	public enum ErrorCode implements IExceptionEnum {
		NO_RECIPIENT_DEFINED, NO_TENANT_DEFINED, NO_CHANNEL_DEFINED, NO_TOPIC_DEFINED, NO_MESSAGE_DEFINED;
		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return this.ordinal();
		}
	}

	public PostManException(Exception e) {
		super(e);
	}

	public PostManException(String msg) {
		super(msg);
	}

	public PostManException(ErrorCode error) {
		super(error);
	}

	public PostManException(AmxApiError apiError) {
		super(apiError);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError) {
		return new PostManException(apiError);
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId) {
		return ErrorCode.valueOf(errorId);
	}

	@Override
	public boolean isReportable() {
		return false;
	}

}
