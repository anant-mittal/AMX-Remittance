package com.amx.jax;

import java.math.BigDecimal;

import com.amx.jax.customer.ICustRegService.RegModeModel;

public class CustRegRequestModel implements RegModeModel {

	BigDecimal mode;

	@Override
	public BigDecimal getMode() {
		return this.mode;
	}

	@Override
	public void setMode(BigDecimal mode) {
		this.mode = mode;
	}

}
