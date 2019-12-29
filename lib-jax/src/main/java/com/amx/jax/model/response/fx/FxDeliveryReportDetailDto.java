package com.amx.jax.model.response.fx;

public class FxDeliveryReportDetailDto {

	
	String customerName;
	String mobile;
	String deliveryTimeSlot;
	ShippingAddressDto address;
	String transactionRefId;
	String inventoryId;//
	String deliveryRemark;
	String orderStatus;
	String deliveryCharges;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}
	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
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
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
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
	public String getDeliveryCharges() {
		return deliveryCharges;
	}
	public void setDeliveryCharges(String deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
	
}
