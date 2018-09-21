package com.amx.utils;

import java.util.Map;

import com.google.common.base.Splitter;

public final class StringUtils {

	public static boolean isNotBlank(String str) {
		return !ArgUtil.isEmptyString(str);
	}

	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
			String data) {
		return Splitter.on(splitter_char).withKeyValueSeparator(key_value_separator_char).split(data);
	}

	public static int hash(String str, int max) {
		int hash = max;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * 31 + str.charAt(i);
		}
		return (int) Math.abs(hash % max);
	}
}
