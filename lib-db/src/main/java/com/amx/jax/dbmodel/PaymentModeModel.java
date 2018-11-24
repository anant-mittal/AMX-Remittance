package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_PAYMENT_MODE")
public class PaymentModeModel {

	 @Id
	 @GeneratedValue(generator = "ex_payment_mode_seq", strategy = GenerationType.SEQUENCE)
	 @SequenceGenerator(name = "ex_payment_mode_seq", sequenceName = "EX_PAYMENT_MODE_SEQ", allocationSize = 1)
	 @Column(name = "PAYMENT_MODE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	 private BigDecimal paymentModeId;
	  
	  @Column(name="PAYMENT_CODE")
	  private String paymentModeCode;
	  
	  @Column(name="ISACTIVE")
	  private String isActive;

	public BigDecimal getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(BigDecimal paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPaymentModeCode() {
		return paymentModeCode;
	}

	public void setPaymentModeCode(String paymentModeCode) {
		this.paymentModeCode = paymentModeCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	

}
