package com.amx.jax.dbmodel.partner;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_PAYMENT_MODE_LIMIT")
public class PaymentModeLimitsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal paymentModeLimitIdl;
	private BigDecimal applicationCountryId;
	private BigDecimal companyId;
	private BigDecimal bankId;
	private BigDecimal currencyId;
	private String customerTypeFrom;
	private String customerTypeTo;
	private BigDecimal cashLimit;
	private BigDecimal knetLimit;
	
	@Id
	@Column(name = "PAYMENT_MODE_LIMIT_ID")
	public BigDecimal getPaymentModeLimitIdl() {
		return paymentModeLimitIdl;
	}
	public void setPaymentModeLimitIdl(BigDecimal paymentModeLimitIdl) {
		this.paymentModeLimitIdl = paymentModeLimitIdl;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name = "COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	
	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name = "CUSTOMER_TYPE_FROM")
	public String getCustomerTypeFrom() {
		return customerTypeFrom;
	}
	public void setCustomerTypeFrom(String customerTypeFrom) {
		this.customerTypeFrom = customerTypeFrom;
	}
	
	@Column(name = "CUSTOMER_TYPE_TO")
	public String getCustomerTypeTo() {
		return customerTypeTo;
	}
	public void setCustomerTypeTo(String customerTypeTo) {
		this.customerTypeTo = customerTypeTo;
	}
	
	@Column(name = "CASH_LIMIT")
	public BigDecimal getCashLimit() {
		return cashLimit;
	}
	public void setCashLimit(BigDecimal cashLimit) {
		this.cashLimit = cashLimit;
	}
	
	@Column(name = "KNET_LIMIT")
	public BigDecimal getKnetLimit() {
		return knetLimit;
	}
	public void setKnetLimit(BigDecimal knetLimit) {
		this.knetLimit = knetLimit;
	}

}
