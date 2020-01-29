package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class RoutingBankDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Rountry Bank Id can not be Null or Empty")
	private BigDecimal routingBankId;
	
	@NotNull(message="Remittance Id can not be Null or Empty")
	private BigDecimal remittanceId;
	
	@NotNull(message="Delivery Id can not be Null or Empty")
	private BigDecimal deliveryId;
	
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	
	public BigDecimal getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(BigDecimal remittanceId) {
		this.remittanceId = remittanceId;
	}
	
	public BigDecimal getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
	}

}
