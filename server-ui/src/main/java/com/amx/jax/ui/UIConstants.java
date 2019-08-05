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
		DASHBOARD,
		BENE_VIEW, BENE_UPDATE,
		PLACEORDER, REMIT, FXORDER, EXCHANGE_RATE, TRANX_HISTORY,
		MY_INFO_VIEW,
		SQA_UPDATE,
		EMAIL_UPDATE,
		MOBILE_UPDATE, FINGERPRINT_UPDATE,
		EMP_DETAILS_UPDATE, PSWD_UPDATE,

		// PERMISSIONED
		INSURANCE,

		// DEFAULT
		DEFAULT;

		@JsonValue
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}

	}

	/**
	 * IMPORTANT:- All the milestones MUST be in order. between ZERO and INFINITY
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum MileStone {
		ZERO,
		// Add MileStones after this;

		DPR, GIG, HOMESEND,

		// Add MileStones before this;
		FUTRUE;

		private static final int latest = FUTRUE.ordinal() - 1;
		public static final MileStone[] LIST = MileStone.values();

		@JsonValue
		@Override
		public String toString() {
			return this.name();
		}

		public boolean isLegacy() {

			return this.ordinal() < latest;
		}

	}

}
