package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;

public class RoutingProductStatusDetails implements Serializable {

	private static final long serialVersionUID = -2890168223468730676L;

	private List<RoutingProductStatusDetails> routingProductsStatus;

	public List<RoutingProductStatusDetails> getRoutingProductsStatus() {
		return routingProductsStatus;
	}

	public void setRoutingProductsStatus(List<RoutingProductStatusDetails> routingProductsStatus) {
		this.routingProductsStatus = routingProductsStatus;
	}

}
