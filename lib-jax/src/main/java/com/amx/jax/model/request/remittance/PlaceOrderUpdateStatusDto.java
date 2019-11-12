package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

public class PlaceOrderUpdateStatusDto {
	BigDecimal ratePlaceOrderId;
	String flag;
	String remarks;
	public BigDecimal getRatePlaceOrderId() {
		return ratePlaceOrderId;
	}
	public void setRatePlaceOrderId(BigDecimal ratePlaceOrderId) {
		this.ratePlaceOrderId = ratePlaceOrderId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
