package com.amx.amxlib.model.notification;

import java.math.BigDecimal;

public class RemittanceTransactionFailureAlertModel {

	String customerReference;
	String customerName;
	BigDecimal customerContact;
	String beneName;
	BigDecimal transactionAmount;
	String currencyQuoteName;
	String beneBankName;
	String beneBankBranchName;
	BigDecimal beneAccountNumber;
	String selectedProduct;

	String exceptionMessage;

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(BigDecimal customerContact) {
		this.customerContact = customerContact;
	}

	public String getBeneName() {
		return beneName;
	}

	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCurrencyQuoteName() {
		return currencyQuoteName;
	}

	public void setCurrencyQuoteName(String currencyQuoteName) {
		this.currencyQuoteName = currencyQuoteName;
	}

	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	public String getBeneBankBranchName() {
		return beneBankBranchName;
	}

	public void setBeneBankBranchName(String beneBankBranchName) {
		this.beneBankBranchName = beneBankBranchName;
	}

	public BigDecimal getBeneAccountNumber() {
		return beneAccountNumber;
	}

	public void setBeneAccountNumber(BigDecimal beneAccountNumber) {
		this.beneAccountNumber = beneAccountNumber;
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
