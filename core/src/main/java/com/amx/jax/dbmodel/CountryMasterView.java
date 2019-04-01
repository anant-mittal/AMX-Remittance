package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.amx.jax.model.IResourceEntity;

/**
 * 
 * @author Rabil
 *
 */

@Entity
@Table(name = "V_EX_COUNTRY_LANG")
public class CountryMasterView implements java.io.Serializable, IResourceEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private String countryMobileLength;
	private Integer beneCountryRisk;
	private Integer remitterCountryRisk;

	public CountryMasterView() {
		super();
	}

	@Id
	@Column(name = "IDNO")
	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}

	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Column(name = "NATIONALITY")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "BUSINESS_COUNTRY")
	public String getBusinessCountry() {
		return businessCountry;
	}

	public void setBusinessCountry(String businessCountry) {
		this.businessCountry = businessCountry;
	}

	@Column(name = "COUNTRY_ACTIVE")
	public String getCountryActive() {
		return countryActive;
	}

	public void setCountryActive(String countryActive) {
		this.countryActive = countryActive;
	}

	@Column(name = "COUNTRY_ALPHA2_CODE")
	public String getCountryAlpha2Code() {
		return countryAlpha2Code;
	}

	public void setCountryAlpha2Code(String countryAlpha2Code) {
		this.countryAlpha2Code = countryAlpha2Code;
	}

	@Column(name = "COUNTRY_ALPHA3_CODE")
	public String getCountryAlpha3Code() {
		return countryAlpha3Code;
	}

	public void setCountryAlpha3Code(String countryAlpha3Code) {
		this.countryAlpha3Code = countryAlpha3Code;
	}

	@Column(name = "COUNTRY_CODE")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "COUNTRY_ISO_CODE")
	public String getCountryISOCode() {
		return countryISOCode;
	}

	public void setCountryISOCode(String countryISOCode) {
		this.countryISOCode = countryISOCode;
	}

	@Column(name = "COUNTRY_TEL_CODE")
	public String getCountryTelCode() {
		//return StringUtils.stripStart(countryTelCode, "0");
		return countryTelCode;
	}

	public void setCountryTelCode(String countryTelCode) {
		this.countryTelCode = countryTelCode;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "SPLIT_INDICATOR")
	public BigDecimal getSplitIndicator() {
		return splitIndicator;
	}

	public void setSplitIndicator(BigDecimal splitIndicator) {
		this.splitIndicator = splitIndicator;
	}

	@Column(name = "DIRECTION")
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Column(name = "LANGUAGE_CODE")
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	@Column(name = "LANGUAGE_NAME")
	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	@Column(name = "COUNTRY_MOB_LENGTH")
	public String getCountryMobileLength() {
		return countryMobileLength;
	}

	public void setCountryMobileLength(String countryMobileLength) {
		this.countryMobileLength = countryMobileLength;
	}

	@Override
	public BigDecimal resourceId() {
		return this.countryId;
	}

	@Override
	public String resourceName() {
		return this.countryName;
	}

	@Override
	public String resourceCode() {
		return this.countryISOCode;
	}

	@Column(name = "bene_country_risk")
	public Integer getBeneCountryRisk() {
		return beneCountryRisk;
	}

	public void setBeneCountryRisk(Integer beneCountryRisk) {
		this.beneCountryRisk = beneCountryRisk;
	}

	@Column(name = "remitter_country_risk")
	public Integer getRemitterCountryRisk() {
		return remitterCountryRisk;
	}

	public void setRemitterCountryRisk(Integer remitterCountryRisk) {
		this.remitterCountryRisk = remitterCountryRisk;
	}

}
