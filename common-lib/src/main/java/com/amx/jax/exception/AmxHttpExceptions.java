package com.amx.jax.exception;

public class AmxHttpExceptions {

	public static class AmxHttpClientException extends AmxException {

		private static final long serialVersionUID = 7427401687837732495L;

		public AmxHttpClientException(String msg) {
			super(msg);
		}

		public AmxHttpClientException(int value, String statusText) {
			super(value, statusText);
		}

	}

	public static class AmxHttpNotFoundException extends AmxException {
		private static final long serialVersionUID = -2333301958163665424L;

		public AmxHttpNotFoundException(int errorCode, String statusText) {
			super(errorCode, statusText);
		}
	}

	public static class AmxHttpServerException extends AmxException {
		private static final long serialVersionUID = 3282368584279205762L;

		public AmxHttpServerException(int errorCode, String statusText) {
			super(errorCode, statusText);
		}
	}
}
