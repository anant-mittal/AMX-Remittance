package com.amx.jax.model.request.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class FcSaleDeliveryDetailUpdateReceiptRequest {

	@NotNull
	BigDecimal deliveryDetailSeqId;
	@NotNull
	String transactionRecieptImageClob;
	
	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}
	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}
	public String getTransactionRecieptImageClob() {
		return transactionRecieptImageClob;
	}
	public void setTransactionRecieptImageClob(String transactionRecieptImageClob) {
		this.transactionRecieptImageClob = transactionRecieptImageClob;
	}
}
