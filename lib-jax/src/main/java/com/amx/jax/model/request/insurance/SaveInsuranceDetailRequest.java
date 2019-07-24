package com.amx.jax.model.request.insurance;

import java.util.List;

import javax.validation.Valid;

public class SaveInsuranceDetailRequest {

	@Valid
	List<CreateOrUpdateNomineeRequest> addNomineeRequestData;
	
	Boolean optIn;


	public List<CreateOrUpdateNomineeRequest> getAddNomineeRequestData() {
		return addNomineeRequestData;
	}

	public void setAddNomineeRequestData(List<CreateOrUpdateNomineeRequest> addNomineeRequestData) {
		this.addNomineeRequestData = addNomineeRequestData;
	}

	public Boolean getOptIn() {
		return optIn;
	}

	public void setOptIn(Boolean optIn) {
		this.optIn = optIn;
	}

}
