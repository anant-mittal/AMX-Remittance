package com.amx.jax.exception;

import org.springframework.http.HttpStatus;

public class AmxHttpExceptions {

	public static class AmxHttpClientException extends AmxException {

		private static final long serialVersionUID = 7427401687837732495L;

		public AmxHttpClientException(HttpStatus statusCode, AmxApiError apiError) {
			super(apiError.getMessage());
		}

	}

	public static class AmxHttpNotFoundException extends AmxException {
		private static final long serialVersionUID = -2333301958163665424L;

		public AmxHttpNotFoundException(HttpStatus statusCode) {
			super(String.format("[%s]", statusCode.value(), statusCode.getReasonPhrase()));
		}
	}

	public static class AmxHttpServerException extends AmxException {
		private static final long serialVersionUID = 3282368584279205762L;

		public AmxHttpServerException(HttpStatus statusCode, AmxApiError apiError) {
			super(apiError.getMessage());
		}

	}
}
