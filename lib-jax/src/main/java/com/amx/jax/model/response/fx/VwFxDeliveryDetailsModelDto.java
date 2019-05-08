package com.amx.jax.model.response.fx;


import java.math.BigDecimal;
import java.util.Date;

public class VwFxDeliveryDetailsModelDto {
	
	BigDecimal deleviryDelSeqId;
	BigDecimal collectionDocNo;
	BigDecimal collectionDocFinYear;
	String customerName;
	BigDecimal customerId;
	Date deliveryDate;
	BigDecimal deliveryRemarkId;
	String deliveryTimeSlot;
	BigDecimal driverEmployeeId;
	BigDecimal shippingAddressId;
	String orderStatus;
	String inventoryId;
	String otpTokenPrefix;
	BigDecimal toBranchId;
	BigDecimal fromBranchId;
	String isActive;
	
	public BigDecimal getDeleviryDelSeqId() {
		return deleviryDelSeqId;
	}
	public void setDeleviryDelSeqId(BigDecimal deleviryDelSeqId) {
		this.deleviryDelSeqId = deleviryDelSeqId;
	}
	public BigDecimal getCollectionDocNo() {
		return collectionDocNo;
	}
	public void setCollectionDocNo(BigDecimal collectionDocNo) {
		this.collectionDocNo = collectionDocNo;
	}
	public BigDecimal getCollectionDocFinYear() {
		return collectionDocFinYear;
	}
	public void setCollectionDocFinYear(BigDecimal collectionDocFinYear) {
		this.collectionDocFinYear = collectionDocFinYear;
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
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public BigDecimal getDeliveryRemarkId() {
		return deliveryRemarkId;
	}
	public void setDeliveryRemarkId(BigDecimal deliveryRemarkId) {
		this.deliveryRemarkId = deliveryRemarkId;
	}
	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}
	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
	}
	public BigDecimal getDriverEmployeeId() {
		return driverEmployeeId;
	}
	public void setDriverEmployeeId(BigDecimal driverEmployeeId) {
		this.driverEmployeeId = driverEmployeeId;
	}
	public BigDecimal getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(BigDecimal shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
	public BigDecimal getToBranchId() {
		return toBranchId;
	}
	public void setToBranchId(BigDecimal toBranchId) {
		this.toBranchId = toBranchId;
	}
	public BigDecimal getFromBranchId() {
		return fromBranchId;
	}
	public void setFromBranchId(BigDecimal fromBranchId) {
		this.fromBranchId = fromBranchId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
	}
