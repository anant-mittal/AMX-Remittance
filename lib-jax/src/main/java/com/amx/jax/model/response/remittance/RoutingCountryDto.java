package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

@Deprecated
public class RoutingCountryDto extends ResourceDTO {

	private static final long serialVersionUID = -4744148641802508045L;

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
