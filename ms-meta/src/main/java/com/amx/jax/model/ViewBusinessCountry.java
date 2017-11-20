package com.amx.jax.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionId;

@Entity
@Table(name="VIEW_EX_BUSINESS_COUNTRY")
public class ViewBusinessCountry {
	
	private BigDecimal srNo;
	private BigDecimal countryId;
	private BigDecimal languageId;
	private String countryName;
	private String countryAlphaTwoCode;
	private String nationality;
	@Id
	@Column(name="srno")
	public BigDecimal getSrNo() {
		return srNo;
	}
	public void setSrNo(BigDecimal srNo) {
		this.srNo = srNo;
	}
	
	@Column(name="LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	@Column(name="COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Column(name="COUNTRY_ALPHA2_CODE")
	public String getCountryAlphaTwoCode() {
		return countryAlphaTwoCode;
	}
	public void setCountryAlphaTwoCode(String countryAlphaTwoCode) {
		this.countryAlphaTwoCode = countryAlphaTwoCode;
	}
	@Column(name="NATIONALITY")
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
}
