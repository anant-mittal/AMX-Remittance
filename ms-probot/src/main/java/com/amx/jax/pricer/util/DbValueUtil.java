package com.amx.jax.pricer.util;

import com.amx.utils.StringUtils;

public class DbValueUtil {

	public static String BIG_Y = "Y";
	public static String BIG_YES = "YES";

	public static boolean isActive(String flagVal) {

		if (flagVal == null)
			return false;

		return StringUtils.anyMatch(flagVal, BIG_Y, BIG_YES);
	}

}
