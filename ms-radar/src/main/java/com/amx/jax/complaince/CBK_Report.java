package com.amx.jax.complaince;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "V_CBK_REPORT")
public class CBK_Report implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal docNo;
	
	private BigDecimal docFyr;

	private String transactionRefNo;

	private String transmodeCode;

	private BigDecimal amountLocal;

	private BigDecimal foreignAmount;

	private BigDecimal foreignExchangerate;

	private String custGender;

	private String fundsCode;

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

	private String custIdentificationType;

	private String custIdentityNumber;

	private String custIdIssueCountry;

	private String fromCountry;

	// private String toFundscode;

	private String beneFirstName;

	private String beneLastName;

	private String beneNationality;

	private String benePhoneContacttype;

	private String benePhoneCommunicationType;

	private String benePhoneNumber;

	private String beneAddressType;

	private String beneAddress;

	private String beneCity;

	private String beneCountryCode;

	private String toCountry;

//	private String indicator;

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

	private String trnxLocal;

	private String trnxDate;

	private String teller;

	private String authorized;
	
	private BigDecimal remitTrnxId;

	@Column(name = "TRANSACTION_NUMBER")
	public String getTransactionRefNo() {
		return transactionRefNo;
	}

	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}

	@Column(name = "TRANSMODE_CODE")
	public String getTransmodeCode() {
		return transmodeCode;
	}

	public void setTransmodeCode(String transmodeCode) {
		this.transmodeCode = transmodeCode;
	}

	@Column(name = "AMOUNT_LOCAL")
	public BigDecimal getAmountLocal() {
		return amountLocal;
	}

	public void setAmountLocal(BigDecimal amountLocal) {
		this.amountLocal = amountLocal;
	}

	@Column(name = "FOREIGN_AMOUNT")
	public BigDecimal getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(BigDecimal foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	@Column(name = "FOREIGN_EXCHANGE_RATE")
	public BigDecimal getForeignExchangerate() {
		return foreignExchangerate;
	}

	public void setForeignExchangerate(BigDecimal foreignExchangerate) {
		this.foreignExchangerate = foreignExchangerate;
	}

	@Column(name = "CUST_GENDER")
	public String getCustGender() {
		return custGender;
	}

	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}

	@Column(name = "FOREIGN_CURRENCY_CODE")
	public String getForeignCurrencycode() {
		return foreignCurrencycode;
	}

	@Column(name = "FUNDS_CODE")
	public String getFundsCode() {
		return fundsCode;
	}

	public void setFundsCode(String fundsCode) {
		this.fundsCode = fundsCode;
	}

	public void setForeignCurrencycode(String foreignCurrencycode) {
		this.foreignCurrencycode = foreignCurrencycode;
	}

	@Column(name = "CUST_TITLE")
	public String getCustTitle() {
		return custTitle;
	}

	public void setCustTitle(String custTitle) {
		this.custTitle = custTitle;
	}

	@Column(name = "CUST_FIRST_NAME")
	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	@Column(name = "CUST_LAST_NAME")
	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}

	@Column(name = "CUST_SSN")
	public String getCustSsn() {
		return custSsn;
	}

	public void setCustSsn(String custSsn) {
		this.custSsn = custSsn;
	}

	@Column(name = "CUST_NATIONALITY")
	public String getCustNationality() {
		return custNationality;
	}

	public void setCustNationality(String custNationality) {
		this.custNationality = custNationality;
	}

	@Column(name = "CUST_PH_CONTACT_TYPE")
	public String getCustPhContacttype() {
		return custPhContacttype;
	}

	public void setCustPhContacttype(String custPhContacttype) {
		this.custPhContacttype = custPhContacttype;
	}

	@Column(name = "CUST_PH_COMMUNICATION_TYPE")
	public String getCustPhCommunicationType() {
		return custPhCommunicationType;
	}

	public void setCustPhCommunicationType(String custPhCommunicationType) {
		this.custPhCommunicationType = custPhCommunicationType;
	}

	@Column(name = "CUST_PH_COUNTRY_PREFIX")
	public String getCustPhCountryPrefix() {
		return custPhCountryPrefix;
	}

	public void setCustPhCountryPrefix(String custPhCountryPrefix) {
		this.custPhCountryPrefix = custPhCountryPrefix;
	}

	@Column(name = "CUST_PH_NUMBER")
	public String getCustPhNo() {
		return custPhNo;
	}

	public void setCustPhNo(String custPhNo) {
		this.custPhNo = custPhNo;
	}

	@Column(name = "CUST_ADDRESS_TYPE")
	public String getCustAddressType() {
		return custAddressType;
	}

	public void setCustAddressType(String custAddressType) {
		this.custAddressType = custAddressType;
	}

	@Column(name = "CUST_ADDRESS")
	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	@Column(name = "CUST_CITY")
	public String getCustCity() {
		return custCity;
	}

	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}

	@Column(name = "CUST_COUNTRY_CODE")
	public String getCustCountryCode() {
		return custCountryCode;
	}

	public void setCustCountryCode(String custCountryCode) {
		this.custCountryCode = custCountryCode;
	}

	@Column(name = "CUST_IDENTIFICATION_TYPE")
	public String getCustIdentificationType() {
		return custIdentificationType;
	}

	public void setCustIdentificationType(String custIdentificationType) {
		this.custIdentificationType = custIdentificationType;
	}

	@Column(name = "CUST_IDENTITY_NUMBER")
	public String getCustIdentityNumber() {
		return custIdentityNumber;
	}

	public void setCustIdentityNumber(String custIdentityNumber) {
		this.custIdentityNumber = custIdentityNumber;
	}

	@Column(name = "CUST_ID_ISSUE_COUNTRY")
	public String getCustIdIssueCountry() {
		return custIdIssueCountry;
	}

	public void setCustIdIssueCountry(String custIdIssueCountry) {
		this.custIdIssueCountry = custIdIssueCountry;
	}

	@Column(name = "FROM_COUNTRY")
	public String getFromCountry() {
		return fromCountry;
	}

	public void setFromCountry(String fromCountry) {
		this.fromCountry = fromCountry;
	}

	@Column(name = "BENE_FIRST_NAME")
	public String getBeneFirstName() {
		return beneFirstName;
	}

	public void setBeneFirstName(String beneFirstName) {
		this.beneFirstName = beneFirstName;
	}

	@Column(name = "BENE_LAST_NAME")
	public String getBeneLastName() {
		return beneLastName;
	}

	public void setBeneLastName(String beneLastName) {
		this.beneLastName = beneLastName;
	}

	@Column(name = "BENE_NATIONALITY")
	public String getBeneNationality() {
		return beneNationality;
	}

	public void setBeneNationality(String beneNationality) {
		this.beneNationality = beneNationality;
	}

	@Column(name = "BENE_PHONE_CONTACT_TYPE")
	public String getBenePhoneContacttype() {
		return benePhoneContacttype;
	}

	public void setBenePhoneContacttype(String benePhoneContacttype) {
		this.benePhoneContacttype = benePhoneContacttype;
	}

	@Column(name = "BENE_PHONE_COMMUNICATION_TYPE")
	public String getBenePhoneCommunicationType() {
		return benePhoneCommunicationType;
	}

	public void setBenePhoneCommunicationType(String benePhoneCommunicationType) {
		this.benePhoneCommunicationType = benePhoneCommunicationType;
	}

	@Column(name = "BENE_PHONE_NUMBER")
	public String getBenePhoneNumber() {
		return benePhoneNumber;
	}

	public void setBenePhoneNumber(String benePhoneNumber) {
		this.benePhoneNumber = benePhoneNumber;
	}

	@Column(name = "BENE_ADDRESS_TYPE")
	public String getBeneAddressType() {
		return beneAddressType;
	}

	public void setBeneAddressType(String beneAddressType) {
		this.beneAddressType = beneAddressType;
	}

	@Column(name = "BENE_ADDRESS")
	public String getBeneAddress() {
		return beneAddress;
	}

	public void setBeneAddress(String beneAddress) {
		this.beneAddress = beneAddress;
	}

	@Column(name = "BENE_CITY")
	public String getBeneCity() {
		return beneCity;
	}

	public void setBeneCity(String beneCity) {
		this.beneCity = beneCity;
	}

	@Column(name = "BENE_COUNTRY_CODE")
	public String getBeneCountryCode() {
		return beneCountryCode;
	}

	public void setBeneCountryCode(String beneCountryCode) {
		this.beneCountryCode = beneCountryCode;
	}

	@Column(name = "TO_COUNTRY")
	public String getToCountry() {
		return toCountry;
	}

	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}

	@Column(name = "RENTITY_ID")
	public String getRentityId() {
		return rentityId;
	}

	public void setRentityId(String rentityId) {
		this.rentityId = rentityId;
	}

	@Column(name = "SUBMISSION_CODE")
	public String getSubmissionCode() {
		return submissionCode;
	}

	public void setSubmissionCode(String submissionCode) {
		this.submissionCode = submissionCode;
	}

	@Column(name = "REPORT_CODE")
	public String getReportcode() {
		return reportcode;
	}

	public void setReportcode(String reportcode) {
		this.reportcode = reportcode;
	}

	@Column(name = "SUBMISSION_DATE")
	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	@Column(name = "CURRENCY_CODE_LOCAL")
	public String getCurrencyCodeLocal() {
		return currencyCodeLocal;
	}

	public void setCurrencyCodeLocal(String currencyCodeLocal) {
		this.currencyCodeLocal = currencyCodeLocal;
	}

	@Column(name = "EMP_FULL_NAME")
	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	@Column(name = "EMP_PHONE_CONTACT_TYPE")
	public String getEmpPhcontacttype() {
		return empPhcontacttype;
	}

	public void setEmpPhcontacttype(String empPhcontacttype) {
		this.empPhcontacttype = empPhcontacttype;
	}

	@Column(name = "EMP_PHONE_COMMINICATION_TYPE")
	public String getEmpPhoneCommunicationtype() {
		return empPhoneCommunicationtype;
	}

	public void setEmpPhoneCommunicationtype(String empPhoneCommunicationtype) {
		this.empPhoneCommunicationtype = empPhoneCommunicationtype;
	}

	@Column(name = "EMP_PHONE_NUMBER")
	public String getEmpPhoneNo() {
		return empPhoneNo;
	}

	public void setEmpPhoneNo(String empPhoneNo) {
		this.empPhoneNo = empPhoneNo;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "EMP_ADDRESS_TYPE")
	public String getEmpaddresstype() {
		return empaddresstype;
	}

	public void setEmpaddresstype(String empaddresstype) {
		this.empaddresstype = empaddresstype;
	}

	@Column(name = "EMP_COUNTRY_CODE")
	public String getEmpcountrycode() {
		return empcountrycode;
	}

	public void setEmpcountrycode(String empcountrycode) {
		this.empcountrycode = empcountrycode;
	}

	@Column(name = "TRANSACTION_LOCATION")
	public String getTrnxLocal() {
		return trnxLocal;
	}

	public void setTrnxLocal(String trnxLocal) {
		this.trnxLocal = trnxLocal;
	}

	@Column(name = "DATE_TRANSACTION")
	public String getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(String trnxDate) {
		this.trnxDate = trnxDate;
	}

	@Column(name = "TELLER")
	public String getTeller() {
		return teller;
	}

	public void setTeller(String teller) {
		this.teller = teller;
	}

	@Column(name = "AUTHORIZED")
	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	@Id
	@Column(name = "REMITTANCE_TRANSACTION_ID")
	public BigDecimal getRemitTrnxId() {
		return remitTrnxId;
	}

	public void setRemitTrnxId(BigDecimal remitTrnxId) {
		this.remitTrnxId = remitTrnxId;
	}

	@Column(name = "DOCNO")
	public BigDecimal getDocNo() {
		return docNo;
	}

	public void setDocNo(BigDecimal docNo) {
		this.docNo = docNo;
	}

	@Column(name = "DOCFYR")
	public BigDecimal getDocFyr() {
		return docFyr;
	}

	public void setDocFyr(BigDecimal docFyr) {
		this.docFyr = docFyr;
	}
	
	

}
