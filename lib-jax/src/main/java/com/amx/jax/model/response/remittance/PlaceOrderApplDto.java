package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class PlaceOrderApplDto {

	private BigDecimal placeOrderId;     
	private BigDecimal companyId;        
	private BigDecimal documentFinanceYear;        
	private BigDecimal documentId;       
	private BigDecimal beneficiaryId;        
	private String beneficiaryName; 
	private String beneficiaryBank; 
	private String beneficiaryBranch; 
	private String beneficiaryAccountNo;
	private BigDecimal customerId;
	private BigDecimal foreigncurrency;
	private BigDecimal localcurrency;
	private String remarks;
	private String createdBy;
	private BigDecimal exchangeRate;
	private BigDecimal exchangeRateOfferd;
	private BigDecimal foreignTranxAmount;  
	private BigDecimal localTranxAmount;
	private String customerName;
	private BigDecimal customerReference;
	private String approvedBy;
	private String foreigncurrencyName;
	private String localcurrencyName;
	private String civilId;
	private String specialOrCommonPoolIndicator;
	private BigDecimal routingCountry;
	private String routingCountryName;
	private String routingBankName;
	private String customerEmailId;
	private Boolean boonegotiate=false;
	private String status;
	private BigDecimal orignalExchangeRate;
	private BigDecimal routingBankId;
	
	private BigDecimal discount;
	
	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}
	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}
	public BigDecimal getDocumentId() {
		return documentId;
	}
	public void setDocumentId(BigDecimal documentId) {
		this.documentId = documentId;
	}
	public BigDecimal getBeneficiaryId() {
		return beneficiaryId;
	}
	public void setBeneficiaryId(BigDecimal beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getBeneficiaryBank() {
		return beneficiaryBank;
	}
	public void setBeneficiaryBank(String beneficiaryBank) {
		this.beneficiaryBank = beneficiaryBank;
	}
	public String getBeneficiaryBranch() {
		return beneficiaryBranch;
	}
	public void setBeneficiaryBranch(String beneficiaryBranch) {
		this.beneficiaryBranch = beneficiaryBranch;
	}
	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}
	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getForeigncurrency() {
		return foreigncurrency;
	}
	public void setForeigncurrency(BigDecimal foreigncurrency) {
		this.foreigncurrency = foreigncurrency;
	}
	public BigDecimal getLocalcurrency() {
		return localcurrency;
	}
	public void setLocalcurrency(BigDecimal localcurrency) {
		this.localcurrency = localcurrency;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getExchangeRateOfferd() {
		return exchangeRateOfferd;
	}
	public void setExchangeRateOfferd(BigDecimal exchangeRateOfferd) {
		this.exchangeRateOfferd = exchangeRateOfferd;
	}
	public BigDecimal getForeignTranxAmount() {
		return foreignTranxAmount;
	}
	public void setForeignTranxAmount(BigDecimal foreignTranxAmount) {
		this.foreignTranxAmount = foreignTranxAmount;
	}
	public BigDecimal getLocalTranxAmount() {
		return localTranxAmount;
	}
	public void setLocalTranxAmount(BigDecimal localTranxAmount) {
		this.localTranxAmount = localTranxAmount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getForeigncurrencyName() {
		return foreigncurrencyName;
	}
	public void setForeigncurrencyName(String foreigncurrencyName) {
		this.foreigncurrencyName = foreigncurrencyName;
	}
	public String getLocalcurrencyName() {
		return localcurrencyName;
	}
	public void setLocalcurrencyName(String localcurrencyName) {
		this.localcurrencyName = localcurrencyName;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getSpecialOrCommonPoolIndicator() {
		return specialOrCommonPoolIndicator;
	}
	public void setSpecialOrCommonPoolIndicator(String specialOrCommonPoolIndicator) {
		this.specialOrCommonPoolIndicator = specialOrCommonPoolIndicator;
	}
	public BigDecimal getRoutingCountry() {
		return routingCountry;
	}
	public void setRoutingCountry(BigDecimal routingCountry) {
		this.routingCountry = routingCountry;
	}
	public String getRoutingCountryName() {
		return routingCountryName;
	}
	public void setRoutingCountryName(String routingCountryName) {
		this.routingCountryName = routingCountryName;
	}
	public String getRoutingBankName() {
		return routingBankName;
	}
	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public Boolean getBoonegotiate() {
		return boonegotiate;
	}
	public void setBoonegotiate(Boolean boonegotiate) {
		this.boonegotiate = boonegotiate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getOrignalExchangeRate() {
		return orignalExchangeRate;
	}
	public void setOrignalExchangeRate(BigDecimal orignalExchangeRate) {
		this.orignalExchangeRate = orignalExchangeRate;
	}
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}     
	
	
}
