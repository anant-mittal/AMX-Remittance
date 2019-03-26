package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class RemittanceModeDto {
	
	private BigDecimal remittanceModeId;
	private String remittancCode;
	private String remittanceDescription;
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
}
