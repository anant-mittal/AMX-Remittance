package com.amx.jax.model.request;

import java.util.Date;

import com.amx.jax.swagger.ApiMockModelProperty;

public class EditCustomerInfoRequest {

	@ApiMockModelProperty(example = "2018-09-12")
	Date dateOfBirth;

	@ApiMockModelProperty(example = "Y")
	String insurance;

	String customerSignature;

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

}
