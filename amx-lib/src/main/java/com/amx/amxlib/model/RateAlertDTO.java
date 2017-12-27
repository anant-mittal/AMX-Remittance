package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

public class RateAlertDTO extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal rateAlertId;
	private BigDecimal customerId;
	private Date fromDate;
	private Date toDate;
	private String rule;
	private BigDecimal alertRate;
	private BigDecimal baseCurrencyId;
	private String baseCurrencyQuote;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyQuote;
	private String isActive;
	
	/**
	 * @return the rateAlertId
	 */
	public BigDecimal getRateAlertId() {
		return rateAlertId;
	}

	/**
	 * @param rateAlertId the rateAlertId to set
	 */
	public void setRateAlertId(BigDecimal rateAlertId) {
		this.rateAlertId = rateAlertId;
	}

	/**
	 * @return the customerId
	 */
	public BigDecimal getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}

	/**
	 * @return the alertRate
	 */
	public BigDecimal getAlertRate() {
		return alertRate;
	}

	/**
	 * @param alertRate the alertRate to set
	 */
	public void setAlertRate(BigDecimal alertRate) {
		this.alertRate = alertRate;
	}

	/**
	 * @return the baseCurrencyId
	 */
	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	/**
	 * @param baseCurrencyId the baseCurrencyId to set
	 */
	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	/**
	 * @return the baseCurrencyQuote
	 */
	public String getBaseCurrencyQuote() {
		return baseCurrencyQuote;
	}

	/**
	 * @param baseCurrencyQuote the baseCurrencyQuote to set
	 */
	public void setBaseCurrencyQuote(String baseCurrencyQuote) {
		this.baseCurrencyQuote = baseCurrencyQuote;
	}

	/**
	 * @return the foreignCurrencyId
	 */
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	/**
	 * @param foreignCurrencyId the foreignCurrencyId to set
	 */
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	/**
	 * @return the foreignCurrencyQuote
	 */
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}

	/**
	 * @param foreignCurrencyQuote the foreignCurrencyQuote to set
	 */
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}

	/* (non-Javadoc)
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return "rate-alert-dto";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RateAlertDTO [rateAlertId=" + rateAlertId + ", customerId=" + customerId + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", rule=" + rule + ", alertRate=" + alertRate + ", baseCurrencyId="
				+ baseCurrencyId + ", baseCurrencyQuote=" + baseCurrencyQuote + ", foreignCurrencyId="
				+ foreignCurrencyId + ", foreignCurrencyQuote=" + foreignCurrencyQuote + ", isActive=" + isActive + "]";
	}
	



}
