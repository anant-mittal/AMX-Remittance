package com.amx.jax.tpc.api;

public class TPCApiConstants {

	public static final class Config {
		public static final long REQUEST_TOKEN_VALIDITY = 30;
		public static final long SESSION_TOKEN_VALIDITY = 3 * 3600;
	}

	public static class Keys {
		public static final String CLIENT_REG_KEY_XKEY = "x-device-reg-id";
		public static final String CLIENT_REG_TOKEN_XKEY = "x-device-reg-token";

		public static final String CLIENT_SESSION_TOKEN_XKEY = "x-device-session-token";

		public static final String DEVICE_REQ_KEY_XKEY = "x-device-req-key";
		public static final String CLIENT_REQ_TOKEN_XKEY = "x-device-req-token";
	}

	public static class Path {
		public static final String CLIENT_AUTH = "/tpc/client/auth";
		public static final String CUSTOMER_LOGIN = "/tpc/customer/login";
		public static final String CUSTOMER_LOGIN_CALLBACK = "/tpc/customer/login_callback";
		public static final String CUSTOMER_DETAILS = "/tpc/customer/details";
		public static final String CUSTOMER_BENE_LIST = "/tpc/customer/bene/list";
		public static final String CUSTOMER_SOURCE_LIST = "/tpc/customer/source/list";
		public static final String CUSTOMER_PURPOSE_LIST = "/tpc/customer/purpose/list";
		public static final String CUSTOMER_REMIT_INQUIRY = "/tpc/customer/remit/inquiry";
		public static final String CUSTOMER_REMIT_CONFIRM = "/tpc/customer/remit/confirm";
		public static final String CUSTOMER_REMIT_VERIFY = "/tpc/customer/remit/verify";
	}

	public static class Params {
		public static final String PARAM_CLIENT_TYPE = "clientType";
		public static final String PARAM_CLIENT_ID = "clientId";
		public static final String PARAM_SYSTEM_ID = "systemid";
	}

}
