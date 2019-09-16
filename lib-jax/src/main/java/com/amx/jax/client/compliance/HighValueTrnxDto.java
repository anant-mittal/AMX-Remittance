package com.amx.jax.client.compliance;

import java.math.BigDecimal;

public class HighValueTrnxDto {

	String customerFullName;
	String customerReference;
	BigDecimal localTransactionAmount;
	BigDecimal foreignTransactionAmount;
	String foreignCurrencyQuote;
	String bankCode;
	String bankBranch;
	BigDecimal documentNo;
	BigDecimal documentFinancialYear;
	BigDecimal transactionId;
	BigDecimal collectionDocNo;
	BigDecimal collectionDocYear;
	BigDecimal collectionDocCode;
	BigDecimal customerId;

	public String getCustomerFullName() {
		return customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public BigDecimal getLocalTransactionAmount() {
		return localTransactionAmount;
	}

	public void setLocalTransactionAmount(BigDecimal localTransactionAmount) {
		this.localTransactionAmount = localTransactionAmount;
	}

	public BigDecimal getForeignTransactionAmount() {
		return foreignTransactionAmount;
	}

	public void setForeignTransactionAmount(BigDecimal foreignTransactionAmount) {
		this.foreignTransactionAmount = foreignTransactionAmount;
	}

	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}

	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public BigDecimal getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	public BigDecimal getDocumentFinancialYear() {
		return documentFinancialYear;
	}

	public void setDocumentFinancialYear(BigDecimal documentFinancialYear) {
		this.documentFinancialYear = documentFinancialYear;
	}

	public BigDecimal getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigDecimal transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getCollectionDocNo() {
		return collectionDocNo;
	}

	public void setCollectionDocNo(BigDecimal collectionDocNo) {
		this.collectionDocNo = collectionDocNo;
	}

	public BigDecimal getCollectionDocYear() {
		return collectionDocYear;
	}

	public void setCollectionDocYear(BigDecimal collectionDocYear) {
		this.collectionDocYear = collectionDocYear;
	}

	public BigDecimal getCollectionDocCode() {
		return collectionDocCode;
	}

	public void setCollectionDocCode(BigDecimal collectionDocCode) {
		this.collectionDocCode = collectionDocCode;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
