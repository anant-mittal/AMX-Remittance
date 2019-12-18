package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

public class DeliveryModeStatusInfo implements Serializable, Comparable<DeliveryModeStatusInfo> {

	private static final long serialVersionUID = -4678281755411004882L;

	private BigDecimal deliveryModeId;
	private String deliveryModeDesc;

	private ROUTING_STATUS routingStatus;
	private ROUTING_STATUS productStatus;

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public String getDeliveryModeDesc() {
		return deliveryModeDesc;
	}

	public void setDeliveryModeDesc(String deliveryModeDesc) {
		this.deliveryModeDesc = deliveryModeDesc;
	}

	public ROUTING_STATUS getRoutingStatus() {
		return routingStatus;
	}

	public void setRoutingStatus(ROUTING_STATUS routingStatus) {
		this.routingStatus = routingStatus;
	}

	public ROUTING_STATUS getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ROUTING_STATUS productStatus) {
		this.productStatus = productStatus;
	}

	@Override
	public int compareTo(DeliveryModeStatusInfo o) {
		if (o == null)
			return -1;

		return this.deliveryModeId.compareTo(o.deliveryModeId);
	}

}
