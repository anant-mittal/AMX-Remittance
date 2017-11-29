package com.amx.jax.ui;

public class EnumUtil {

	public enum StatusCode {

		// Registration
		ALREADY_ACTIVE("400.1", "ALREADY_ACTIVE"), INVALID_ID("202.2", "INVALID_ID"),

		ALREADY_LOGGED_IN("202.3", "ALREADY_LOGGED_IN"), VERIFY_SUCCESS("202.4",
				"VERIFY_SUCCESS"), VERIFY_FAILED("202.4", "VERIFY_FAILED");

		private final String code;
		private final String key;

		public String getCode() {
			return code;
		}

		public String getKey() {
			return key;
		}

		StatusCode(String code, String key) {
			this.code = code;
			this.key = key;
		}

		public String toString() {
			return this.key;
		}

	}
}
