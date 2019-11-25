package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_REMIT_STATUS;

public class RemitModeStatusInfo implements Serializable, Comparable<RemitModeStatusInfo> {

	private static final long serialVersionUID = 2028657101281895917L;

	private BigDecimal remitModeId;
	private String remitModeDesc;

	private ROUTING_REMIT_STATUS routingStatus;
	private ROUTING_REMIT_STATUS productStatus;

	private BigDecimal provisionalTrnxAmountLocal;
	private BigDecimal provisionalTrnxAmountForeign;

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

	public ROUTING_REMIT_STATUS getRoutingStatus() {
		return routingStatus;
	}

	public void setRoutingStatus(ROUTING_REMIT_STATUS routingStatus) {
		this.routingStatus = routingStatus;
	}

	public ROUTING_REMIT_STATUS getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ROUTING_REMIT_STATUS fieldStatus) {
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

	@Override
	public int compareTo(RemitModeStatusInfo o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
