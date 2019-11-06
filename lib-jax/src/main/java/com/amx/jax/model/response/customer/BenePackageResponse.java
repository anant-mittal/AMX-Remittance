package com.amx.jax.model.response.customer;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;


public class BenePackageResponse {
	BigDecimal amount;
	List<JaxConditionalFieldDto> requiredFlexFields;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<JaxConditionalFieldDto> getRequiredFlexFields() {
		return requiredFlexFields;
	}

	public void setRequiredFlexFields(List<JaxConditionalFieldDto> requiredFlexFields) {
		this.requiredFlexFields = requiredFlexFields;
	}
}
