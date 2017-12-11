package com.amx.jax.util;

import java.math.BigDecimal;

public class Util {
	

	public static boolean isNullZeroBigDecimalCheck(BigDecimal value) {
		if(value != null && value.compareTo(BigDecimal.ZERO)!=0) {
			return true;
		}else {
			return false;
		}
	}
}
