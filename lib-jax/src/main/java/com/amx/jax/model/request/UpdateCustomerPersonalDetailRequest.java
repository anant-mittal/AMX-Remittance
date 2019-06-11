package com.amx.jax.model.request;

import java.util.Date;

public class UpdateCustomerPersonalDetailRequest {

	Date dateOfBirth;

	Boolean insurance;

	String customerSignature;

	Boolean pepsIndicator;

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public Boolean getInsurance() {
		return insurance;
	}

	public void setInsurance(Boolean insurance) {
		this.insurance = insurance;
	}

}
