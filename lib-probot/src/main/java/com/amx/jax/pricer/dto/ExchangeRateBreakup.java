package com.amx.jax.pricer.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class ExchangeRateBreakup implements Cloneable, Comparable<ExchangeRateBreakup> {

	@NotNull
	BigDecimal rate;

	@NotNull
	BigDecimal inverseRate;

	@NotNull
	BigDecimal convertedFCAmount;

	@NotNull
	BigDecimal convertedLCAmount;

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

	public void setConvertedFCAmount(BigDecimal convertedFCAmount) {
		this.convertedFCAmount = convertedFCAmount;
	}

	public BigDecimal getConvertedLCAmount() {
		return convertedLCAmount;
	}

	public void setConvertedLCAmount(BigDecimal convertedLCAmount) {
		this.convertedLCAmount = convertedLCAmount;
	}

	public BigDecimal getConvertedFCAmount() {
		return convertedFCAmount;
	}

	@Override
	public int compareTo(ExchangeRateBreakup o) {
		if (null == o) {
			return 1;
		}
		return this.rate.compareTo(o.rate);
	}

	@Override
	public ExchangeRateBreakup clone() {

		try {
			return (ExchangeRateBreakup) super.clone();
		} catch (CloneNotSupportedException e) {

			ExchangeRateBreakup breakup = new ExchangeRateBreakup();
			breakup.rate = this.rate;
			breakup.inverseRate = this.inverseRate;
			breakup.convertedFCAmount = this.convertedFCAmount;
			breakup.convertedLCAmount = this.convertedLCAmount;

			return breakup;

		}

	}// Clone

}
