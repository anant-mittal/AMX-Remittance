package com.amx.jax.payg;

import java.math.BigDecimal;

public class PaymentResponseDto extends PayGModel {

	private static final long serialVersionUID = -7991187321598015743L;

	public static final String PAYMENT_CAPTURE_URL = "/callback/payg/payment/capture";

	BigDecimal customerId;
	BigDecimal companyId;

	String resultCode;
	String transactionId;
	String tranData;
	String auth_appNo;
	String referenceId;
	String postDate;

	String userName;
	String product;
	String payId;

	/**
	 * Local Payment Reference id, is used to track payment in local application
	 */
	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	String amount;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getAuth_appNo() {
		return auth_appNo;
	}

	public void setAuth_appNo(String auth_appNo) {
		this.auth_appNo = auth_appNo;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getTranData() {
		return tranData;
	}

	public void setTranData(String tranData) {
		this.tranData = tranData;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "PaymentResponseDto [customerId=" + customerId + ", companyId=" + companyId + ", resultCode="
				+ resultCode + ", transactionId=" + transactionId + ", tranData=" + tranData + ", auth_appNo="
				+ auth_appNo + ", referenceId=" + referenceId + ", postDate=" + postDate + ", userName=" + userName
				+ ", product=" + product + "]";
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
