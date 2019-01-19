package com.amx.libjax.model.postman;

import java.math.BigDecimal;

public class SuspiciousTransactionAddBeneDto {

	String transactionReference;
	String remitterName;
	String nationalityName;
	String beneName;
	BigDecimal transactionAmountDom;
	String countryName;
	// cash or bank
	String product;
	String beneBankName;
	BigDecimal noOfAttempts;
	String customerEmailId;
	String customerMobile;
	
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	public String getRemitterName() {
		return remitterName;
	}
	public void setRemitterName(String remitterName) {
		this.remitterName = remitterName;
	}
	public String getNationalityName() {
		return nationalityName;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}
	public String getBeneName() {
		return beneName;
	}
	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}
	public BigDecimal getTransactionAmountDom() {
		return transactionAmountDom;
	}
	public void setTransactionAmountDom(BigDecimal transactionAmountDom) {
		this.transactionAmountDom = transactionAmountDom;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getBeneBankName() {
		return beneBankName;
	}
	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}
	public BigDecimal getNoOfAttempts() {
		return noOfAttempts;
	}
	public void setNoOfAttempts(BigDecimal noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
}
