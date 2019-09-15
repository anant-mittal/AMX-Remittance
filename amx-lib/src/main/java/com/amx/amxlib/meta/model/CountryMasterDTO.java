package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Rabil
 *
 */

public class CountryMasterDTO implements Serializable {

	private static final long serialVersionUID = 6575018794136014643L;

	private BigDecimal idNo;
	private String countryName;
	private BigDecimal countryId;
	private BigDecimal languageId;
	private String nationality;
	private String approvedBy;
	private Date approvedDate;
	private String businessCountry;
	private String countryActive;
	private String countryAlpha2Code;
	private String countryAlpha3Code;
	private String countryCode;
	private String countryISOCode;
	private String countryTelCode;
	private String createdBy;
	private Date createdDate;
	private String isActive;
	private BigDecimal splitIndicator;
	private String direction;
	private String languageCode;
	private String languageName;
	private List<ServiceGroupMasterDescDto> supportedServiceGroup;
	private String countryMobileLength;
	private String localName;

	public CountryMasterDTO() {
		super();
	}

	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getBusinessCountry() {
		return businessCountry;
	}

	public void setBusinessCountry(String businessCountry) {
		this.businessCountry = businessCountry;
	}

	public String getCountryActive() {
		return countryActive;
	}

	public void setCountryActive(String countryActive) {
		this.countryActive = countryActive;
	}

	public String getCountryAlpha2Code() {
		return countryAlpha2Code;
	}

	public void setCountryAlpha2Code(String countryAlpha2Code) {
		this.countryAlpha2Code = countryAlpha2Code;
	}

	public String getCountryAlpha3Code() {
		return countryAlpha3Code;
	}

	public void setCountryAlpha3Code(String countryAlpha3Code) {
		this.countryAlpha3Code = countryAlpha3Code;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryISOCode() {
		return countryISOCode;
	}

	public void setCountryISOCode(String countryISOCode) {
		this.countryISOCode = countryISOCode;
	}

	public String getCountryTelCode() {
		return countryTelCode;
	}

	public void setCountryTelCode(String countryTelCode) {
		this.countryTelCode = countryTelCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getSplitIndicator() {
		return splitIndicator;
	}

	public void setSplitIndicator(BigDecimal splitIndicator) {
		this.splitIndicator = splitIndicator;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public List<ServiceGroupMasterDescDto> getSupportedServiceGroup() {
		return supportedServiceGroup;
	}

	public void setSupportedServiceGroup(List<ServiceGroupMasterDescDto> supportedServiceGroup) {
		this.supportedServiceGroup = supportedServiceGroup;
	}
	
	public String getCountryMobileLength() {
		return countryMobileLength;
	}

	public void setCountryMobileLength(String countryMobileLength) {
		this.countryMobileLength = countryMobileLength;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}
	
	
}
