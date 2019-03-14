package com.amx.jax.model.response.remittance;

public class AmlCheckResponseDto {
	
	private String messageCode;
	private String messageDescription;
	private String amlFlag="N";
	private String rangeSlab;
	private String authType;
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessageDescription() {
		return messageDescription;
	}
	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}
	public String getAmlFlag() {
		return amlFlag;
	}
	public void setAmlFlag(String amlFlag) {
		this.amlFlag = amlFlag;
	}
	
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getRangeSlab() {
		return rangeSlab;
	}
	public void setRangeSlab(String rangeSlab) {
		this.rangeSlab = rangeSlab;
	}

}
