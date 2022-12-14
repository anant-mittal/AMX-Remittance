package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.jax.model.AbstractModel;

public class FxOrderReportResponseDto extends AbstractModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String civilId;
	private String idExpiryDate;
	private String customerName;
	private BigDecimal customerReferenceId;
	private String localCurrency;
	private String insurence1;
	private String insurence2;
	private String location;
	private String phoneNumber;
	private String receiptNo;
	private String loyalityPointExpiring;
	private BigDecimal noOfTransaction;
	private String deliveryDate;
	private String deliveryTime;
	private BigDecimal deliveryCharges;
	private String paymentMode;
	private String amountPayable;
	private String collectionMode;
	private String approvalNo;
	private String KnetReceiptDateTime ="";
	private Boolean knetBooleanCheck=false;
	private Boolean drawLine=false;
	private String netAmount;
	private String paidAmount;
	private String refundedAmount;
	private BigDecimal collectAmount;
	private BigDecimal loyaltyPoints;
	private String email;
	private String engCompanyInfo;
	private String arabicCompanyInfo;
	

	List<FxOrderTransactionHistroyDto> fxOrderTrnxList = new ArrayList<>();
	FxDeliveryReportDetailDto  deliveryDetailReport = new FxDeliveryReportDetailDto();
	ShippingAddressDto shippingAddressdto = new ShippingAddressDto();
	PaygDetailsDto payg = new PaygDetailsDto();
	

	public List<FxOrderTransactionHistroyDto> getFxOrderTrnxList() {
		return fxOrderTrnxList;
	}
	public void setFxOrderTrnxList(List<FxOrderTransactionHistroyDto> fxOrderTrnxList) {
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
	public void setDeliveryDetailReport(FxDeliveryReportDetailDto deliveryDetailReport) {
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
	
	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}
	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
	
	public String getEngCompanyInfo() {
		return engCompanyInfo;
	}
	public void setEngCompanyInfo(String engCompanyInfo) {
		this.engCompanyInfo = engCompanyInfo;
	}
	
	public String getArabicCompanyInfo() {
		return arabicCompanyInfo;
	}
	public void setArabicCompanyInfo(String arabicCompanyInfo) {
		this.arabicCompanyInfo = arabicCompanyInfo;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	public String getAmountPayable() {
		return amountPayable;
	}
	public void setAmountPayable(String amountPayable) {
		this.amountPayable = amountPayable;
	}
	
	public String getCollectionMode() {
		return collectionMode;
	}
	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}
	
	public String getApprovalNo() {
		return approvalNo;
	}
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public String getKnetReceiptDateTime() {
		return KnetReceiptDateTime;
	}
	public void setKnetReceiptDateTime(String knetReceiptDateTime) {
		KnetReceiptDateTime = knetReceiptDateTime;
	}

	public Boolean getKnetBooleanCheck() {
		return knetBooleanCheck;
	}
	public void setKnetBooleanCheck(Boolean knetBooleanCheck) {
		this.knetBooleanCheck = knetBooleanCheck;
	}
	
	public Boolean getDrawLine() {
		return drawLine;
	}
	public void setDrawLine(Boolean drawLine) {
		this.drawLine = drawLine;
	}
	
	public String getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public String getRefundedAmount() {
		return refundedAmount;
	}
	public void setRefundedAmount(String refundedAmount) {
		this.refundedAmount = refundedAmount;
	}
	
	public BigDecimal getCollectAmount() {
		return collectAmount;
	}
	public void setCollectAmount(BigDecimal collectAmount) {
		this.collectAmount = collectAmount;
	}
	
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getCustomerReferenceId() {
		return customerReferenceId;
	}
	public void setCustomerReferenceId(BigDecimal customerReferenceId) {
		this.customerReferenceId = customerReferenceId;
	}

}
