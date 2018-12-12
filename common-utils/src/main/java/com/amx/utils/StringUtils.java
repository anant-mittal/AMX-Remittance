package com.amx.utils;

import java.util.HashMap;
import java.util.Map;

public final class StringUtils {

	public static boolean isNotBlank(String str) {
		return !ArgUtil.isEmptyString(str);
	}

//	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
//			String data) {
//		return Splitter.on(splitter_char).withKeyValueSeparator(key_value_separator_char).split(data);
//	}

	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
			String data) {
		Map<String, String> map = new HashMap<String, String>();
		String[] stubs = data.split(splitter_char);
		for (String string : stubs) {
			String[] stub = string.split(key_value_separator_char);
			map.put(stub[0], stub[1]);
		}
		return map;
	}

	public static int hash(String str, int max) {
		int hash = max;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * 31 + str.charAt(i);
		}
		return (int) Math.abs(hash % max);
	}
}
