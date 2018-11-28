package com.amx.jax.payg;

import java.math.BigDecimal;

import com.amx.jax.api.ARespModel;
import com.amx.jax.payg.PayGCodes.CodeCategory;

public class PaymentResponseDto extends ARespModel {

	private static final long serialVersionUID = -7991187321598015743L;

	public static final String PAYMENT_CAPTURE_URL = "/callback/payg/payment/capture";

	String paymentId;
	String errorText;
	String udf1;
	String udf2;
	String udf3;
	String udf4;
	String udf5;
	String resultCode;
	String auth_appNo;
	String trackId;
	String referenceId;
	String transactionId;
	String postDate;
	String tranData;
	BigDecimal customerId;
	BigDecimal applicationCountryId;
	BigDecimal companyId;
	String userName;
	BigDecimal collectionFinanceYear;
	BigDecimal collectionDocumentNumber;
	BigDecimal collectionDocumentCode;
	String error = null;
	CodeCategory errorCategory = null;

	String product;

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getUdf1() {
		return udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public String getUdf2() {
		return udf2;
	}

	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	public String getUdf3() {
		return udf3;
	}

	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}

	public String getUdf4() {
		return udf4;
	}

	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}

	public String getUdf5() {
		return udf5;
	}

	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}

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

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
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

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
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

	public BigDecimal getCollectionFinanceYear() {
		return collectionFinanceYear;
	}

	public void setCollectionFinanceYear(BigDecimal collectionFinanceYear) {
		this.collectionFinanceYear = collectionFinanceYear;
	}

	public BigDecimal getCollectionDocumentNumber() {
		return collectionDocumentNumber;
	}

	public void setCollectionDocumentNumber(BigDecimal collectionDocumentNumber) {
		this.collectionDocumentNumber = collectionDocumentNumber;
	}

	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}

	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public CodeCategory getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(CodeCategory errorCategory) {
		this.errorCategory = errorCategory;
	}

	@Override
	public String toString() {
		return "PaymentResponseDto [paymentId=" + paymentId + ", errorText=" + errorText + ", udf1=" + udf1 + ", udf2="
				+ udf2 + ", udf3=" + udf3 + ", udf4=" + udf4 + ", udf5=" + udf5 + ", resultCode=" + resultCode
				+ ", auth_appNo=" + auth_appNo + ", trackId=" + trackId + ", referenceId=" + referenceId
				+ ", transactionId=" + transactionId + ", postDate=" + postDate + ", tranData=" + tranData
				+ ", customerId=" + customerId + ", applicationCountryId=" + applicationCountryId + ", companyId="
				+ companyId + ", userName=" + userName + ", collectionFinanceYear=" + collectionFinanceYear
				+ ", collectionDocumentNumber=" + collectionDocumentNumber + ", collectionDocumentCode="
				+ collectionDocumentCode + ", error=" + error + ", errorCategory=" + errorCategory + "]";
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
