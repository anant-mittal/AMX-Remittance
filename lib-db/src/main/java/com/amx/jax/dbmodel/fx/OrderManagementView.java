package com.amx.jax.dbmodel.fx;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_ORDER_MANAGEMENT")
public class OrderManagementView {
	
	
	@Id
	@Column(name = "DOCUMENT_NO")
	BigDecimal DocumentNo;
	
	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	BigDecimal collectionDocFinanceYear;
	
	@Column(name = "COLLECTION_DOCUMENT_NO")
	BigDecimal collectionDocumentNo;
	
	@Column(name = "CUSTOMER_MOBILE")
	String customerMobile;
	
	@Column(name = "DELIVERY_DATE")
	Date deliveryDate;
	
	@Column(name = "DELIVERY_TIME")
	String deliveryTime;
	
	@Column(name = "DRIVER_EMPLOYEE_ID")
	BigDecimal driverEmployeId;
	
	@Column(name = "FOREIGN_CURRENCY_ID")
	BigDecimal foreignCurrencyId;
	
	@Column(name = "FOREIGN_CURRENCY_QUOTE")
	String foreignCurrencyQuote;
	
	@Column(name = "FOREIGN_TRNX_AMOUNT")
	BigDecimal foreignTrnxAmount;
	
	@Column(name = "SOURCE_ID")
	BigDecimal sourceId;
	
	@Column(name = "SOURCE_DESC")
	String sourceDesc;
	
	@Column(name = "PURPOSE_ID")
	BigDecimal purposeId;
	
	@Column(name = "PURPOSE_DESC")
	String purposeDesc;
	
	@Column(name = "DENOMINATION_TYPE")
	String denominationType;
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	BigDecimal applicationCountryId;
	
	@Column(name = "INVENTORY_ID")
	String inventoryId;
	
	@Column(name = "AREA_CODE")
	BigDecimal areaCode;
	
	@Column(name = "DELIVERY_DET_SEQ_ID")
	BigDecimal deliveryDetailsId;
	
	@Column(name = "RECEIPT_PAYMENT_ID")
	BigDecimal receiptPaymentId;
	
	@Column(name = "LOCAL_NET_AMOUNT")
	BigDecimal localNetAmount;
	
	@Column(name = "TRANSACTION_ACTUAL_RATE")
	BigDecimal transactionActualRate;
	
	@Column(name = "CUSTOMER_NAME")
	String customerName;
	
	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;
	
	@Column(name = "ORDER_STATUS")
	String orderStatus;
	
	@Column(name = "ORDER_STATUS_DESC")
	String orderStatusDesc;
	
	@Column(name = "DRIVER_EMPLOYEE_NAME")
	String driverEmployeeName;
	
	@Column(name = "ORDER_LOCK")
	Date orderLock;
	
	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;
	
	@Column(name = "FROM_BRANCH_ID")
	BigDecimal fromBranchId;
	
	@Column(name = "TO_BRANCH_ID")
	BigDecimal toBranchId;
	
	@Column(name = "RECPAY_COUNTRY_BRANCH_ID")
	BigDecimal recPayCountryBranchId;
	
	@Column(name = "RECPAY_BRANCH_ID")
	BigDecimal recPayBranchId;
	
	public BigDecimal getCollectionDocFinanceYear() {
		return collectionDocFinanceYear;
	}
	public void setCollectionDocFinanceYear(BigDecimal collectionDocFinanceYear) {
		this.collectionDocFinanceYear = collectionDocFinanceYear;
	}
	
	public BigDecimal getCollectionDocumentNo() {
		return collectionDocumentNo;
	}
	public void setCollectionDocumentNo(BigDecimal collectionDocumentNo) {
		this.collectionDocumentNo = collectionDocumentNo;
	}
	
	public BigDecimal getDocumentNo() {
		return DocumentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		DocumentNo = documentNo;
	}
	
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
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
	
	public BigDecimal getDriverEmployeId() {
		return driverEmployeId;
	}
	public void setDriverEmployeId(BigDecimal driverEmployeId) {
		this.driverEmployeId = driverEmployeId;
	}
	
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
	
	public BigDecimal getForeignTrnxAmount() {
		return foreignTrnxAmount;
	}
	public void setForeignTrnxAmount(BigDecimal foreignTrnxAmount) {
		this.foreignTrnxAmount = foreignTrnxAmount;
	}
	
	public BigDecimal getSourceId() {
		return sourceId;
	}
	public void setSourceId(BigDecimal sourceId) {
		this.sourceId = sourceId;
	}
	
	public BigDecimal getPurposeId() {
		return purposeId;
	}
	public void setPurposeId(BigDecimal purposeId) {
		this.purposeId = purposeId;
	}
	
	public String getDenominationType() {
		return denominationType;
	}
	public void setDenominationType(String denominationType) {
		this.denominationType = denominationType;
	}
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}
	
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	
	public String getPurposeDesc() {
		return purposeDesc;
	}
	public void setPurposeDesc(String purposeDesc) {
		this.purposeDesc = purposeDesc;
	}
	
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	public BigDecimal getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(BigDecimal areaCode) {
		this.areaCode = areaCode;
	}
	
	public BigDecimal getDeliveryDetailsId() {
		return deliveryDetailsId;
	}
	public void setDeliveryDetailsId(BigDecimal deliveryDetailsId) {
		this.deliveryDetailsId = deliveryDetailsId;
	}
	
	public BigDecimal getReceiptPaymentId() {
		return receiptPaymentId;
	}
	public void setReceiptPaymentId(BigDecimal receiptPaymentId) {
		this.receiptPaymentId = receiptPaymentId;
	}
	
	public BigDecimal getLocalNetAmount() {
		return localNetAmount;
	}
	public void setLocalNetAmount(BigDecimal localNetAmount) {
		this.localNetAmount = localNetAmount;
	}
	
	public BigDecimal getTransactionActualRate() {
		return transactionActualRate;
	}
	public void setTransactionActualRate(BigDecimal transactionActualRate) {
		this.transactionActualRate = transactionActualRate;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	
	public String getDriverEmployeeName() {
		return driverEmployeeName;
	}
	public void setDriverEmployeeName(String driverEmployeeName) {
		this.driverEmployeeName = driverEmployeeName;
	}
	
	public Date getOrderLock() {
		return orderLock;
	}
	public void setOrderLock(Date orderLock) {
		this.orderLock = orderLock;
	}
	
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	
	public BigDecimal getFromBranchId() {
		return fromBranchId;
	}
	public void setFromBranchId(BigDecimal fromBranchId) {
		this.fromBranchId = fromBranchId;
	}
	
	public BigDecimal getToBranchId() {
		return toBranchId;
	}
	public void setToBranchId(BigDecimal toBranchId) {
		this.toBranchId = toBranchId;
	}
	
	public BigDecimal getRecPayCountryBranchId() {
		return recPayCountryBranchId;
	}
	public void setRecPayCountryBranchId(BigDecimal recPayCountryBranchId) {
		this.recPayCountryBranchId = recPayCountryBranchId;
	}
	
	public BigDecimal getRecPayBranchId() {
		return recPayBranchId;
	}
	public void setRecPayBranchId(BigDecimal recPayBranchId) {
		this.recPayBranchId = recPayBranchId;
	}
									
}
