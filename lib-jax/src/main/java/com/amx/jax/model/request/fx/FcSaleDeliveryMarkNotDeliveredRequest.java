package com.amx.jax.model.request.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class FcSaleDeliveryMarkNotDeliveredRequest {

	@NotNull
	BigDecimal deliveryDetailSeqId;
	@NotNull
	BigDecimal deleviryRemarkSeqId;

	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}

	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}

	public BigDecimal getDeleviryRemarkSeqId() {
		return deleviryRemarkSeqId;
	}

	public void setDeleviryRemarkSeqId(BigDecimal deleviryRemarkSeqId) {
		this.deleviryRemarkSeqId = deleviryRemarkSeqId;
	}
}
