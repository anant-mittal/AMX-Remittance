package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

public class FxDeliveryDetailDto {

	String customerName;
	String mobile;
	String deliveryTimeSlot;
	ShippingAddressDto address;
	String transactionRefId;
	String inventoryId;//
	String deliveryRemark;
	String orderStatus;
	BigDecimal deliveryDetailSeqId;
	String otpTokenPrefix;

	public String getOtpTokenPrefix() {
		return otpTokenPrefix;
	}

	public void setOtpTokenPrefix(String otpTokenPrefix) {
		this.otpTokenPrefix = otpTokenPrefix;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public ShippingAddressDto getAddress() {
		return address;
	}

	public void setAddress(ShippingAddressDto address) {
		this.address = address;
	}

	public String getTransactionRefId() {
		return transactionRefId;
	}

	public void setTransactionRefId(String transactionRefId) {
		this.transactionRefId = transactionRefId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}

	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}

	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}


}
