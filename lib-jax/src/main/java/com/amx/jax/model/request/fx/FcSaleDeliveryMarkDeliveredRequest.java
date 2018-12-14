package com.amx.jax.model.request.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class FcSaleDeliveryMarkDeliveredRequest {

	@NotNull
	BigDecimal deliveryDetailSeqId;
	 

	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}
	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}
	@Override
	public String toString() {
		return "FcSaleDeliveryMarkDeliveredRequest [deliveryDetailSeqId=" + deliveryDetailSeqId + "]";
	}
	
	
}
