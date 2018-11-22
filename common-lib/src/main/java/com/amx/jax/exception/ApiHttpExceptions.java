package com.amx.jax.exception;

import org.springframework.http.HttpStatus;

public class ApiHttpExceptions {

	public static enum ApiStatusCodes implements IExceptionEnum {

		SUCCESS(200), NO_CONTENT(204), NO_STATUS(200),

		HTTP_CLIENT_ERROR, HTTP_SERVER_ERROR(500), API_ERROR(500), FAIL(500),

		PARAM_INVALID(400), PARAM_MISSING(400), PARAM_ILLEGAL(400), PARAM_TYPE_MISMATCH(400), UNKNOWN_CLIENT_ERROR(400),

		UNAUTORIZED(401),
		HTTP_NOT_FOUND(404),

		OTP_REQUIRED(461), MOTP_REQUIRED(461), EOTP_REQUIRED(463), DOTP_REQUIRED(464),

		UNKNOWN(520);

		int statusCode;

		ApiStatusCodes(int statusCode) {
			this.statusCode = statusCode;
		}

		ApiStatusCodes() {
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

	public static class ApiErrorException extends AmxApiException {

		private static final long serialVersionUID = -7306148895312312163L;

		public ApiErrorException(AmxApiError apiError) {
			super(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return ApiStatusCodes.API_ERROR;
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}

	public static class ApiHttpArgException extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public ApiHttpArgException(AmxApiError apiError) {
			super(apiError);
		}

		public ApiHttpArgException() {
			super("Http Argument Exception");
			this.setError(ApiStatusCodes.PARAM_INVALID);
		}

		public ApiHttpArgException(ApiStatusCodes statusCode) {
			super(statusCode);
		}

		public ApiHttpArgException(ApiStatusCodes statusCode, String message) {
			super(statusCode, message);
		}

		public ApiHttpArgException(Exception e) {
			super(e);
			this.setError(ApiStatusCodes.PARAM_INVALID);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new ApiHttpArgException(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return ApiStatusCodes.PARAM_INVALID;
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
