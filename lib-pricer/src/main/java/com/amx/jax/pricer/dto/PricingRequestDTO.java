package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

public class PricingRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7408571124880744930L;

	private BigDecimal customerId;
	private BigDecimal beneId;

	@NotNull(message = "Local Country Id Can not be Null or Empty")
	private BigDecimal localCountryId;

	@NotNull(message = "Foreign Country Id Can not be Null or Empty")
	private BigDecimal foreignCountryId;

	private BigDecimal localAmount;
	private BigDecimal foreignAmount;

	@NotNull(message = "Local Currency Id Can not be Null or Empty")
	private BigDecimal localCurrencyId;

	@NotNull(message = "Foreign Currency Id Can not be Null or Empty")
	private BigDecimal foreignCurrencyId;

	@NotNull(message = "Country Branch Id Can not be Null or Empty")
	private BigDecimal countryBranchId;

	private List<BigDecimal> routingBankIds;

	@NotNull(message = "Pricing Level Can not be Null or Empty")
	private PRICE_BY pricingLevel;

	@NotNull(message = "Channel Can not be Null or Empty")
	private Channel channel;

	private Map<String, Object> info;

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

	public BigDecimal getLocalCountryId() {
		return localCountryId;
	}

	public void setLocalCountryId(BigDecimal localCountryId) {
		this.localCountryId = localCountryId;
	}

	public BigDecimal getForeignCountryId() {
		return foreignCountryId;
	}

	public void setForeignCountryId(BigDecimal foreignCountryId) {
		this.foreignCountryId = foreignCountryId;
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

	public List<BigDecimal> getRoutingBankIds() {
		return routingBankIds;
	}

	public void setRoutingBankIds(List<BigDecimal> routingBankIds) {
		this.routingBankIds = routingBankIds;
	}

	public PRICE_BY getPricingLevel() {
		return pricingLevel;
	}

	public void setPricingLevel(PRICE_BY pricingLevel) {
		this.pricingLevel = pricingLevel;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}


}
