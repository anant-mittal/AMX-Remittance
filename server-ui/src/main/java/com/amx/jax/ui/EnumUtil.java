package com.amx.jax.ui;

public class EnumUtil {

	/**
	 * http://www.restapitutorial.com/httpstatuscodes.html
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum StatusCode {

		// Registration
		ALREADY_ACTIVE("302", "ALREADY_ACTIVE"), INVALID_ID("202.2", "INVALID_ID"),

		ALREADY_LOGGED_IN("302", "ALREADY_LOGGED_IN"), VERIFY_SUCCESS("201", "VERIFY_SUCCESS"), VERIFY_FAILED("401",
				"VERIFY_FAILED"),

		QA_UPDATED("200", "QA_UPDATED"),

		ERROR("500", "ERROR");

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
