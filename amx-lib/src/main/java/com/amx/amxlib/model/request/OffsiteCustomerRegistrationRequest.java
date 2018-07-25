package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class OffsiteCustomerRegistrationRequest {
	
	BigDecimal ecNumber;
	String civilId;
	String mOtp;
	String eOtp;
	
	public BigDecimal getEcNumber() {
		return ecNumber;
	}
	public void setEcNumber(BigDecimal ecNumber) {
		this.ecNumber = ecNumber;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getmOtp() {
		return mOtp;
	}
	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}
	public String geteOtp() {
		return eOtp;
	}
	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}
	@Override
	public String toString() {
		return "OffsiteCustomerRegistrationRequest [ecNumber=" + ecNumber + ", civilId=" + civilId + ", mOtp=" + mOtp
				+ ", eOtp=" + eOtp + "]";
	}
	
	

}
