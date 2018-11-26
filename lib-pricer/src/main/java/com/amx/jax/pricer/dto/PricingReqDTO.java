package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

public class PricingReqDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7408571124880744930L;

	private BigDecimal customerId;
	private BigDecimal beneId;
	private BigDecimal lCountryId;
	private BigDecimal fcCountryId;

	private BigDecimal localAmount;
	private BigDecimal foreignAmount;

	private BigDecimal localCurrencyId;
	private BigDecimal foreignCurrencyId;

	private BigDecimal countryBranchId;

	private List<BigDecimal> routingBankIds;


	private PRICE_BY pricingLevel;

	/**
	 * Required by EX_EXchange_rate_master_APRDET;
	 */
	private BigDecimal serviceIndicatorId;
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

	public BigDecimal getFcCountryId() {
		return fcCountryId;
	}

	public void setFcCountryId(BigDecimal fcCountryId) {
		this.fcCountryId = fcCountryId;
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

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getDefaultCurrencyId() {
		return localCurrencyId;
	}

	public void setDefaultCurrencyId(BigDecimal defaultCurrencyId) {
		this.localCurrencyId = defaultCurrencyId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public BigDecimal getLocalCurrencyId() {
		return localCurrencyId;
	}

	public void setLocalCurrencyId(BigDecimal localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public BigDecimal getServiceIndicatorId() {
		return serviceIndicatorId;
	}

	public void setServiceIndicatorId(BigDecimal serviceIndicatorId) {
		this.serviceIndicatorId = serviceIndicatorId;
	}

	public List<BigDecimal> getRoutingBankIds() {
		return routingBankIds;
	}

	public void setRoutingBankIds(List<BigDecimal> routingBankIds) {
		this.routingBankIds = routingBankIds;
	}

	public BigDecimal getCountryId() {
		return lCountryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.lCountryId = countryId;
	}

	public PRICE_BY getPricingLevel() {
		return pricingLevel;
	}

	public void setPricingLevel(PRICE_BY pricingLevel) {
		this.pricingLevel = pricingLevel;
	}

}
