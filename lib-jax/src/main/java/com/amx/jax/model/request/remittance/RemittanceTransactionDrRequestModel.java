package com.amx.jax.model.request.remittance;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;

public class RemittanceTransactionDrRequestModel extends RemittanceTransactionRequestModel{

	@NotNull
	DynamicRoutingPricingDto dynamicRroutingPricingBreakup;
	public DynamicRoutingPricingDto getDynamicRroutingPricingBreakup() {
		return dynamicRroutingPricingBreakup;
	}
	public void setDynamicRroutingPricingBreakup(DynamicRoutingPricingDto dynamicRroutingPricingBreakup) {
		this.dynamicRroutingPricingBreakup = dynamicRroutingPricingBreakup;
	}
	
}
