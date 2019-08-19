package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.dict.UserClient.Channel;

public class CustomerDiscountReqDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal foreignCurrencyId;
	private BigDecimal customerId;
	public BigDecimal bankId;
	private BigDecimal foreignAmount;
	
	private Channel channel;

	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

}
