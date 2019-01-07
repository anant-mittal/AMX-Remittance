package com.amx.libjax.model.postman;

import java.math.BigDecimal;

public class SuspiciousTransactionPaymentDto {

	String remitterReferenceNo;
	String remitterName;
	String nationalityName;
	String beneName;
	String bankName;
	String product;
	String beneBankName;
	Long noOfAttempts;
	String customerEmailId;
	String countryName;
	String customerMobile;
	public String getRemitterReferenceNo() {
		return remitterReferenceNo;
	}
	public void setRemitterReferenceNo(String remitterReferenceNo) {
		this.remitterReferenceNo = remitterReferenceNo;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	
	
	public Long getNoOfAttempts() {
		return noOfAttempts;
	}
	public void setNoOfAttempts(Long noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	
}
