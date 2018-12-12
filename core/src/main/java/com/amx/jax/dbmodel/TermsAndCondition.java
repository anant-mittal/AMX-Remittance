package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_TERMS_CONDITION" )
public class TermsAndCondition {
	private static final long serialVersionUID = 1L;
	private BigDecimal termsConditionId;//TERMS_CONDITION_ID
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private String status;
	private String description;
	private String moduleType;
	private String moduleDescription;
	private String channel;
	
	@Id
	@Column(name="TERMS_CONDITION_ID")
	public BigDecimal getTermsConditionId() {
		return termsConditionId;
	}
	public void setTermsConditionId(BigDecimal termsConditionId) {
		this.termsConditionId = termsConditionId;
	}
	@Column(name="COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	@Column(name="LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	@Column(name="ISACTIVE")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="MODULE_TYPE")
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	@Column(name="MODULE_DESC")
	public String getModuleDescription() {
		return moduleDescription;
	}
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}
	@Column(name="CHANNEL")
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
	
	
	
	
}
