package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeRateBreakup {

	@JsonProperty("domXRate")
	BigDecimal rate;

	@JsonProperty("forXRate")
	BigDecimal inverseRate;

	@JsonProperty("forAmount")
	BigDecimal conversionAmount;

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getInverseRate() {
		return inverseRate;
	}

	public void setInverseRate(BigDecimal inverseRate) {
		this.inverseRate = inverseRate;
	}

	public BigDecimal getConversionAmount() {
		return conversionAmount;
	}

	public void setConversionAmount(BigDecimal conversionAmount) {
		this.conversionAmount = conversionAmount;
	}
}
