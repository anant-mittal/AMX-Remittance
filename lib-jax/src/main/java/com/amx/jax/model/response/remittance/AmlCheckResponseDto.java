package com.amx.jax.model.response.remittance;



public class AmlCheckResponseDto {
	
	private String messageCode;
	private String messageDescription;
	private String amlFlag="N";
	private String rangeSlab;
	private String authType;
	
	String blackRemark1;
	String blackRemark2;
	String blackRemark3;
	String tag;
	String stopTrnxFlag;
	String highValueTrnxFlag;
	
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
	public String getBlackRemark1() {
		return blackRemark1;
	}
	public void setBlackRemark1(String blackRemark1) {
		this.blackRemark1 = blackRemark1;
	}
	public String getBlackRemark2() {
		return blackRemark2;
	}
	public void setBlackRemark2(String blackRemark2) {
		this.blackRemark2 = blackRemark2;
	}
	public String getBlackRemark3() {
		return blackRemark3;
	}
	public void setBlackRemark3(String blackRemark3) {
		this.blackRemark3 = blackRemark3;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getStopTrnxFlag() {
		return stopTrnxFlag;
	}
	public void setStopTrnxFlag(String stopTrnxFlag) {
		this.stopTrnxFlag = stopTrnxFlag;
	}
	public String getHighValueTrnxFlag() {
		return highValueTrnxFlag;
	}
	public void setHighValueTrnxFlag(String highValueTrnxFlag) {
		this.highValueTrnxFlag = highValueTrnxFlag;
	}

}
