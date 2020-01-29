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
	String riskLevel1;
	String riskLevel2;
	String riskLevel3;
	String riskLevel4;
	String riskLevel5;
	
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
	public String getRiskLevel1() {
		return riskLevel1;
	}
	public void setRiskLevel1(String riskLevel1) {
		this.riskLevel1 = riskLevel1;
	}
	public String getRiskLevel2() {
		return riskLevel2;
	}
	public void setRiskLevel2(String riskLevel2) {
		this.riskLevel2 = riskLevel2;
	}
	public String getRiskLevel3() {
		return riskLevel3;
	}
	public void setRiskLevel3(String riskLevel3) {
		this.riskLevel3 = riskLevel3;
	}
	public String getRiskLevel4() {
		return riskLevel4;
	}
	public void setRiskLevel4(String riskLevel4) {
		this.riskLevel4 = riskLevel4;
	}
	public String getRiskLevel5() {
		return riskLevel5;
	}
	public void setRiskLevel5(String riskLevel5) {
		this.riskLevel5 = riskLevel5;
	}

}
