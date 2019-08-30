package com.amx.jax.model.request.remittance;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class GetServiceApplicabilityRequest {

	@NotNull
	@ApiMockModelProperty(example="94")
	Integer countryId;
	@NotNull
	@ApiMockModelProperty(example="2")
	Integer currencyId;
	@NotNull
	List<String> fieldNames;

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

}
