package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.dict.UserClient.Channel;

public class PricingReqDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7408571124880744930L;

	private BigDecimal customerId;
	private BigDecimal beneId;
	private BigDecimal sourceOfFund;
	private BigDecimal localAmount;
	private BigDecimal foreignAmount;
	private BigDecimal defaultCurrencyId;
	private BigDecimal countryBranchId;
	private boolean availLoyalityPoints;
	private Channel channel;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

	public BigDecimal getSourceOfFund() {
		return sourceOfFund;
	}

	public void setSourceOfFund(BigDecimal sourceOfFund) {
		this.sourceOfFund = sourceOfFund;
	}

	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public boolean isAvailLoyalityPoints() {
		return availLoyalityPoints;
	}

	public void setAvailLoyalityPoints(boolean availLoyalityPoints) {
		this.availLoyalityPoints = availLoyalityPoints;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getDefaultCurrencyId() {
		return defaultCurrencyId;
	}

	public void setDefaultCurrencyId(BigDecimal defaultCurrencyId) {
		this.defaultCurrencyId = defaultCurrencyId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

}
