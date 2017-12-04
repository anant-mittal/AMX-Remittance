package com.amx.jax.ui;

public class EnumUtil {

	/**
	 * http://www.restapitutorial.com/httpstatuscodes.html
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum StatusCode {

		// Registration - CIVIL ID validation
		ALREADY_ACTIVE("302", "ALREADY_ACTIVE"), INVALID_ID("200", "INVALID_ID"), OTP_SENT("200", "OTP_SENT"),

		// Registration - OTP validation
		ALREADY_LOGGED_IN("302", "ALREADY_LOGGED_IN"), VERIFY_SUCCESS("200", "VERIFY_SUCCESS"), VERIFY_FAILED("401",
				"VERIFY_FAILED"),

		// User Updates
		USER_UPDATE_SUCCESS("200", "USER_UPDATE_SUCCESS"), LOGOUT_DONE("200", "LOGOUT_DONE"), USER_UPDATE_FAILED("300",
				"USER_UPDATE_FAILED"),

		// Auth Reponses
		AUTH_FAILED("302", "AUTH_FAILED"), AUTH_OK("200", "AUTH_OK"), AUTH_DONE("200",
				"AUTH_DONE"), AUTH_BLOCKED_TEMP("200", "AUTH_BLOCKED_TEMP"), AUTH_BLOCKED("200", "AUTH_BLOCKED"),

		// Registration - END ERROR
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
