package com.amx.jax.dbmodel;

/**
 * @auth :rabil
 * @date :06/03/2019
 */
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dbmodel.PaymentModeModel;

@Entity
@Table(name = "EX_PAYMENT_MODE_DESC")
public class PaymentModeDesc implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;
	private BigDecimal paymentModeDescId;
	private PaymentModeModel paymentMode;
	private LanguageType languageType;
	private String localPaymentName;

	public PaymentModeDesc() {

	}

	public PaymentModeDesc(BigDecimal paymentModeDescId,
			PaymentModeModel paymentMode, LanguageType languageType,
			String localPaymentName) {
		super();
		this.paymentModeDescId = paymentModeDescId;
		this.paymentMode = paymentMode;
		this.languageType = languageType;
		this.localPaymentName = localPaymentName;
	}

	@Id
	@GeneratedValue(generator = "ex_payment_mode_desc_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_payment_mode_desc_seq", sequenceName = "EX_PAYMENT_MODE_DESC_SEQ", allocationSize = 1)
	@Column(name = "PAYMENT_MODE_DESC_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getPaymentModeDescId() {
		return paymentModeDescId;
	}

	public void setPaymentModeDescId(BigDecimal paymentModeDescId) {
		this.paymentModeDescId = paymentModeDescId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENT_MODE_ID")
	public PaymentModeModel getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentModeModel paymentMode) {
		this.paymentMode = paymentMode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID")
	public LanguageType getLanguageType() {
		return languageType;
	}

	public void setLanguageType(LanguageType languageType) {
		this.languageType = languageType;
	}

	@Column(name = "LOCAL_PAYMENT_NAME")
	public String getLocalPaymentName() {
		return localPaymentName;
	}

	public void setLocalPaymentName(String localPaymentName) {
		this.localPaymentName = localPaymentName;
	}

}
