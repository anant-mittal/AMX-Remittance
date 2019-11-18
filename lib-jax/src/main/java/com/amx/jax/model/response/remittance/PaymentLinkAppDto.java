package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentLinkAppDto {
	private BigDecimal remittanceApplicationId;
	private Date linkDate;
	private BigDecimal amount;
	private BigDecimal customerId;
	private BigDecimal exchangeRate;
	private BigDecimal foreignAmount;
	private BigDecimal documentDate;
	private String applIsActive;
	private String foreignCurrencyDesc;
	public BigDecimal getRemittanceApplicationId() {
		return remittanceApplicationId;
	}
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}
	public Date getLinkDate() {
		return linkDate;
	}
	public void setLinkDate(Date linkDate) {
		this.linkDate = linkDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}
	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}
	public BigDecimal getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(BigDecimal documentDate) {
		this.documentDate = documentDate;
	}
	public String getApplIsActive() {
		return applIsActive;
	}
	public void setApplIsActive(String applIsActive) {
		this.applIsActive = applIsActive;
	}
	public String getForeignCurrencyDesc() {
		return foreignCurrencyDesc;
	}
	public void setForeignCurrencyDesc(String foreignCurrencyDesc) {
		this.foreignCurrencyDesc = foreignCurrencyDesc;
	}
	
	
}
