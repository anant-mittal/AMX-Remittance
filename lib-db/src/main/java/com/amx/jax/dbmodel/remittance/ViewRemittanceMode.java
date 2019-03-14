package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

 
@Entity
@Table(name = "V_EX_REMITTANCE_MODE")
public class ViewRemittanceMode implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="REMITTANCE_MODE_ID")
	private BigDecimal remittanceModeId;
	@Column(name="REMITTANCE_CODE")
	private String remittancCode;
	@Column(name="REMITTANCE_DESCRIPTION")
	private String remittanceDescription;
	@Column(name="LANGUAGE_ID")
	private BigDecimal languageId;
	
	
	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}
	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}
	public String getRemittancCode() {
		return remittancCode;
	}
	public void setRemittancCode(String remittancCode) {
		this.remittancCode = remittancCode;
	}
	public String getRemittanceDescription() {
		return remittanceDescription;
	}
	public void setRemittanceDescription(String remittanceDescription) {
		this.remittanceDescription = remittanceDescription;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	
	

}
