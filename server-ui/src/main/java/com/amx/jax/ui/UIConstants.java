package com.amx.jax.ui;

import com.bootloaderjs.Random;

public final class UIConstants extends com.bootloaderjs.Constants {

	public static final String EMPTY = "";
	public static final String REFERRER = "agentId";
	public static final String REG_SUC = "Registration successful";
	public static final String RESP_DATA_KEY = "data";
	public static final String DEVICE_ID_KEY = "did";
	public static final String CDN_VERSION = "CDN_VERSION";
	public static final String SEQ_KEY = "seqkey";
	public static final String SEQ_KEY_STEP_LOGIN = Random.randomAlpha(1, "LMNOPQR") + Random.randomAlpha(3);
	public static final String SEQ_KEY_STEP_SECQ = Random.randomAlpha(1, "STUVWX") + Random.randomAlpha(3);

}