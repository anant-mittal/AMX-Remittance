package com.amx.jax;

import com.amx.utils.Constants;

public class AppConstants extends Constants {

	protected AppConstants() {
		// Not Allowed
	}

	public static class Validator {
		public static final String IDENTITY = "^[0-9a-zA-Z]+$";
		public static final String OTP = "^[0-9]{6}$";
		public static final String PHONE = "^[0-9]{15}$";
		public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	}

	public static final String AUTH_KEY_XKEY = "x-app-auth-key";
	public static final String SESSION_ID_XKEY = "x-session-id";
	public static final String TRACE_ID_XKEY = "x-trace-id";
	public static final String TRACE_TIME_XKEY = "x-time-id";
	public static final String REQUEST_TYPE_XKEY = "x-request-type";
	public static final String TRANX_ID_XKEY = "x-tranx-id";
	public static final String META_XKEY = "meta-info";
	public static final String TRANX_ID_XKEY_CLEAN = TRANX_ID_XKEY.replaceAll("[-]", "_");
	public static final String ACTOR_ID_XKEY = "x-actor-id";
	public static final String DEVICE_ID_KEY = "did";
	public static final String DEVICE_ID_XKEY = "x-did";
	public static final String APP_DETAILS = "app";
	public static final String BROWSER_ID_KEY = "bid";
	public static final String SESSIONID = "JSESSIONID";
}
