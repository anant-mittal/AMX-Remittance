package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

public class RateUploadRuleDto implements Serializable {

	private static final long serialVersionUID = 7960231131456740830L;

	private String ruleId;

	@NotNull(message = "Currency Id Can not be Null or Empty")
	private BigDecimal currencyId;
	private String curDisplayName;

	private BigDecimal countryId;
	private String countryDisplayName;

	private Map<BigDecimal, String> bankIdQuoteMap;

	private Map<BigDecimal, String> serviceIdNameMap;

	private Map<BigDecimal, String> branchGroupIdNameMap;

	private Map<BigDecimal, String> cBranchIdNameMap;

	@NotNull(message = "Sell Rate Can not be Null or Empty")
	private BigDecimal sellExchangeRate;
	
	@NotNull(message = "Buy Rate Can not be Null or Empty")
	private BigDecimal buyExchangeRate;

	private IS_ACTIVE isActive;
	private RATE_UPLOAD_STATUS status;

	private String createdBy;
	private String createdDate;

	private String modifiedBy;
	private String modifiedDate;

	private String approvedBy;
	private String approvedDate;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurDisplayName() {
		return curDisplayName;
	}

	public void setCurDisplayName(String curDisplayName) {
		this.curDisplayName = curDisplayName;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getCountryDisplayName() {
		return countryDisplayName;
	}

	public void setCountryDisplayName(String countryDisplayName) {
		this.countryDisplayName = countryDisplayName;
	}

	public Map<BigDecimal, String> getBankIdQuoteMap() {
		return bankIdQuoteMap;
	}

	public void setBankIdQuoteMap(Map<BigDecimal, String> bankIdQuoteMap) {
		this.bankIdQuoteMap = bankIdQuoteMap;
	}

	public Map<BigDecimal, String> getServiceIdNameMap() {
		return serviceIdNameMap;
	}

	public void setServiceIdNameMap(Map<BigDecimal, String> serviceIdNameMap) {
		this.serviceIdNameMap = serviceIdNameMap;
	}

	public Map<BigDecimal, String> getBranchGroupIdNameMap() {
		return branchGroupIdNameMap;
	}

	public void setBranchGroupIdNameMap(Map<BigDecimal, String> branchGroupIdNameMap) {
		this.branchGroupIdNameMap = branchGroupIdNameMap;
	}

	public Map<BigDecimal, String> getcBranchIdNameMap() {
		return cBranchIdNameMap;
	}

	public void setcBranchIdNameMap(Map<BigDecimal, String> cBranchIdNameMap) {
		this.cBranchIdNameMap = cBranchIdNameMap;
	}

	public BigDecimal getSellExchangeRate() {
		return sellExchangeRate;
	}

	public void setSellExchangeRate(BigDecimal sellExchangeRate) {
		this.sellExchangeRate = sellExchangeRate;
	}

	public BigDecimal getBuyExchangeRate() {
		return buyExchangeRate;
	}

	public void setBuyExchangeRate(BigDecimal buyExchangeRate) {
		this.buyExchangeRate = buyExchangeRate;
	}

	public IS_ACTIVE getIsActive() {
		return isActive;
	}

	public void setIsActive(IS_ACTIVE isActive) {
		this.isActive = isActive;
	}

	public RATE_UPLOAD_STATUS getStatus() {
		return status;
	}

	public void setStatus(RATE_UPLOAD_STATUS status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

}
