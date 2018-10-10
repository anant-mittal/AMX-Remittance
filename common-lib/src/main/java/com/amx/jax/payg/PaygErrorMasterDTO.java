package com.amx.jax.payg;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Shivaraj
 *
 */

public class PaygErrorMasterDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal onlinePaygErrorId;
	private BigDecimal countryId; 
	private String errorCategory;
	private String paygCode;
	private String errorCode;
	
	public BigDecimal getOnlinePaygErrorId() {
		return onlinePaygErrorId;
	}
	public void setOnlinePaygErrorId(BigDecimal onlinePaygErrorId) {
		this.onlinePaygErrorId = onlinePaygErrorId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public String getErrorCategory() {
		return errorCategory;
	}
	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}
	public String getPaygCode() {
		return paygCode;
	}
	public void setPaygCode(String paygCode) {
		this.paygCode = paygCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
