package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

public class FxOrderDetailNotificationDto {
	String customerName;
	private String email;
	private String mobileNo;
	private BigDecimal loyaltyPoints;
	private String receiptNo;
	private String date;
	private BigDecimal netAmount;
	private String localQurrencyQuote;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
	public String getLocalQurrencyQuote() {
		return localQurrencyQuote;
	}
	public void setLocalQurrencyQuote(String localQurrencyQuote) {
		this.localQurrencyQuote = localQurrencyQuote;
	}
	

}
