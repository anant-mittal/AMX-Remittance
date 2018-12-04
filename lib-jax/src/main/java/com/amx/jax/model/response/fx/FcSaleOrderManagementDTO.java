package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.amx.jax.model.AbstractModel;

public class FcSaleOrderManagementDTO extends AbstractModel {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	BigDecimal DocumentNo;
	BigDecimal collectionDocFinanceYear;
	BigDecimal collectionDocumentNo;
	String customerMobile;
	String deliveryDate;
	String deliveryTime;
	BigDecimal driverEmployeId;
	BigDecimal foreignCurrencyId;
	String foreignCurrencyQuote;
	BigDecimal foreignTrnxAmount;
	BigDecimal sourceId;
	String sourceDesc;
	BigDecimal purposeId;
	String purposeDesc;
	String denominationType;
	BigDecimal applicationCountryId;
	List<FcSaleCurrencyAmountModel> mutipleFcAmount;
	String mutipleInventoryId;
	String inventoryId;
	BigDecimal areaCode;
	BigDecimal deliveryDetailsId;
	BigDecimal receiptPaymentId;
	BigDecimal localNetAmount;
	BigDecimal transactionActualRate;
	String customerName;
	BigDecimal customerId;
	String orderStatus;
	String orderStatusDesc;
	String driverEmployeName;
	BigDecimal localActualRate;
	String orderLock;
	BigDecimal employeeId;
	Boolean orderEmployee;
	
	public BigDecimal getDocumentNo() {
		return DocumentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		DocumentNo = documentNo;
	}
	
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
	
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
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
	
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
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
	
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	
	public BigDecimal getPurposeId() {
		return purposeId;
	}
	public void setPurposeId(BigDecimal purposeId) {
		this.purposeId = purposeId;
	}
	
	public String getPurposeDesc() {
		return purposeDesc;
	}
	public void setPurposeDesc(String purposeDesc) {
		this.purposeDesc = purposeDesc;
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
	
	public List<FcSaleCurrencyAmountModel> getMutipleFcAmount() {
		return mutipleFcAmount;
	}
	public void setMutipleFcAmount(List<FcSaleCurrencyAmountModel> mutipleFcAmount) {
		this.mutipleFcAmount = mutipleFcAmount;
	}
	
	public String getMutipleInventoryId() {
		return mutipleInventoryId;
	}
	public void setMutipleInventoryId(String mutipleInventoryId) {
		this.mutipleInventoryId = mutipleInventoryId;
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
	
	public String getDriverEmployeName() {
		return driverEmployeName;
	}
	public void setDriverEmployeName(String driverEmployeName) {
		this.driverEmployeName = driverEmployeName;
	}
	
	public BigDecimal getLocalActualRate() {
		return localActualRate;
	}
	public void setLocalActualRate(BigDecimal localActualRate) {
		this.localActualRate = localActualRate;
	}
	
	public String getOrderLock() {
		return orderLock;
	}
	public void setOrderLock(String orderLock) {
		this.orderLock = orderLock;
	}
	
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	
	public Boolean getOrderEmployee() {
		return orderEmployee;
	}
	public void setOrderEmployee(Boolean orderEmployee) {
		this.orderEmployee = orderEmployee;
	}

}
