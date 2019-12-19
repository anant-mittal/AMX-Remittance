package com.amx.jax.complaince;

import java.io.Serializable;
import java.math.BigDecimal;

public class CbkReportDto implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private BigDecimal transactionRefNo;
	
	private BigDecimal transmodeCode;
	
	private BigDecimal amountLocal;
	
	private BigDecimal foreignAmount;
	
	private BigDecimal foreignExchangerate;
	
	private String custGender;
	
	private String fromFundsCode;
	
	private String foreignCurrencycode;
	
	private String custTitle;
	
	private String custFirstName;
	
	private String custLastName;
	
	private String custSsn;
	
	private String custNationality;
	
	private String custPhContacttype;
	
	private String custPhCommunicationType;
	
	private String custPhCountryPrefix;
	
	private String custPhNo;
	
	private String custAddressType;
	
	private String custAddress;
	
	private String custCity;
	
	private String custCountryCode;
	
	private BigDecimal custIdentificationType;
	
	private String custIdentityNumber;
	
	private String custIdIssueCountry;
	
	private BigDecimal fromCountry;
	
	private String toFundscode;
	
	private String beneFirstName;
	
	private String beneLastName;
	
	private String beneNationality;
	
	private String benePhoneContacttype;
	
	private String benePhoneCommunicationType;
	
	private String benePhoneNumber;
	
	private String beneAddressType;
	
	private String beneAddress;
	
	private String beneCity;
	
	private BigDecimal beneCountryCode;
	
	private BigDecimal toCountry;
	
	private String indicator;
	
	private String rentityId;
	
	private String submissionCode;
	
	private String reportcode;
	
	private String submissionDate;
	
	private String currencyCodeLocal;
	
	private String empFullName;
	
	private String empPhcontacttype;
	
	private String empPhoneCommunicationtype;
	
	private String empPhoneNo;
	
	private String email;
	
	private String empaddresstype;
	
	private String empcountrycode;
	
	private String reason;
	
	private String action;
	
	private String trnxLocal;
	
	private String trnxDate;
	
	private String teller;
	
	private String authorized;

	public BigDecimal getTransactionRefNo() {
		return transactionRefNo;
	}

	public void setTransactionRefNo(BigDecimal transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}

	public BigDecimal getTransmodeCode() {
		return transmodeCode;
	}

	public void setTransmodeCode(BigDecimal transmodeCode) {
		this.transmodeCode = transmodeCode;
	}

	public BigDecimal getAmountLocal() {
		return amountLocal;
	}

	public void setAmountLocal(BigDecimal amountLocal) {
		this.amountLocal = amountLocal;
	}

	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public BigDecimal getForeignExchangerate() {
		return foreignExchangerate;
	}

	public void setForeignExchangerate(BigDecimal foreignExchangerate) {
		this.foreignExchangerate = foreignExchangerate;
	}

	public String getCustGender() {
		return custGender;
	}

	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}

	public String getFromFundsCode() {
		return fromFundsCode;
	}

	public void setFromFundsCode(String fromFundsCode) {
		this.fromFundsCode = fromFundsCode;
	}

	public String getForeignCurrencycode() {
		return foreignCurrencycode;
	}

	public void setForeignCurrencycode(String foreignCurrencycode) {
		this.foreignCurrencycode = foreignCurrencycode;
	}

	public String getCustTitle() {
		return custTitle;
	}

	public void setCustTitle(String custTitle) {
		this.custTitle = custTitle;
	}

	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}

	public String getCustSsn() {
		return custSsn;
	}

	public void setCustSsn(String custSsn) {
		this.custSsn = custSsn;
	}

	public String getCustNationality() {
		return custNationality;
	}

	public void setCustNationality(String custNationality) {
		this.custNationality = custNationality;
	}

	public String getCustPhContacttype() {
		return custPhContacttype;
	}

	public void setCustPhContacttype(String custPhContacttype) {
		this.custPhContacttype = custPhContacttype;
	}

	public String getCustPhCommunicationType() {
		return custPhCommunicationType;
	}

	public void setCustPhCommunicationType(String custPhCommunicationType) {
		this.custPhCommunicationType = custPhCommunicationType;
	}

	public String getCustPhCountryPrefix() {
		return custPhCountryPrefix;
	}

	public void setCustPhCountryPrefix(String custPhCountryPrefix) {
		this.custPhCountryPrefix = custPhCountryPrefix;
	}

	public String getCustPhNo() {
		return custPhNo;
	}

	public void setCustPhNo(String custPhNo) {
		this.custPhNo = custPhNo;
	}

	public String getCustAddressType() {
		return custAddressType;
	}

	public void setCustAddressType(String custAddressType) {
		this.custAddressType = custAddressType;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public String getCustCity() {
		return custCity;
	}

	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}

	public String getCustCountryCode() {
		return custCountryCode;
	}

	public void setCustCountryCode(String custCountryCode) {
		this.custCountryCode = custCountryCode;
	}

	public BigDecimal getCustIdentificationType() {
		return custIdentificationType;
	}

	public void setCustIdentificationType(BigDecimal custIdentificationType) {
		this.custIdentificationType = custIdentificationType;
	}

	public String getCustIdentityNumber() {
		return custIdentityNumber;
	}

	public void setCustIdentityNumber(String custIdentityNumber) {
		this.custIdentityNumber = custIdentityNumber;
	}

	public String getCustIdIssueCountry() {
		return custIdIssueCountry;
	}

	public void setCustIdIssueCountry(String custIdIssueCountry) {
		this.custIdIssueCountry = custIdIssueCountry;
	}

	public BigDecimal getFromCountry() {
		return fromCountry;
	}

	public void setFromCountry(BigDecimal fromCountry) {
		this.fromCountry = fromCountry;
	}

	public String getToFundscode() {
		return toFundscode;
	}

	public void setToFundscode(String toFundscode) {
		this.toFundscode = toFundscode;
	}

	public String getBeneFirstName() {
		return beneFirstName;
	}

	public void setBeneFirstName(String beneFirstName) {
		this.beneFirstName = beneFirstName;
	}

	public String getBeneLastName() {
		return beneLastName;
	}

	public void setBeneLastName(String beneLastName) {
		this.beneLastName = beneLastName;
	}

	public String getBeneNationality() {
		return beneNationality;
	}

	public void setBeneNationality(String beneNationality) {
		this.beneNationality = beneNationality;
	}

	public String getBenePhoneContacttype() {
		return benePhoneContacttype;
	}

	public void setBenePhoneContacttype(String benePhoneContacttype) {
		this.benePhoneContacttype = benePhoneContacttype;
	}

	public String getBenePhoneCommunicationType() {
		return benePhoneCommunicationType;
	}

	public void setBenePhoneCommunicationType(String benePhoneCommunicationType) {
		this.benePhoneCommunicationType = benePhoneCommunicationType;
	}

	public String getBenePhoneNumber() {
		return benePhoneNumber;
	}

	public void setBenePhoneNumber(String benePhoneNumber) {
		this.benePhoneNumber = benePhoneNumber;
	}

	public String getBeneAddressType() {
		return beneAddressType;
	}

	public void setBeneAddressType(String beneAddressType) {
		this.beneAddressType = beneAddressType;
	}

	public String getBeneAddress() {
		return beneAddress;
	}

	public void setBeneAddress(String beneAddress) {
		this.beneAddress = beneAddress;
	}

	public String getBeneCity() {
		return beneCity;
	}

	public void setBeneCity(String beneCity) {
		this.beneCity = beneCity;
	}

	public BigDecimal getBeneCountryCode() {
		return beneCountryCode;
	}

	public void setBeneCountryCode(BigDecimal beneCountryCode) {
		this.beneCountryCode = beneCountryCode;
	}

	public BigDecimal getToCountry() {
		return toCountry;
	}

	public void setToCountry(BigDecimal toCountry) {
		this.toCountry = toCountry;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getRentityId() {
		return rentityId;
	}

	public void setRentityId(String rentityId) {
		this.rentityId = rentityId;
	}

	public String getSubmissionCode() {
		return submissionCode;
	}

	public void setSubmissionCode(String submissionCode) {
		this.submissionCode = submissionCode;
	}

	public String getReportcode() {
		return reportcode;
	}

	public void setReportcode(String reportcode) {
		this.reportcode = reportcode;
	}

	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getCurrencyCodeLocal() {
		return currencyCodeLocal;
	}

	public void setCurrencyCodeLocal(String currencyCodeLocal) {
		this.currencyCodeLocal = currencyCodeLocal;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public String getEmpPhcontacttype() {
		return empPhcontacttype;
	}

	public void setEmpPhcontacttype(String empPhcontacttype) {
		this.empPhcontacttype = empPhcontacttype;
	}

	public String getEmpPhoneCommunicationtype() {
		return empPhoneCommunicationtype;
	}

	public void setEmpPhoneCommunicationtype(String empPhoneCommunicationtype) {
		this.empPhoneCommunicationtype = empPhoneCommunicationtype;
	}

	public String getEmpPhoneNo() {
		return empPhoneNo;
	}

	public void setEmpPhoneNo(String empPhoneNo) {
		this.empPhoneNo = empPhoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmpaddresstype() {
		return empaddresstype;
	}

	public void setEmpaddresstype(String empaddresstype) {
		this.empaddresstype = empaddresstype;
	}

	public String getEmpcountrycode() {
		return empcountrycode;
	}

	public void setEmpcountrycode(String empcountrycode) {
		this.empcountrycode = empcountrycode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTrnxLocal() {
		return trnxLocal;
	}

	public void setTrnxLocal(String trnxLocal) {
		this.trnxLocal = trnxLocal;
	}

	public String getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(String trnxDate) {
		this.trnxDate = trnxDate;
	}

	public String getTeller() {
		return teller;
	}

	public void setTeller(String teller) {
		this.teller = teller;
	}

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}



	
	
}
