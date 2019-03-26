package com.amx.jax.ui;

import com.amx.utils.Constants;
import com.amx.utils.Random;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Class UIConstants.
 */
public final class UIConstants extends Constants {

	/** The Constant EMPTY. */
	public static final String EMPTY = "";

	/** The Constant REFERRER. */
	public static final String REFERRER = "agentId";

	/** The Constant REG_SUC. */
	public static final String REG_SUC = "Registration successful";

	/** The Constant RESP_DATA_KEY. */
	public static final String RESP_DATA_KEY = "data";

	/** The Constant CDN_VERSION. */
	public static final String CDN_VERSION = "CDN_VERSION";

	/** The Constant SEQ_KEY. */
	public static final String SEQ_KEY = Random.randomAlpha(1, "LMNOPQR") + Random.randomAlpha(3);

	/** The Constant CACHE_TIME. */
	public static final int CACHE_TIME = 31556926;

	public enum Features {
		BENEFICIARY, PLACEORDER, RATE_ALERT, REMIT, FXORDER;

		@JsonValue
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}

	}

}
