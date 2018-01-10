package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EX_ONLINE_CONFIG")
public class OnlineConfiguration {
	
	private BigDecimal companyId;
	private BigDecimal onlineConfigId;
	private BigDecimal countryId;
	private BigDecimal currency;
	private String action;
	private String languageCode;
	private String responseUrl;
	private String errorUrl;
	private String aliasName;
	private String resourcePath;
	private String status;
	private String appInd;
	
	@Column(name="COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	@Id
	@Column(name="ONLINE_CONFIG_ID")
	public BigDecimal getOnlineConfigId() {
		return onlineConfigId;
	}
	public void setOnlineConfigId(BigDecimal onlineConfigId) {
		this.onlineConfigId = onlineConfigId;
	}
	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	@Column(name="CURRENCY")
	public BigDecimal getCurrency() {
		return currency;
	}
	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}
	@Column(name="ACTION")
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Column(name="LANGUAGE_CODE")
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	@Column(name="RESPONSE_URL")
	public String getResponseUrl() {
		return responseUrl;
	}
	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	@Column(name="ERROR_URL")
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	@Column(name="ALIAS_NAME")
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	@Column(name="RESOURCE_PATH")
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	@Column(name="ISACTIVE")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="APPL_IND")
	public String getAppInd() {
		return appInd;
	}
	public void setAppInd(String appInd) {
		this.appInd = appInd;
	}
}
