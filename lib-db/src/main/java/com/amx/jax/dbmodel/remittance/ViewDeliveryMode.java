package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "V_EX_DELIVERY_MODE")
public class ViewDeliveryMode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;
	@Column(name="DELIVERY_CODE")
	private String deliveryCode;
	
	@Column(name="DELIVERY_DESCRIPTION")
	private String deliveryDescription;
	
	@Column(name="LANGUAGE_ID")
	private BigDecimal languageId;
	
	
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
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	

}

