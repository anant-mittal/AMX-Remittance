package com.amx.jax.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionFactory {
	private static Map<String, AmxApiException> map = new HashMap<String, AmxApiException>();

	private static Map<String, AmxApiException> clasmap = new HashMap<String, AmxApiException>();

	public static void register(AmxApiException exc) {
		clasmap.put(exc.getClass().getName(), exc);
	}

	public static void register(String key, AmxApiException exc) {
		map.put(key, exc);
	}

	public static AmxApiException get(String key) {
		return map.get(key);
	}
}
