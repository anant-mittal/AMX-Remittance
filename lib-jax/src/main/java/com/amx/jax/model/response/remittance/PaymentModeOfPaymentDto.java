package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class PaymentModeOfPaymentDto extends ResourceDTO {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private BigDecimal paymentModeDescId;
	private BigDecimal languageId;
	private String isActive;

	public BigDecimal getPaymentModeDescId() {
		return paymentModeDescId;
	}

	public void setPaymentModeDescId(BigDecimal paymentModeDescId) {
		this.paymentModeDescId = paymentModeDescId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
