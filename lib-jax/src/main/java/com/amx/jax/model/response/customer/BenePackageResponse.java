package com.amx.jax.model.response.customer;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;


public class BenePackageResponse {
	BigDecimal fcAmount;
	List<JaxConditionalFieldDto> requiredFlexFields;

	public List<JaxConditionalFieldDto> getRequiredFlexFields() {
		return requiredFlexFields;
	}

	public void setRequiredFlexFields(List<JaxConditionalFieldDto> requiredFlexFields) {
		this.requiredFlexFields = requiredFlexFields;
	}

	public BigDecimal getFcAmount() {
		return fcAmount;
	}

	public void setFcAmount(BigDecimal fcAmount) {
		this.fcAmount = fcAmount;
	}
}
