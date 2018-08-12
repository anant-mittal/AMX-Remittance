package com.amx.utils;

public final class StringUtils {

	public static boolean isNotBlank(String str) {
		return !ArgUtil.isEmptyString(str);
	}
}
