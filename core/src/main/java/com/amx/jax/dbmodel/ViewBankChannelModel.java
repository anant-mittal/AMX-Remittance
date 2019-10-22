package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_EX_CHANNEL_BANKS")
public class ViewBankChannelModel {

	private BigDecimal rowNo;
	private BigDecimal bankId;
	private String bankCode;
	private String bankFullName;
	private BigDecimal bankCountryId;
	private String bankShortName;
	private String ibanFlag;
	private String localName;
	private String languageInd;

	@Id
	@Column(name = "ROW_ID")
	public BigDecimal getRowNo() {
		return rowNo;
	}

	public void setRowNo(BigDecimal rowNo) {
		this.rowNo = rowNo;
	}

	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "BANK_FULL_NAME")
	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	@Column(name = "BANK_COUNTRY_ID")
	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}

	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}

	@Column(name = "BANK_SHORT_NAME")
	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	@Column(name = "IBAN_FLAG")
	public String getIbanFlag() {
		return ibanFlag;
	}

	public void setIbanFlag(String ibanFlag) {
		this.ibanFlag = ibanFlag;
	}

	@Column(name = "BANK_LOCAL_NAME")
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	@Column(name = "LANGUAGE_IND")
	public String getLanguageInd() {
		return this.languageInd;
	}

	public void setLanguageInd(String languageInd) {
		this.languageInd = languageInd;
	}
}
