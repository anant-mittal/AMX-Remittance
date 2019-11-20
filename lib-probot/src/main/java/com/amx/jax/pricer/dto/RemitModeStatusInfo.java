package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

public class RemitModeStatusInfo implements Serializable, Comparable<RemitModeStatusInfo> {

	private static final long serialVersionUID = 2028657101281895917L;

	private BigDecimal remitModeId;
	private String remitModeDesc;

	private ROUTING_STATUS routingStatus;

	private BigDecimal pendingTrnxAmountLocal;
	private BigDecimal pendingTrnxAmountForeign;

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

	public BigDecimal getPendingTrnxAmountLocal() {
		return pendingTrnxAmountLocal;
	}

	public void setPendingTrnxAmountLocal(BigDecimal pendingTrnxAmountLocal) {
		this.pendingTrnxAmountLocal = pendingTrnxAmountLocal;
	}

	public BigDecimal getPendingTrnxAmountForeign() {
		return pendingTrnxAmountForeign;
	}

	public void setPendingTrnxAmountForeign(BigDecimal pendingTrnxAmountForeign) {
		this.pendingTrnxAmountForeign = pendingTrnxAmountForeign;
	}

	@Override
	public int compareTo(RemitModeStatusInfo o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
