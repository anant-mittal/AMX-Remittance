package com.amx.jax.model.request.fx;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FcDeliveryBranchOrderSearchRequest {

	private String orderId;
	private BigDecimal countryBranchId;
	private String civilId;
	private String orderStatus;
	
	//@JsonIgnore
	private BigDecimal customerId;
	@JsonIgnore
	private String countryBranchName;
	@JsonIgnore
	private String orderStatusCode;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Override
	public String toString() {
		return "FcDeliveryBranchOrderSearchRequest [orderId=" + orderId + ", countryBranchId=" + countryBranchId
				+ ", civilId=" + civilId + ", orderStatus=" + orderStatus + "]";
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public String getCountryBranchName() {
		return countryBranchName;
	}
	public void setCountryBranchName(String countryBranchName) {
		this.countryBranchName = countryBranchName;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	
	
	
	
	
}
