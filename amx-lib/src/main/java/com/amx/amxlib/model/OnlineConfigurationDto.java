package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class OnlineConfigurationDto extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getOnlineConfigId() {
		return onlineConfigId;
	}
	public void setOnlineConfigId(BigDecimal onlineConfigId) {
		this.onlineConfigId = onlineConfigId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getCurrency() {
		return currency;
	}
	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getResponseUrl() {
		return responseUrl;
	}
	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAppInd() {
		return appInd;
	}
	public void setAppInd(String appInd) {
		this.appInd = appInd;
	}
	@Override
	public String getModelType() {
		return "online-config";
	}
	
}
