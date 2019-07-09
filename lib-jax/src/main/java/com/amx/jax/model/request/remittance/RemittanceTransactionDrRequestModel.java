package com.amx.jax.model.request.remittance;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.utils.ArgUtil;

public class RemittanceTransactionDrRequestModel extends RemittanceTransactionRequestModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4453130484416309371L;
	@NotNull
	DynamicRoutingPricingDto dynamicRroutingPricingBreakup;
	public DynamicRoutingPricingDto getDynamicRroutingPricingBreakup() {
		return dynamicRroutingPricingBreakup;
	}
	public void setDynamicRroutingPricingBreakup(DynamicRoutingPricingDto dynamicRroutingPricingBreakup) {
		this.dynamicRroutingPricingBreakup = dynamicRroutingPricingBreakup;
	}
	
	@Override
	public ExchangeRateBreakup getExchangeRateBreakup() {
		if(ArgUtil.isEmpty(super.getExchangeRateBreakup())){
			return this.dynamicRroutingPricingBreakup.getExRateBreakup();	
		}
		return super.getExchangeRateBreakup();
	}
}
