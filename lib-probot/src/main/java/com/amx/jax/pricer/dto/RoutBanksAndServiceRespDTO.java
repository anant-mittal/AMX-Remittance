package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RoutBanksAndServiceRespDTO implements Serializable {

	private static final long serialVersionUID = -2594412775985894857L;

	private BigDecimal routingBankId;

	private String routingBankName;

	private BigDecimal serviceId;

	private String serviceDesc;

	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}

	public String getRoutingBankName() {
		return routingBankName;
	}

	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}

	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

}
