package com.amx.jax.dbmodel.insurance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_INSURANCE_SETUP_MASTER")
public class InsuranceSetupMaster {

	@Id
	@Column(name = "INS_SETPUP_ID")
	BigDecimal insurnaceSetupId;

	@Column(name = "COVER_AMOUNT")
	BigDecimal coverAmount;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getInsurnaceSetupId() {
		return insurnaceSetupId;
	}

	public void setInsurnaceSetupId(BigDecimal insurnaceSetupId) {
		this.insurnaceSetupId = insurnaceSetupId;
	}

	public BigDecimal getCoverAmount() {
		return coverAmount;
	}

	public void setCoverAmount(BigDecimal converAmount) {
		this.coverAmount = converAmount;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
}
