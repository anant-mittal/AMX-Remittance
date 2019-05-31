package com.amx.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

	public static class StringMatcher {
		String str;
		Matcher m;

		public StringMatcher(String str) {
			this.str = str;
		}

		public boolean match(Pattern pattern) {
			this.m = pattern.matcher(str);
			return this.m != null && this.m.find();
		}

		public boolean find() {
			return this.m != null && this.m.find();
		}

		public String group(int index) {
			return this.m.group(index);
		}

	}

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

	public static String trim(String str) {
		return (str == null) ? str : str.trim();
	}

	public static String pad(String src, String pad, int alignment, int trim) {
		if (alignment == 0) {
			String fullPart = pad + src;
			if (trim == 0) {
				return fullPart.substring(Math.min(src.length(), pad.length()));
			} else {
				return fullPart.substring(src.length());
			}
		} else {
			String fullPart = src + pad;
			if (trim == 0) {
				return fullPart.substring(0, Math.max(src.length(), pad.length()));
			} else {
				return fullPart.substring(0, pad.length());
			}
		}

	}

	public static String substring(String str, int length) {
		if (str == null || str.length() <= 0) {
			return Constants.BLANK;
		} else if (str.length() <= length) {
			return str;
		} else {
			return str.substring(0, length);
		}
	}
}
