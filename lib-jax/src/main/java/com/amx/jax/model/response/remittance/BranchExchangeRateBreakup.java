package com.amx.jax.model.response.remittance;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.amx.jax.model.response.ExchangeRateBreakup;

public class BranchExchangeRateBreakup extends ExchangeRateBreakup {

	public BranchExchangeRateBreakup(ExchangeRateBreakup exRateBreakup) {
		try {
			BeanUtils.copyProperties(this, exRateBreakup);
		} catch (IllegalAccessException | InvocationTargetException e) {
		}
	}

	public BranchExchangeRateBreakup() {
		super();
	}

}
