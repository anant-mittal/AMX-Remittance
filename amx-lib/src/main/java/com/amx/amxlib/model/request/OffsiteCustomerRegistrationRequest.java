package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class OffsiteCustomerRegistrationRequest {
	
	BigDecimal ecNumber;
	String civilId;
	BigDecimal mOtp;
	BigDecimal eOtp;
	
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
	public BigDecimal getmOtp() {
		return mOtp;
	}
	public void setmOtp(BigDecimal mOtp) {
		this.mOtp = mOtp;
	}
	public BigDecimal geteOtp() {
		return eOtp;
	}
	public void seteOtp(BigDecimal eOtp) {
		this.eOtp = eOtp;
	}
	@Override
	public String toString() {
		return "OffsiteCustomerRegistrationRequest [ecNumber=" + ecNumber + ", civilId=" + civilId + ", mOtp=" + mOtp
				+ ", eOtp=" + eOtp + "]";
	}
	
	

}
