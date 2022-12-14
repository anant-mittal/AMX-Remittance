package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "EX_INFORMATION" )
public class WhyDoAskInformation {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal infoId;//TERMS_CONDITION_ID
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private String status;
	private String description;
	

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
	@Id
	@Column(name="INFO_ID")
	public BigDecimal getInfoId() {
		return infoId;
	}
	public void setInfoId(BigDecimal infoId) {
		this.infoId = infoId;
	}
	
	
}
