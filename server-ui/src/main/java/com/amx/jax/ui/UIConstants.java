package com.amx.jax.ui;

import com.bootloaderjs.ArgUtil.ParameterException;
import com.bootloaderjs.Random;

public final class UIConstants extends com.bootloaderjs.Constants {

	public static final String EMPTY = "";
	public static final String REFERRER = "agentId";
	public static final String REG_SUC = "Registration successful";
	public static final String RESP_DATA_KEY = "data";
	public static final String DEVICE_ID_KEY = "did";
	public static final String BROWSER_ID_KEY = "bid";
	public static final String SESSIONID = "JSESSIONID";
	public static final String CDN_VERSION = "CDN_VERSION";
	public static final String SEQ_KEY = Random.randomAlpha(1, "LMNOPQR") + Random.randomAlpha(3);

	public static class Validator {
		public static final String IDENTITY = "^[0-9a-zA-Z]+$";
	}

}
