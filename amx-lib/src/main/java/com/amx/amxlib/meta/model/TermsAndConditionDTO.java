package com.amx.amxlib.meta.model;

import java.math.BigDecimal;


public class TermsAndConditionDTO {

	private BigDecimal termsConditionId;//TERMS_CONDITION_ID
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private String status;
	private String description;
	

	public BigDecimal getTermsConditionId() {
		return termsConditionId;
	}
	public void setTermsConditionId(BigDecimal termsConditionId) {
		this.termsConditionId = termsConditionId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
	
}
