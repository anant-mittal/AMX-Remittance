package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class AccountTypeDto {
	
	String flexiFiled;
	String amiecCode;
	String amiecDesc;
	BigDecimal srNo;
	BigDecimal countryId;
	BigDecimal additionalBankRuleId;
	BigDecimal additionalAmiecId;
	String modelType;
	BigDecimal bankAccountTypeId;
	
	
	public String getModelType() {
		return "accountType";
	}
	
	public String getFlexiFiled() {
		return flexiFiled;
	}
	public void setFlexiFiled(String flexiFiled) {
		this.flexiFiled = flexiFiled;
	}
	public String getAmiecCode() {
		return amiecCode;
	}
	public void setAmiecCode(String amiecCode) {
		this.amiecCode = amiecCode;
	}
	public String getAmiecDesc() {
		return amiecDesc;
	}
	public void setAmiecDesc(String amiecDesc) {
		this.amiecDesc = amiecDesc;
	}
	public BigDecimal getSrNo() {
		return srNo;
	}
	public void setSrNo(BigDecimal srNo) {
		this.srNo = srNo;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getAdditionalBankRuleId() {
		return additionalBankRuleId;
	}
	public void setAdditionalBankRuleId(BigDecimal additionalBankRuleId) {
		this.additionalBankRuleId = additionalBankRuleId;
	}
	public BigDecimal getAdditionalAmiecId() {
		return additionalAmiecId;
	}
	public void setAdditionalAmiecId(BigDecimal additionalAmiecId) {
		this.additionalAmiecId = additionalAmiecId;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public BigDecimal getBankAccountTypeId() {
		return bankAccountTypeId;
	}

	public void setBankAccountTypeId(BigDecimal bankAccountTypeId) {
		this.bankAccountTypeId = bankAccountTypeId;
	}

}
