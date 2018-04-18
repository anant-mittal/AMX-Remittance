package com.amx.jax.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionFactory {
	private static Map<String, AbstractAppException> map = new HashMap<String, AbstractAppException>();

	private static Map<String, AbstractAppException> clasmap = new HashMap<String, AbstractAppException>();

	public static void register(AbstractAppException exc) {
		clasmap.put(exc.getClass().getName(), exc);
	}

	public static void register(String key, AbstractAppException exc) {
		map.put(key, exc);
	}

	public static AbstractAppException get(String key) {
		return map.get(key);
	}
}
