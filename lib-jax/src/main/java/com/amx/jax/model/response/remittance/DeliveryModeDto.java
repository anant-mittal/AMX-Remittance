package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;



public class DeliveryModeDto {
	private BigDecimal rowId;
	private BigDecimal deliveryModeId;
	private String deliveryCode;
	private String deliveryDescription;
	

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}
	public String getDeliveryCode() {
		return deliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
	public String getDeliveryDescription() {
		return deliveryDescription;
	}
	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}

}
