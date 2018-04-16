package com.amx.jax.ui;

import com.amx.utils.Constants;
import com.amx.utils.Random;

public final class UIConstants extends Constants {

	public static final String EMPTY = "";
	public static final String REFERRER = "agentId";
	public static final String REG_SUC = "Registration successful";
	public static final String RESP_DATA_KEY = "data";
	public static final String CDN_VERSION = "CDN_VERSION";
	public static final String SEQ_KEY = Random.randomAlpha(1, "LMNOPQR") + Random.randomAlpha(3);
	public static final int CACHE_TIME = 31556926;
}
