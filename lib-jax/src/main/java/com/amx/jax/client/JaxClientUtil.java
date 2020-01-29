package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.CryptoUtil.HashBuilder;

public class JaxClientUtil {
	public static HashBuilder getTransactionVeryCode(BigDecimal transactionId) {
		return new HashBuilder().message(ArgUtil.parseAsString(transactionId, Constants.BLANK))
				.toSHA2().toNumeric(8);
	}
}
