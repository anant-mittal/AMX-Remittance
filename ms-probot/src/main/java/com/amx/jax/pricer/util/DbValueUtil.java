package com.amx.jax.pricer.util;

import static com.amx.jax.pricer.var.PricerServiceConstants.BIG_Y;
import static com.amx.jax.pricer.var.PricerServiceConstants.BIG_YES;

import java.util.stream.Stream;

public class DbValueUtil {

	public static boolean isActive(String flagVal) {

		if (flagVal == null)
			return false;

		return Stream.of(BIG_Y, BIG_YES).anyMatch(flagVal::equalsIgnoreCase);
	}

}
