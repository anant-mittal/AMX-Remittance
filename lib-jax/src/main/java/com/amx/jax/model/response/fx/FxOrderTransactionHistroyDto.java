package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.List;

public class FxOrderTransactionHistroyDto {
	private BigDecimal idno;
	private BigDecimal customerReference;

	private BigDecimal documentNumber;
	private String documentDate;
	private BigDecimal documentFinanceYear;
	private String transactionReferenceNo;
	private String foreignCurrencyCode;
	private BigDecimal exchangeRate;
	private BigDecimal foreignTransactionAmount;
	private String currencyQuoteName;
	private String transactionStatusDesc;
	private String transactionTypeDesc;
	private BigDecimal collectionDocumentNo;
	private BigDecimal localCurrencyAmout;

	private BigDecimal collectionDocumentCode;

	private BigDecimal collectionDocumentFinYear;

	private String branchDesc;

	private BigDecimal customerId;

	private BigDecimal localTrnxAmount;

	private BigDecimal sourceOfIncomeId;

	private String purposeOfTrnx;

	private BigDecimal documentCode;

	private BigDecimal deliveryDetSeqId;

	private BigDecimal pagDetSeqId;

	private String orderStatus;
	private String orderStatusCode;

	private String deliveryDate;

	private String deliveryTime;

	private String customerName;

	private BigDecimal deliveryCharges;

	private String createdDate;
	private String sourceOfIncomeDesc;
	private String travelCountryName;
	private String travelDateRange;
	private String localCurrQuoteName;
	private String multiAmount;
	private String multiExchangeRate;
	private String deliveryAddress;
	private String inventoryId;
	
	private String otpTokenPrefix;
	private String otpTokenCustomer;
	
	private String driverName;
	private String employeeName;
	private String phoneNumber;
	public String getMutipleInventoryId() {
		return mutipleInventoryId;
	}

	public void setMutipleInventoryId(String mutipleInventoryId) {
		this.mutipleInventoryId = mutipleInventoryId;
	}

	String mutipleInventoryId;
	
	List<FcSaleCurrencyAmountModel> mutipleFcAmount;

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

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String documentDate) {
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

	public void setCollectionDocumentFinYear(
			BigDecimal collectionDocumentFinYear) {
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

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
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

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getSourceOfIncomeDesc() {
		return sourceOfIncomeDesc;
	}

	public void setSourceOfIncomeDesc(String sourceOfIncomeDesc) {
		this.sourceOfIncomeDesc = sourceOfIncomeDesc;
	}

	public String getTravelCountryName() {
		return travelCountryName;
	}

	public void setTravelCountryName(String travelCountryName) {
		this.travelCountryName = travelCountryName;
	}

	public String getTravelDateRange() {
		return travelDateRange;
	}

	public void setTravelDateRange(String travelDateRange) {
		this.travelDateRange = travelDateRange;
	}

	public String getLocalCurrQuoteName() {
		return localCurrQuoteName;
	}

	public void setLocalCurrQuoteName(String localCurrQuoteName) {
		this.localCurrQuoteName = localCurrQuoteName;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getTransactionReferenceNo() {
		return transactionReferenceNo;
	}

	public void setTransactionReferenceNo(String transactionReferenceNo) {
		this.transactionReferenceNo = transactionReferenceNo;
	}

	public String getMultiAmount() {
		return multiAmount;
	}

	public void setMultiAmount(String multiAmount) {
		this.multiAmount = multiAmount;
	}

	public String getMultiExchangeRate() {
		return multiExchangeRate;
	}

	public void setMultiExchangeRate(String multiExchangeRate) {
		this.multiExchangeRate = multiExchangeRate;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getOrderStatusCode() {
		return orderStatusCode;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getOtpTokenPrefix() {
		return otpTokenPrefix;
	}

	public void setOtpTokenPrefix(String otpTokenPrefix) {
		this.otpTokenPrefix = otpTokenPrefix;
	}

	public String getOtpTokenCustomer() {
		return otpTokenCustomer;
	}

	public void setOtpTokenCustomer(String otpTokenCustomer) {
		this.otpTokenCustomer = otpTokenCustomer;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BigDecimal getLocalCurrencyAmout() {
		return localCurrencyAmout;
	}

	public void setLocalCurrencyAmout(BigDecimal localCurrencyAmout) {
		this.localCurrencyAmout = localCurrencyAmout;
	}

	public List<FcSaleCurrencyAmountModel> getMutipleFcAmount() {
		return mutipleFcAmount;
	}

	public void setMutipleFcAmount(List<FcSaleCurrencyAmountModel> mutipleFcAmount) {
		this.mutipleFcAmount = mutipleFcAmount;
	}
	
	
	
	

	
}
