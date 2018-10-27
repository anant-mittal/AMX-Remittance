package com.amx.jax.exception;

import org.springframework.http.HttpStatus;

public class ApiHttpExceptions {

	public static enum ApiHttpCodes implements IExceptionEnum {

		HTTP_CLIENT_ERROR, HTTP_SERVER_ERROR(500),

		HTTP_NOT_FOUND(404),

		PARAM_INVALID, PARAM_MISSING, PARAM_ILLEGAL, PARAM_TYPE_MISMATCH;

		int statusCode;

		ApiHttpCodes(int statusCode) {
			this.statusCode = statusCode;
		}

		ApiHttpCodes() {
			this(400);
		}

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return this.statusCode;
		}

	}

	public static class ApiHttpClientException extends AmxException {

		private static final long serialVersionUID = 7427401687837732495L;

		public ApiHttpClientException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError.getMessage());
		}

	}

	public static class ApiHttpNotFoundException extends AmxException {
		private static final long serialVersionUID = -2333301958163665424L;

		public ApiHttpNotFoundException(HttpStatus statusCode) {
			super(statusCode, String.format("[%s]", statusCode.value(), statusCode.getReasonPhrase()));
		}
	}

	public static class ApiHttpServerException extends AmxException {
		private static final long serialVersionUID = 3282368584279205762L;

		public ApiHttpServerException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError.getMessage());
		}
	}

	public static class ApiHttpArgException extends AmxApiException {

		private static final long serialVersionUID = 1L;

		AmxApiError apiError;

		public ApiHttpArgException(AmxApiError apiError) {
			this.apiError = apiError;
		}

		public ApiHttpArgException() {
			super("Http Argument Exception");
			this.setError(ApiHttpCodes.PARAM_INVALID);
		}

		public ApiHttpArgException(ApiHttpCodes statusCode) {
			super(statusCode);
		}

		public ApiHttpArgException(ApiHttpCodes statusCode, String message) {
			super(statusCode, message);
		}

		public ApiHttpArgException(Exception e) {
			super(e);
			this.setError(ApiHttpCodes.PARAM_INVALID);
		}

		@Override
		public AmxApiError createAmxApiError() {
			return this.apiError;
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new ApiHttpArgException(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return ApiHttpCodes.PARAM_INVALID;
		}

		public static <T> T evaluate(Exception e) {
			if (e instanceof ApiHttpArgException) {
				throw (ApiHttpArgException) e;
			} else {
				throw new ApiHttpArgException(e);
			}
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}
}
