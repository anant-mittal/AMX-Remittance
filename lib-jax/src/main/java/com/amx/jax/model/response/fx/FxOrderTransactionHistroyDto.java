package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.Date;

public class FxOrderTransactionHistroyDto {


	private BigDecimal idno;

	private BigDecimal customerReference;
	

	private BigDecimal documentNumber;

	private Date documentDate;
	

	private BigDecimal documentFinanceYear;
	

	private String foreignCurrencyCode;
	

	private BigDecimal foreignTransactionAmount;

	private String currencyQuoteName;

	private String transactionStatusDesc;
	

	private String transactionTypeDesc;
	

	private BigDecimal collectionDocumentNo;
		

	private BigDecimal collectionDocumentCode;
	

	private BigDecimal  collectionDocumentFinYear;
	

	private String branchDesc;
	

	private BigDecimal customerId;
	

	private BigDecimal localTrnxAmount;
	

	private BigDecimal sourceOfIncomeId;
	

	private String purposeOfTrnx;
	

	private BigDecimal documentCode;
	

	private BigDecimal deliveryDetSeqId;
	

	private BigDecimal pagDetSeqId;

	private String orderStatus;
	

	private Date deliveryDate;

	private String deliveryTime;
	
	private String customerName;

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

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getLocalTrnxAmount() {
		return localTrnxAmount;
	}

	public void setLocalTrnxAmount(BigDecimal localTrnxAmount) {
		this.localTrnxAmount = localTrnxAmount;
	}

	public BigDecimal getSourceOfIncomeId() {
		return sourceOfIncomeId;
	}

	public void setSourceOfIncomeId(BigDecimal sourceOfIncomeId) {
		this.sourceOfIncomeId = sourceOfIncomeId;
	}

	public String getPurposeOfTrnx() {
		return purposeOfTrnx;
	}

	public void setPurposeOfTrnx(String purposeOfTrnx) {
		this.purposeOfTrnx = purposeOfTrnx;
	}

	public BigDecimal getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}

	public BigDecimal getDeliveryDetSeqId() {
		return deliveryDetSeqId;
	}

	public void setDeliveryDetSeqId(BigDecimal deliveryDetSeqId) {
		this.deliveryDetSeqId = deliveryDetSeqId;
	}

	public BigDecimal getPagDetSeqId() {
		return pagDetSeqId;
	}

	public void setPagDetSeqId(BigDecimal pagDetSeqId) {
		this.pagDetSeqId = pagDetSeqId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
