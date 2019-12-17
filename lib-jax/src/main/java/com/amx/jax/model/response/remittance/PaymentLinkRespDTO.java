package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.List;

public class PaymentLinkRespDTO extends RemittanceApplicationResponseModel {

	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal id;
	private String verificationCode;
	private String applicationIds;
	private String linkStatus;

	private String curQutoe;
	private BigDecimal netAmount;
	private String requestData;

	private BigDecimal totalTrnxAmount;
	private BigDecimal totalCommissionAmt;

	private PaymentLinkRespStatus paymentLinkRespStatus;

	private List<CustomerShoppingCartDto> shoppingCartDetails;

	public List<CustomerShoppingCartDto> getShoppingCartDetails() {
		return shoppingCartDetails;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(String applicationIds) {
		this.applicationIds = applicationIds;
	}
	
	public String getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(String linkStatus) {
		this.linkStatus = linkStatus;
	}

	public String getCurQutoe() {
		return curQutoe;
	}

	public void setCurQutoe(String curQutoe) {
		this.curQutoe = curQutoe;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getTotalTrnxAmount() {
		return totalTrnxAmount;
	}

	public void setTotalTrnxAmount(BigDecimal totalTrnxAmount) {
		this.totalTrnxAmount = totalTrnxAmount;
	}

	public BigDecimal getTotalCommissionAmt() {
		return totalCommissionAmt;
	}

	public void setTotalCommissionAmt(BigDecimal totalCommissionAmt) {
		this.totalCommissionAmt = totalCommissionAmt;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public PaymentLinkRespStatus getPaymentLinkRespStatus() {
		return paymentLinkRespStatus;
	}

	public void setPaymentLinkRespStatus(PaymentLinkRespStatus paymentLinkRespStatus) {
		this.paymentLinkRespStatus = paymentLinkRespStatus;
	}

	public void setShoppingCartDetails(List<CustomerShoppingCartDto> shoppingCartDetails) {
		this.shoppingCartDetails = shoppingCartDetails;
	}

	@Override
	public String toString() {
		return "PaymentLinkRespDTO [id=" + id + ", verificationCode=" + verificationCode + ", applicationIds="
				+ applicationIds + ", linkStatus=" + linkStatus + ", curQutoe=" + curQutoe + ", netAmount=" + netAmount
				+ ", requestData=" + requestData + ", totalTrnxAmount=" + totalTrnxAmount + ", totalCommissionAmt="
				+ totalCommissionAmt + ", paymentLinkRespStatus=" + paymentLinkRespStatus + ", shoppingCartDetails="
				+ shoppingCartDetails + ", getPgCode()=" + getPgCode() + ", getModelType()=" + getModelType()
				+ ", getRemittanceAppId()=" + getRemittanceAppId() + ", getNetPayableAmount()=" + getNetPayableAmount()
				+ ", getDocumentIdForPayment()=" + getDocumentIdForPayment() + ", getMerchantTrackId()="
				+ getMerchantTrackId() + ", getDocumentFinancialYear()=" + getDocumentFinancialYear() + "]";
	}
	
	
	
}
