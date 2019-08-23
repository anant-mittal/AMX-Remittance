package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class ExchangeRateDetails implements Serializable, Cloneable, Comparable<ExchangeRateDetails> {

	private static final long serialVersionUID = -4224946950188132064L;

	@NotNull
	private BigDecimal bankId;
	private BigDecimal serviceIndicatorId;

	@NotNull
	private ExchangeRateBreakup sellRateNet;

	private ExchangeRateBreakup sellRateBase;

	private Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails;

	private boolean isDiscountAvailed = false;

	private boolean isCostRateLimitReached = false;

	private boolean isLowGLBalance = false;
	
	private boolean isBetterRateAvailable = false;

	private BigDecimal betterRateAmountSlab;

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

	public Map<DISCOUNT_TYPE, ExchangeDiscountInfo> getCustomerDiscountDetails() {
		return customerDiscountDetails;
	}

	public void setCustomerDiscountDetails(Map<DISCOUNT_TYPE, ExchangeDiscountInfo> discountPipsDetails) {
		this.customerDiscountDetails = discountPipsDetails;
	}

	public boolean isDiscountAvailed() {
		return isDiscountAvailed;
	}

	public void setDiscountAvailed(boolean isDiscountAvailed) {
		this.isDiscountAvailed = isDiscountAvailed;
	}

	public boolean isCostRateLimitReached() {
		return isCostRateLimitReached;
	}

	public void setCostRateLimitReached(boolean isCostRateLimitReached) {
		this.isCostRateLimitReached = isCostRateLimitReached;
	}

	public boolean isLowGLBalance() {
		return isLowGLBalance;
	}

	public void setLowGLBalance(boolean isLowGLBalance) {
		this.isLowGLBalance = isLowGLBalance;
	}

	public boolean isBetterRateAvailable() {
		return isBetterRateAvailable;
	}

	public void setBetterRateAvailable(boolean isBetterRateAvailable) {
		this.isBetterRateAvailable = isBetterRateAvailable;
	}

	public BigDecimal getBetterRateAmountSlab() {
		return betterRateAmountSlab;
	}

	public void setBetterRateAmountSlab(BigDecimal betterRateAmountSlab) {
		this.betterRateAmountSlab = betterRateAmountSlab;
	}

	@Override
	public int compareTo(ExchangeRateDetails o) {

		if (null != this.sellRateNet && null != o.sellRateNet) {
			return this.sellRateNet.compareTo(o.sellRateNet);
		} else if (null != this.sellRateBase && null != o.sellRateBase) {
			return this.sellRateBase.compareTo(o.sellRateBase);
		}

		return 1;
	}

	@Override
	public ExchangeRateDetails clone() {

		try {

			ExchangeRateDetails cloned = (ExchangeRateDetails) super.clone();

			// cloned.customerDiscountDetails =
			// this.customerDiscountDetails.entrySet().stream()
			// .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (this.customerDiscountDetails != null) {
				cloned.customerDiscountDetails = new HashMap<DISCOUNT_TYPE, ExchangeDiscountInfo>();

				for (Entry<DISCOUNT_TYPE, ExchangeDiscountInfo> entry : this.customerDiscountDetails.entrySet()) {
					cloned.customerDiscountDetails.put(entry.getKey(), entry.getValue().clone());
				}
			}

			return cloned;

		} catch (CloneNotSupportedException e) {

			ExchangeRateDetails cloned = new ExchangeRateDetails();
			cloned.bankId = this.bankId;
			cloned.serviceIndicatorId = this.serviceIndicatorId;
			cloned.sellRateBase = this.sellRateBase.clone();
			cloned.sellRateNet = this.sellRateNet.clone();
			cloned.isDiscountAvailed = this.isDiscountAvailed;
			cloned.isCostRateLimitReached = this.isCostRateLimitReached;

			// cloned.customerDiscountDetails =
			// this.customerDiscountDetails.entrySet().stream()
			// .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (this.customerDiscountDetails != null) {
				cloned.customerDiscountDetails = new HashMap<DISCOUNT_TYPE, ExchangeDiscountInfo>();

				for (Entry<DISCOUNT_TYPE, ExchangeDiscountInfo> entry : this.customerDiscountDetails.entrySet()) {
					cloned.customerDiscountDetails.put(entry.getKey(), entry.getValue().clone());
				}
			}

			return cloned;

		}

	}// Clone

}
