package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FxExchangeRateBreakup {

	@JsonProperty("domXRate")
	@NotNull
	BigDecimal rate;

	@JsonProperty("forXRate")
	BigDecimal inverseRate;

	@JsonProperty("forAmount")
	BigDecimal convertedFCAmount=BigDecimal.ZERO;

	@JsonProperty("domAmount")
	BigDecimal convertedLCAmount=BigDecimal.ZERO;

	BigDecimal netAmount=BigDecimal.ZERO;

	BigDecimal netAmountWithoutLoyality;

	BigDecimal lcDecimalNumber;

	BigDecimal fcDecimalNumber;
	
	BigDecimal deliveryCharges=BigDecimal.ZERO;

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



	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
}