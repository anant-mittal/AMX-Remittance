package com.amx.jax.sso;

public class SSOConstants {

	private SSOConstants() {
		// Not allowed
	}

	public static final String APP_LOGGEDIN_URL = "/sso/loggedin/default";
	public static final String APP_LOGGEDIN_URL_JSON = "/sso/loggedin/{json}";
	public static final String APP_LOGGEDIN_URL_HTML = "/sso/loggedin/{html}";
	public static final String APP_LOGOUT_URL = "/sso/logout";

	public static final String APP_LOGIN_URL = "/sso/login";
	public static final String APP_LOGIN_URL_JSON = "/sso/login/{jsonstep}";
	public static final String APP_LOGIN_URL_HTML = "/sso/login/{htmlstep}";
	public static final String APP_LOGIN_URL_DONE = APP_LOGIN_URL + "/DONE";
	public static final String APP_LOGIN_URL_CHECK = APP_LOGIN_URL + "/CHECK";

	public static final String SSO_LOGIN_URL = "/sso/auth/login";
	public static final String SSO_LOGIN_URL_JSON = SSO_LOGIN_URL + "/{jsonstep}";
	public static final String SSO_LOGIN_URL_HTML = SSO_LOGIN_URL + "/{htmlstep}";
	public static final String SSO_LOGIN_URL_REQUIRED = SSO_LOGIN_URL + "/REQUIRED";
	public static final String SSO_LOGIN_URL_DO = SSO_LOGIN_URL + "/DO";

	public static final String SSO_CARD_DETAILS = "/sso/card/details";

	public static final String SSO_INDEX_PAGE = "sso_index";
	public static final String REDIRECT = "redirect:";

	public static final String PARAM_REDIRECT = "redirect";
	public static final String PARAM_SOTP = "sotp";
	public static final String PARAM_STEP = "step";
	public static final String PARAM_SSO_LOGIN_URL = "SSO_LOGIN_URL";

	public enum SSOAuthStep {
		DO, DONE, REQUIRED, CHECK, CREDS, OTP
	}

}
