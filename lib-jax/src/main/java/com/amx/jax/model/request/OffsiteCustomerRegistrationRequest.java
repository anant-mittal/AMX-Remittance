package com.amx.jax.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class OffsiteCustomerRegistrationRequest {
	
	//BigDecimal ecNumber;
	String identityInt;
	String mOtp;
	String eOtp;
	String email;
	String mobile;
	@NotNull
	private BigDecimal countryId;
	@NotNull
	private BigDecimal nationalityId;	
	
	
	public String getIdentityInt() {
		return identityInt;
	}
	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
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
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}	
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}
	@Override
	public String toString() {
		return "OffsiteCustomerRegistrationRequest [identityInt=" + identityInt + ", mOtp=" + mOtp + ", eOtp=" + eOtp
				+ ", email=" + email + ", mobile=" + mobile + ", countryId=" + countryId + ", nationalityId="
				+ nationalityId + "]";
	}
	
}
