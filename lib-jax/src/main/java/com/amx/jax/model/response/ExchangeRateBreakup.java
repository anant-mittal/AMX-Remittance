package com.amx.jax.model.response;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeRateBreakup implements Comparable<ExchangeRateBreakup> {

	@JsonProperty("domXRate")
	@NotNull
	BigDecimal rate;

	@JsonProperty("forXRate")
	BigDecimal inverseRate;

	@JsonProperty("forAmount")
	BigDecimal convertedFCAmount;

	@JsonProperty("domAmount")
	BigDecimal convertedLCAmount;

	BigDecimal netAmount;

	BigDecimal netAmountWithoutLoyality;

	BigDecimal lcDecimalNumber;

	BigDecimal fcDecimalNumber;
	
	BigDecimal deliveryCharges;
	
	BigDecimal baseRate;

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

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getNetAmountWithoutLoyality() {
		return netAmountWithoutLoyality;
	}

	public void setNetAmountWithoutLoyality(BigDecimal netAmountWithoutLoyality) {
		this.netAmountWithoutLoyality = netAmountWithoutLoyality;
	}

	public BigDecimal getFcDecimalNumber() {
		return fcDecimalNumber;
	}

	public void setFcDecimalNumber(BigDecimal fcDecimalNumber) {
		this.fcDecimalNumber = fcDecimalNumber;
	}

	public BigDecimal getLcDecimalNumber() {
		return lcDecimalNumber;
	}

	public void setLcDecimalNumber(BigDecimal lcDecimalNumber) {
		this.lcDecimalNumber = lcDecimalNumber;
	}

	public BigDecimal getBaseRate() {
		return baseRate;
	}
	
	public void setBaseRate(BigDecimal baseRate) {
		this.baseRate = baseRate;
	}

	@Override
	public int compareTo(ExchangeRateBreakup o) {
		return this.rate.compareTo(o.rate);
	}

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExchangeRateBreakup other = (ExchangeRateBreakup) obj;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		return true;
	}

}
