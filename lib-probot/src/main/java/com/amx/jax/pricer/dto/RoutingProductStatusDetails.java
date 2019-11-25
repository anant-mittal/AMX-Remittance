package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;

public class RoutingProductStatusDetails implements Serializable {

	private static final long serialVersionUID = -2890168223468730676L;

	private List<RoutingProductStatusInfo> routingProductsStatus;

	public List<RoutingProductStatusInfo> getRoutingProductsStatus() {
		return routingProductsStatus;
	}

	public void setRoutingProductsStatus(List<RoutingProductStatusInfo> routingProductsStatus) {
		this.routingProductsStatus = routingProductsStatus;
	}

}
