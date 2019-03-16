package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal bankId;

	private String bankCode;

	private String bankFullName;

	private String bankShortName;

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

}
