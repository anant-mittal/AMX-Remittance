package com.amx.amxlib.meta.model;

import java.math.BigDecimal;


public class AuthenticationLimitCheckDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal applicationCountryId;
	private String authorizationType;
	private BigDecimal authId;
	private BigDecimal authLimit;
	private String authMessage;
	private BigDecimal authPercentage;
	private String authDesc;
	

	public BigDecimal getAuthId() {
		return authId;
	}
	public void setAuthId(BigDecimal authId) {
		this.authId = authId;
	}
	

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	

	public String getAuthorizationType() {
		return authorizationType;
	}
	public void setAuthorizationType(String authorizationType) {
		this.authorizationType = authorizationType;
	}
	

	public BigDecimal getAuthLimit() {
		return authLimit;
	}
	public void setAuthLimit(BigDecimal authLimit) {
		this.authLimit = authLimit;
	}
	

	public String getAuthMessage() {
		return authMessage;
	}
	public void setAuthMessage(String authMessage) {
		this.authMessage = authMessage;
	}
	

	public BigDecimal getAuthPercentage() {
		return authPercentage;
	}
	public void setAuthPercentage(BigDecimal authPercentage) {
		this.authPercentage = authPercentage;
	}
	
	
	public String getAuthDesc() {
		return authDesc;
	}
	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}
	
	
	
	

}
