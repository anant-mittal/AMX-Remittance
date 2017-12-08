package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

public class ExchangeRateResponseModel extends AbstractModel {

	BigDecimal rate;
	
	@Override
	public String getModelType() {
		return "ex_rate";
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
