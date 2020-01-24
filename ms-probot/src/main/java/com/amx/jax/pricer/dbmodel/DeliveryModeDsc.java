package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_DELIVERY_MODE_DESC")
public class DeliveryModeDsc implements Serializable {
	private static final long serialVersionUID = 2315791709068216698L;

	private BigDecimal deliveryModeDescId;
	private BigDecimal deliveryModeId;
	private BigDecimal languageId;
	private String deliveryDesc;

	@Id
	@Column(name = "DELIVERY_MODE_DESC_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getDeliveryModeDescId() {
		return deliveryModeDescId;
	}

	public void setDeliveryModeDescId(BigDecimal deliveryModeDescId) {
		this.deliveryModeDescId = deliveryModeDescId;
	}

	@Column(name = "DELIVERY_MODE_ID")
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Column(name = "DELIVERY_DESCRIPTION")
	public String getDeliveryDesc() {
		return deliveryDesc;
	}

	public void setDeliveryDesc(String deliveryDesc) {
		this.deliveryDesc = deliveryDesc;
	}

}
