package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentModeOfPaymentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal paymentModeId;
	private BigDecimal paymentModeDescId;
	private String paymentModeCode;
	private String paymentModeName;
	private BigDecimal languageId;
	private String isActive;
	
	public BigDecimal getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(BigDecimal paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	
	public BigDecimal getPaymentModeDescId() {
		return paymentModeDescId;
	}
	public void setPaymentModeDescId(BigDecimal paymentModeDescId) {
		this.paymentModeDescId = paymentModeDescId;
	}
	
	public String getPaymentModeCode() {
		return paymentModeCode;
	}
	public void setPaymentModeCode(String paymentModeCode) {
		this.paymentModeCode = paymentModeCode;
	}
	
	public String getPaymentModeName() {
		return paymentModeName;
	}
	public void setPaymentModeName(String paymentModeName) {
		this.paymentModeName = paymentModeName;
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
