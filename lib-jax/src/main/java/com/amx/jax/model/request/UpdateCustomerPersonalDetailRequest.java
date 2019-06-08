package com.amx.jax.model.request;

import java.util.Date;

import com.amx.jax.swagger.ApiMockModelProperty;

public class UpdateCustomerPersonalDetailRequest {

	@ApiMockModelProperty(example = "2000-09-12")
	Date dateOfBirth;

	@ApiMockModelProperty(example = "true")
	Boolean insurance;

	String customerSignature;

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
