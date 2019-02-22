package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class ExchangeRateDetails implements Serializable, Comparable<ExchangeRateDetails> {

	private static final long serialVersionUID = -4224946950188132064L;

	@NotNull
	private BigDecimal bankId;
	private BigDecimal serviceIndicatorId;

	@NotNull
	private ExchangeRateBreakup sellRateNet;

	private ExchangeRateBreakup sellRateBase;

	private Map<DISCOUNT_TYPE, String> discountPipsDetails;

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getServiceIndicatorId() {
		return serviceIndicatorId;
	}

	public void setServiceIndicatorId(BigDecimal serviceIndicatorId) {
		this.serviceIndicatorId = serviceIndicatorId;
	}

	public ExchangeRateBreakup getSellRateNet() {
		return sellRateNet;
	}

	public void setSellRateNet(ExchangeRateBreakup sellRateNet) {
		this.sellRateNet = sellRateNet;
	}

	public ExchangeRateBreakup getSellRateBase() {
		return sellRateBase;
	}

	public void setSellRateBase(ExchangeRateBreakup sellRateBase) {
		this.sellRateBase = sellRateBase;
	}

	public Map<DISCOUNT_TYPE, String> getDiscountPipsDetails() {
		return discountPipsDetails;
	}

	public void setDiscountPipsDetails(Map<DISCOUNT_TYPE, String> discountPipsDetails) {
		this.discountPipsDetails = discountPipsDetails;
	}

	@Override
	public int compareTo(ExchangeRateDetails o) {
		return this.sellRateNet.compareTo(o.sellRateNet);
	}

}
