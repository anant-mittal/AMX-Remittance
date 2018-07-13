package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.amxlib.constant.RuleEnum;
import com.amx.jax.model.AbstractModel;

public class RateAlertDTO extends AbstractModel implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal rateAlertId;
	private BigDecimal customerId;
	private Date fromDate;
	private Date toDate;
	private RuleEnum rule;
	private BigDecimal alertRate;
	private BigDecimal baseCurrencyId;
	private String baseCurrencyQuote;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyQuote;
	private String isActive;
	private BigDecimal payAmount;
	private BigDecimal receiveAmount;
	private String alertEmail;
	private String alertSms;
	private String customerFirstName;
	private String customerMiddleName;
	private String customerLastName;
	
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
	public RuleEnum getRule() {
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	public void setRule(RuleEnum rule) {
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

	/**
	 * @return the payAmount
	 */
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return the receiveAmount
	 */
	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	/**
	 * @param receiveAmount the receiveAmount to set
	 */
	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
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
				+ foreignCurrencyId + ", foreignCurrencyQuote=" + foreignCurrencyQuote + ", isActive=" + isActive
				+ ", payAmount=" + payAmount + ", receiveAmount=" + receiveAmount + "]";
	}

	public String getAlertEmail() {
		return alertEmail;
	}

	public void setAlertEmail(String alertEmail) {
		this.alertEmail = alertEmail;
	}

	public String getAlertSms() {
		return alertSms;
	}

	public void setAlertSms(String alertSms) {
		this.alertSms = alertSms;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerMiddleName() {
		return customerMiddleName;
	}

	public void setCustomerMiddleName(String customerMiddleName) {
		this.customerMiddleName = customerMiddleName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rateAlertId == null) ? 0 : rateAlertId.hashCode());
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
		RateAlertDTO other = (RateAlertDTO) obj;
		if (rateAlertId == null) {
			if (other.rateAlertId != null)
				return false;
		} else if (!rateAlertId.equals(other.rateAlertId))
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	



}
