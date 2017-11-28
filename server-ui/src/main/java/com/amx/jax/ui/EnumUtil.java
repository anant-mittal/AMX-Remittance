package com.amx.jax.ui;

public class EnumUtil {

	public enum StatusCode {

		// Registration
		ALREADY_ACTIVE("202.1", "ALREADY_ACTIVE"), INVALID_ID("202.2", "INVALID_ID");

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
