package com.amx.utils;

import java.math.BigDecimal;

public class NumberUtil {

	/**
	 * @param bigdecial
	 *            to test
	 * @return whether passed bigdecimal is integer or not
	 * 
	 */
	public static boolean isIntegerValue(BigDecimal bd) {
		if (bd == null) {
			return false;
		}
		return bd.stripTrailingZeros().scale() <= 0;
	}
}
