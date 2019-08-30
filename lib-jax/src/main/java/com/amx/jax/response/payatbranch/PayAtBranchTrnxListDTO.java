package com.amx.jax.response.payatbranch;

import java.math.BigDecimal;
import java.util.Date;

public class PayAtBranchTrnxListDTO {
	public BigDecimal transactionId;
	public Date confirmDate;
	public String status;
	public String accountNo;
	public String bank;
	public BigDecimal amount;
	public String identityInt;
	public BigDecimal exchangeRate;
	public BigDecimal foreignAmount;
	public String customerName;
	public Date documentDate;
	public String transactionReferenceNumber;
	public BigDecimal accountId;
	public String applIsActive;
	public BigDecimal transactionDocumentNo;
	public BigDecimal identityTypeId;
	public String foreignCurrencyDescription;
	public String statusDesc;

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getForeignCurrencyDescription() {
		return foreignCurrencyDescription;
	}

	public void setForeignCurrencyDescription(String foreignCurrencyDescription) {
		this.foreignCurrencyDescription = foreignCurrencyDescription;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getApplIsActive() {
		return applIsActive;
	}

	public void setApplIsActive(String applIsActive) {
		this.applIsActive = applIsActive;
	}

	
	public BigDecimal getTransactionDocumentNo() {
		return transactionDocumentNo;
	}

	public void setTransactionDocumentNo(BigDecimal transactionDocumentNo) {
		this.transactionDocumentNo = transactionDocumentNo;
	}

	public String getTransactionReferenceNumber() {
		return transactionReferenceNumber;
	}

	public void setTransactionReferenceNumber(String transactionReferenceNumber) {
		this.transactionReferenceNumber = transactionReferenceNumber;
	}

	public BigDecimal getAccountId() {
		return accountId;
	}

	public void setAccountId(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(BigDecimal transactionId) {
		this.transactionId = transactionId;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}

