package com.amx.jax.model.request.partner;

import java.math.BigDecimal;

public class TransactionFailReportDTO {
	
	private String transactionId;
	private String routingBankCode;
	private String beneficiaryAccount;
	private String localCurrencyQuote;
	private String foreignCurrencyQuote;
	private String beneficiaryBankName;
	private String beneficiaryBranchName;
	private String beneficiaryName;
	private String customerName;
	private String customerContact;
	private String exceptionMessage;
	private BigDecimal customerReference;
	private BigDecimal transactionAmount;
	private BigDecimal transactionForeignAmount;
	private BigDecimal transactionDocYear;
	private BigDecimal transactionDocNumber;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getRoutingBankCode() {
		return routingBankCode;
	}
	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}
	
	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}
	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}
	
	public String getBeneficiaryBankName() {
		return beneficiaryBankName;
	}
	public void setBeneficiaryBankName(String beneficiaryBankName) {
		this.beneficiaryBankName = beneficiaryBankName;
	}
	
	public String getBeneficiaryBranchName() {
		return beneficiaryBranchName;
	}
	public void setBeneficiaryBranchName(String beneficiaryBranchName) {
		this.beneficiaryBranchName = beneficiaryBranchName;
	}
	
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public BigDecimal getTransactionDocYear() {
		return transactionDocYear;
	}
	public void setTransactionDocYear(BigDecimal transactionDocYear) {
		this.transactionDocYear = transactionDocYear;
	}
	
	public BigDecimal getTransactionDocNumber() {
		return transactionDocNumber;
	}
	public void setTransactionDocNumber(BigDecimal transactionDocNumber) {
		this.transactionDocNumber = transactionDocNumber;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	
	public String getLocalCurrencyQuote() {
		return localCurrencyQuote;
	}
	public void setLocalCurrencyQuote(String localCurrencyQuote) {
		this.localCurrencyQuote = localCurrencyQuote;
	}
	
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}
	
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	
	public BigDecimal getTransactionForeignAmount() {
		return transactionForeignAmount;
	}
	public void setTransactionForeignAmount(BigDecimal transactionForeignAmount) {
		this.transactionForeignAmount = transactionForeignAmount;
	}
		
}
