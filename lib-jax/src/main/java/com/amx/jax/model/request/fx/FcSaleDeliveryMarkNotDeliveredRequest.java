package com.amx.jax.model.request.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class FcSaleDeliveryMarkNotDeliveredRequest {

	@NotNull
	BigDecimal deliveryDetailSeqId;
	@NotNull
	BigDecimal deliveryRemarkSeqId;

	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}

	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}

	public BigDecimal getDeleviryRemarkSeqId() {
		return deliveryRemarkSeqId;
	}

	public void setDeleviryRemarkSeqId(BigDecimal deleviryRemarkSeqId) {
		this.deliveryRemarkSeqId = deleviryRemarkSeqId;
	}
}
