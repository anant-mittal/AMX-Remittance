package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

public class BankMasterDTO extends AbstractModel {

	private BigDecimal bankId;
	private String bankCode;
	private String bankFullName;
	private String bankShortName;
	private BigDecimal bankCountryId;

	@Override
	public String getModelType() {
		return "bankmaster";
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}

	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}

}
