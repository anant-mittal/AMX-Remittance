package com.amx.jax;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;

public interface AmxSharedConfig extends AppSharedConfig {

	public interface AmxSharedConfigDB extends AmxSharedConfig {

	}

	public interface AmxSharedConfigApi extends AmxSharedConfig {

		public static class Path {

			public static final String PREFIX = "/meta/config";
			public static final String COMM_PREFS = PREFIX + "/comm/prefs";

		}

		public static class Params {
			public static final String SAMPLE = "sample";
		}
	}

	public static interface CommunicationPrefs {

		CommunicationEvents getEvent();

		Long getSmsPrefs();

		Long getEmailPrefs();

		Long getWaPrefs();

		Long getPushPrefs();

	}

	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs();

}
