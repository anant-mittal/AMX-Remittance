package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionHistroyDTO {
	private BigDecimal idno;

	private BigDecimal customerReference;

	private BigDecimal documentNumber;

	private String beneficaryAccountNumber;

	private String beneficaryBankName;

	private String beneficaryBranchName;

	private String beneficaryName;

	private String beneficaryCorespondingBankName;

	private Date documentDate;

	private BigDecimal documentFinanceYear;

	private String foreignCurrencyCode;

	private BigDecimal foreignTransactionAmount;

	private String currencyQuoteName;

	private String serviceDescription;

	private String transactionStatusDesc;

	private String transactionTypeDesc;

	private BigDecimal collectionDocumentNo;

	private BigDecimal collectionDocumentCode;

	private BigDecimal collectionDocumentFinYear;

	private String branchDesc;
	
	private String trnxIdNumber;
	
	private BigDecimal customerId;

	public BigDecimal getIdno() {
		return idno;
	}

	public void setIdno(BigDecimal idno) {
		this.idno = idno;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getBeneficaryAccountNumber() {
		return beneficaryAccountNumber;
	}

	public void setBeneficaryAccountNumber(String beneficaryAccountNumber) {
		this.beneficaryAccountNumber = beneficaryAccountNumber;
	}

	public String getBeneficaryBankName() {
		return beneficaryBankName;
	}

	public void setBeneficaryBankName(String beneficaryBankName) {
		this.beneficaryBankName = beneficaryBankName;
	}

	public String getBeneficaryBranchName() {
		return beneficaryBranchName;
	}

	public void setBeneficaryBranchName(String beneficaryBranchName) {
		this.beneficaryBranchName = beneficaryBranchName;
	}

	public String getBeneficaryName() {
		return beneficaryName;
	}

	public void setBeneficaryName(String beneficaryName) {
		this.beneficaryName = beneficaryName;
	}

	public String getBeneficaryCorespondingBankName() {
		return beneficaryCorespondingBankName;
	}

	public void setBeneficaryCorespondingBankName(String beneficaryCorespondingBankName) {
		this.beneficaryCorespondingBankName = beneficaryCorespondingBankName;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}

	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	public String getForeignCurrencyCode() {
		return foreignCurrencyCode;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	public BigDecimal getForeignTransactionAmount() {
		return foreignTransactionAmount;
	}

	public void setForeignTransactionAmount(BigDecimal foreignTransactionAmount) {
		this.foreignTransactionAmount = foreignTransactionAmount;
	}

	public String getCurrencyQuoteName() {
		return currencyQuoteName;
	}

	public void setCurrencyQuoteName(String currencyQuoteName) {
		this.currencyQuoteName = currencyQuoteName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getTransactionStatusDesc() {
		return transactionStatusDesc;
	}

	public void setTransactionStatusDesc(String transactionStatusDesc) {
		this.transactionStatusDesc = transactionStatusDesc;
	}

	public String getTransactionTypeDesc() {
		return transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc) {
		this.transactionTypeDesc = transactionTypeDesc;
	}

	public BigDecimal getCollectionDocumentNo() {
		return collectionDocumentNo;
	}

	public void setCollectionDocumentNo(BigDecimal collectionDocumentNo) {
		this.collectionDocumentNo = collectionDocumentNo;
	}

	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}

	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}

	public BigDecimal getCollectionDocumentFinYear() {
		return collectionDocumentFinYear;
	}

	public void setCollectionDocumentFinYear(BigDecimal collectionDocumentFinYear) {
		this.collectionDocumentFinYear = collectionDocumentFinYear;
	}

	public String getBranchDesc() {
		return branchDesc;
	}

	public void setBranchDesc(String branchDesc) {
		this.branchDesc = branchDesc;
	}

	public String getTrnxIdNumber() {
		return trnxIdNumber;
	}

	public void setTrnxIdNumber(String trnxIdNumber) {
		this.trnxIdNumber = trnxIdNumber;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
