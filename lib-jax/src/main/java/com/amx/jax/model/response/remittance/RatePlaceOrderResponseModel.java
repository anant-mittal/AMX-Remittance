package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RatePlaceOrderResponseModel {
	BigDecimal placeOrderId;
	Boolean booGsm;
	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}
	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}
	public Boolean getBooGsm() {
		return booGsm;
	}
	public void setBooGsm(Boolean booGsm) {
		this.booGsm = booGsm;
	}

}
