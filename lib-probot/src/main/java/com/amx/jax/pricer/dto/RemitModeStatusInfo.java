package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

public class RemitModeStatusInfo implements Serializable, Comparable<RemitModeStatusInfo> {

	private static final long serialVersionUID = 2028657101281895917L;

	private BigDecimal remitModeId;
	private String remitModeDesc;

	private ROUTING_STATUS routingStatus;
	private ROUTING_STATUS productStatus;

	private BigDecimal provisionalTrnxAmountLocal;
	private BigDecimal provisionalTrnxAmountForeign;

	private List<DeliveryModeStatusInfo> deliveryModesStatus;

	public BigDecimal getRemitModeId() {
		return remitModeId;
	}

	public void setRemitModeId(BigDecimal remitModeId) {
		this.remitModeId = remitModeId;
	}

	public String getRemitModeDesc() {
		return remitModeDesc;
	}

	public void setRemitModeDesc(String remitModeDesc) {
		this.remitModeDesc = remitModeDesc;
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

	public void setProductStatus(ROUTING_STATUS fieldStatus) {
		this.productStatus = fieldStatus;
	}

	public BigDecimal getProvisionalTrnxAmountLocal() {
		return provisionalTrnxAmountLocal;
	}

	public void setProvisionalTrnxAmountLocal(BigDecimal pendingTrnxAmountLocal) {
		this.provisionalTrnxAmountLocal = pendingTrnxAmountLocal;
	}

	public BigDecimal getProvisionalTrnxAmountForeign() {
		return provisionalTrnxAmountForeign;
	}

	public void setProvisionalTrnxAmountForeign(BigDecimal pendingTrnxAmountForeign) {
		this.provisionalTrnxAmountForeign = pendingTrnxAmountForeign;
	}

	public List<DeliveryModeStatusInfo> getDeliveryModesStatus() {
		return deliveryModesStatus;
	}

	public void setDeliveryModesStatus(List<DeliveryModeStatusInfo> deliveryModesStatus) {
		this.deliveryModesStatus = deliveryModesStatus;
	}

	@Override
	public int compareTo(RemitModeStatusInfo o) {
		if (o == null)
			return -1;

		return this.remitModeId.compareTo(o.remitModeId);
	}
}
