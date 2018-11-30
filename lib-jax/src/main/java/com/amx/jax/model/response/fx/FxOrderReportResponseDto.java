package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.jax.payg.PaymentResponseDto;

public class FxOrderReportResponseDto {
	
	 List<FxOrderTransactionHistroyDto> fxOrderTrnxList = new ArrayList<>();
	 FxDeliveryReportDetailDto  deliveryDetailReport = new FxDeliveryReportDetailDto();
	 ShippingAddressDto shippingAddressdto =new ShippingAddressDto();
	 PaygDetailsDto payg = new PaygDetailsDto();
	 private String civilId;
     private String idExpiryDate;
     String customerName;
     String localCurrency;
     private String insurence1;
 	 private String insurence2;
 	 private String location;
 	 private String phoneNumber;
 	 private String receiptNo;
 	 private String loyalityPointExpiring;
 	 private BigDecimal noOfTransaction;
     
     
     
	
	
	public List<FxOrderTransactionHistroyDto> getFxOrderTrnxList() {
		return fxOrderTrnxList;
	}
	public void setFxOrderTrnxList(
			List<FxOrderTransactionHistroyDto> fxOrderTrnxList) {
		this.fxOrderTrnxList = fxOrderTrnxList;
	}
	public PaygDetailsDto getPayg() {
		return payg;
	}
	public void setPayg(PaygDetailsDto payg) {
		this.payg = payg;
	}
	
	public ShippingAddressDto getShippingAddressdto() {
		return shippingAddressdto;
	}
	public void setShippingAddressdto(ShippingAddressDto shippingAddressdto) {
		this.shippingAddressdto = shippingAddressdto;
	}
	public FxDeliveryReportDetailDto getDeliveryDetailReport() {
		return deliveryDetailReport;
	}
	public void setDeliveryDetailReport(
			FxDeliveryReportDetailDto deliveryDetailReport) {
		this.deliveryDetailReport = deliveryDetailReport;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getIdExpiryDate() {
		return idExpiryDate;
	}
	public void setIdExpiryDate(String idExpiryDate) {
		this.idExpiryDate = idExpiryDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLocalCurrency() {
		return localCurrency;
	}
	public void setLocalCurrency(String localCurrency) {
		this.localCurrency = localCurrency;
	}
	public String getInsurence1() {
		return insurence1;
	}
	public void setInsurence1(String insurence1) {
		this.insurence1 = insurence1;
	}
	public String getInsurence2() {
		return insurence2;
	}
	public void setInsurence2(String insurence2) {
		this.insurence2 = insurence2;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getLoyalityPointExpiring() {
		return loyalityPointExpiring;
	}
	public void setLoyalityPointExpiring(String loyalityPointExpiring) {
		this.loyalityPointExpiring = loyalityPointExpiring;
	}
	public BigDecimal getNoOfTransaction() {
		return noOfTransaction;
	}
	public void setNoOfTransaction(BigDecimal noOfTransaction) {
		this.noOfTransaction = noOfTransaction;
	}
	 
	

}
