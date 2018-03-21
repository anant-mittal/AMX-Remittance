package com.amx.jax.payment.gateway;

public class PayGResponse {

	public static enum PayGStatus {
		INIT, CAPTURED, CANCELLED, ERROR
	}

	String paymentiId = null;
	String result = null;
	String auth = null;
	String ref = null;
	String postDate = null;
	String trackId = null;
	String tranxId = null;
	String responseCode = null;
	String udf1 = null;
	String udf2 = null;
	String udf3 = null;
	String udf4 = null;
	String udf5 = null;
	String countryId = null;
	String errorText = null;
	
	String collectionFinYear;
	String collectionDocNumber;
	String collectionDocCode;

	PayGStatus payGStatus = PayGStatus.INIT;

	public PayGStatus getPayGStatus() {
		return payGStatus;
	}

	public void setPayGStatus(PayGStatus payGStatus) {
		this.payGStatus = payGStatus;
	}

	public String getCollectionFinYear() {
		return collectionFinYear;
	}

	public void setCollectionFinYear(String collectionFinYear) {
		this.collectionFinYear = collectionFinYear;
	}

	public String getCollectionDocNumber() {
		return collectionDocNumber;
	}

	public void setCollectionDocNumber(String collectionDocNumber) {
		this.collectionDocNumber = collectionDocNumber;
	}

	public String getCollectionDocCode() {
		return collectionDocCode;
	}

	public void setCollectionDocCode(String collectionDocCode) {
		this.collectionDocCode = collectionDocCode;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
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

	public String getPaymentiId() {
		return paymentiId;
	}

	public void setPaymentiId(String paymentiId) {
		this.paymentiId = paymentiId;
	}

    /**
     * @return the errorText
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * @param errorText the errorText to set
     */
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

}
