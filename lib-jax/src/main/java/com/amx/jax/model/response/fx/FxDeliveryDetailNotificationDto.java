package com.amx.jax.model.response.fx;

public class FxDeliveryDetailNotificationDto {

	String mOtp;
	String mOtpPrefix;
	FxDeliveryDetailDto deliveryDetail;

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
