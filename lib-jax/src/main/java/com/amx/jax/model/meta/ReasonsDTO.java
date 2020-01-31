package com.amx.jax.model.meta;

import java.math.BigDecimal;

public class ReasonsDTO {
	
	private BigDecimal reasonCodeId;	
	private String reasonCodeDesc;
	private String reasonCode;
	private String reasonCodeCategory;

	public BigDecimal getReasonCodeId() {
		return reasonCodeId;
	}
	public void setReasonCodeId(BigDecimal reasonCodeId) {
		this.reasonCodeId = reasonCodeId;
	}
	public String getReasonCodeDesc() {
		return reasonCodeDesc;
	}
	public void setReasonCodeDesc(String reasonCodeDesc) {
		this.reasonCodeDesc = reasonCodeDesc;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonCodeCategory() {
		return reasonCodeCategory;
	}
	public void setReasonCodeCategory(String reasonCodeCategory) {
		this.reasonCodeCategory = reasonCodeCategory;
	}
}
