package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RoutingCountryDto {

	BigDecimal routingCountryId;
	String routingCountryDescription;
	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}
	public void setRoutingCountryId(BigDecimal routingCountryId) {
		this.routingCountryId = routingCountryId;
	}
	public String getRoutingCountryDescription() {
		return routingCountryDescription;
	}
	public void setRoutingCountryDescription(String routingCountryDescription) {
		this.routingCountryDescription = routingCountryDescription;
	}
	
}
