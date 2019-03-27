package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RoutingBankDto {
	private BigDecimal routingBankId;
	private String routingBankCode;
	private String routingBankName;
	
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	public String getRoutingBankCode() {
		return routingBankCode;
	}
	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}
	public String getRoutingBankName() {
		return routingBankName;
	}
	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}

	

}
