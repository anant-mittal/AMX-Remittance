package com.amx.jax.sso;

public class SSOConstants {

	private SSOConstants() {
		// Not allowed
	}

	public static final String APP_LOGGEDIN_URL = "/sso/loggedin";
	public static final String APP_LOGIN_URL = "/sso/login";
	public static final String SSO_LOGIN_URL = "/sso/auth/login";
	public static final String SSO_LOGIN_URL_JSON = SSO_LOGIN_URL + "/{json}";
	public static final String SSO_LOGIN_URL_HTML = SSO_LOGIN_URL + "/{html}";
	public static final String SSO_LOGIN_URL_REDIRECT = SSO_LOGIN_URL + "/required";
	public static final String SSO_LOGIN_URL_DO = SSO_LOGIN_URL + "/do";

	public static final String SSO_INDEX_PAGE = "sso_index";
	public static final String REDIRECT = "redirect:";

	public static final String PARAM_REDIRECT = "redirect";
	public static final String PARAM_SOTP = "sotp";
	public static final String PARAM_AUTH = "auth";
	public static final String PARAM_SSO_LOGIN_URL = "SSO_LOGIN_URL";

	public enum SSOAuth {
		DONE, INIT, NONE
	}

}
