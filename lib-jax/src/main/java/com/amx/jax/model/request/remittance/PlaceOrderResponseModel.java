package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;

public class PlaceOrderResponseModel {

	DynamicRoutingPricingDto dto = new DynamicRoutingPricingDto();
	BigDecimal beneId;
	public DynamicRoutingPricingDto getDto() {
		return dto;
	}
	public void setDto(DynamicRoutingPricingDto dto) {
		this.dto = dto;
	}
	public BigDecimal getBeneId() {
		return beneId;
	}
	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}
	
	
}
