package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

public class FxDeliveryDetailNotificationDto {

	public BigDecimal getTranxId() {
		return tranxId;
	}


	public void setTranxId(BigDecimal tranxId) {
		this.tranxId = tranxId;
	}



	public String getVerCode() {
		return verCode;
	}


	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}



	String mOtp;
	String mOtpPrefix;
	FxDeliveryDetailDto deliveryDetail;
	BigDecimal tranxId;
	String verCode;
	

	public FxDeliveryDetailNotificationDto(String mOtp, String mOtpPrefix, FxDeliveryDetailDto deliveryDetail) {
		super();
		this.mOtp = mOtp;
		this.mOtpPrefix = mOtpPrefix;
		this.deliveryDetail = deliveryDetail;
	}
	

	public FxDeliveryDetailNotificationDto(FxDeliveryDetailDto deliveryDetail) {
		super();
		this.deliveryDetail = deliveryDetail;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public FxDeliveryDetailDto getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(FxDeliveryDetailDto deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}
}
