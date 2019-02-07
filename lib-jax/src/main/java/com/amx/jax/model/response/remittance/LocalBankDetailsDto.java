package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class LocalBankDetailsDto {
	
	private BigDecimal rowId;
	private BigDecimal applicationCountryId;
	private BigDecimal chequeBankId;
	private String chequeBankCode;
	private String bankFullName;
	private String bankShortName;
	private String bankFullNameAr;
	private String bankShortNameAr;
	private String checkGlNumber;
	private String knetGlNo;
	private BigDecimal debitCardLength;
	
	public BigDecimal getRowId() {
		return rowId;
	}
	public void setRowId(BigDecimal rowId) {
		this.rowId = rowId;
	}
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	public BigDecimal getChequeBankId() {
		return chequeBankId;
	}
	public void setChequeBankId(BigDecimal chequeBankId) {
		this.chequeBankId = chequeBankId;
	}
	
	public String getChequeBankCode() {
		return chequeBankCode;
	}
	public void setChequeBankCode(String chequeBankCode) {
		this.chequeBankCode = chequeBankCode;
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
	
	public String getBankFullNameAr() {
		return bankFullNameAr;
	}
	public void setBankFullNameAr(String bankFullNameAr) {
		this.bankFullNameAr = bankFullNameAr;
	}
	
	public String getBankShortNameAr() {
		return bankShortNameAr;
	}
	public void setBankShortNameAr(String bankShortNameAr) {
		this.bankShortNameAr = bankShortNameAr;
	}
	
	public String getCheckGlNumber() {
		return checkGlNumber;
	}
	public void setCheckGlNumber(String checkGlNumber) {
		this.checkGlNumber = checkGlNumber;
	}
	
	public String getKnetGlNo() {
		return knetGlNo;
	}
	public void setKnetGlNo(String knetGlNo) {
		this.knetGlNo = knetGlNo;
	}
	
	public BigDecimal getDebitCardLength() {
		return debitCardLength;
	}
	public void setDebitCardLength(BigDecimal debitCardLength) {
		this.debitCardLength = debitCardLength;
	}
	
}
