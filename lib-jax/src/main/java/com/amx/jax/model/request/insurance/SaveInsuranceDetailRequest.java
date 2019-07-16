package com.amx.jax.model.request.insurance;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SaveInsuranceDetailRequest {

	@NotNull
	@Valid
	List<CreateOrUpdateNomineeRequest> addNomineeRequestData;

	public List<CreateOrUpdateNomineeRequest> getAddNomineeRequestData() {
		return addNomineeRequestData;
	}

	public void setAddNomineeRequestData(List<CreateOrUpdateNomineeRequest> addNomineeRequestData) {
		this.addNomineeRequestData = addNomineeRequestData;
	}

}
