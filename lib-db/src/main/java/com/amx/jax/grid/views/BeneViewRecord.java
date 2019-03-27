package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

//@Entity
//@Table(name = "VW_KIBANA_BRANCH")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeneViewRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BENE_BANK_ID")
	private BigDecimal bankId;

	@Column(name = "BENE_BANK_NAME")
	private String bankName;

	@Column(name = "BENE_BANKBRANCH_ID")
	private BigDecimal branchId;

	@Column(name = "BENE_BANKBRANCH_NAME")
	private String branchName;

	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal countryId;

	@Column(name = "BENE_COUNTRY_CODE")
	private String countryCode;

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
