package com.amx.amxlib.meta.model;
import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.AbstractModel;


public class ApplicationSetupDTO  extends AbstractModel{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal applicationSetupId;
	private String faAccountNumber;
	private BigDecimal applicationCountryId;
	private BigDecimal companyId;
	private String emailIndicator;
	private String smsIndicator;
	private String scanIndicator;
	private BigDecimal headOfficeLocationBranchCode;
	private String insuranceIndicator;
	private BigDecimal idExpiryYears;
	private BigDecimal payableGLNumber;
	private String loyalityIndicator;
	private BigDecimal roundFactor;
	private String amlCheck;
	private String orsIndicator;

	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	
	private String emailHost;
	private String emailUserName;
	private String emailPassword;
	private BigDecimal emailPortNo;
	private String emailAliasName;
	private String emailId;
	private String onlineFeedbackEmail;
	private String onlineDomUrl;
	private String isoCode;
	private String amlSystem;
	private String amlCustomerSource;
	private String amlInstanciated;

	
	

	public BigDecimal getApplicationSetupId() {
		return applicationSetupId;
	}

	public void setApplicationSetupId(BigDecimal applicationSetupId) {
		this.applicationSetupId = applicationSetupId;
	}


	public String getFaAccountNumber() {
		return faAccountNumber;
	}

	public void setFaAccountNumber(String faAccountNumber) {
		this.faAccountNumber = faAccountNumber;
	}

	

	public String getEmailIndicator() {
		return emailIndicator;
	}

	public void setEmailIndicator(String emailIndicator) {
		this.emailIndicator = emailIndicator;
	}


	public String getSmsIndicator() {
		return smsIndicator;
	}

	public void setSmsIndicator(String smsIndicator) {
		this.smsIndicator = smsIndicator;
	}


	public String getScanIndicator() {
		return scanIndicator;
	}

	public void setScanIndicator(String scanIndicator) {
		this.scanIndicator = scanIndicator;
	}

	public BigDecimal getHeadOfficeLocationBranchCode() {
		return headOfficeLocationBranchCode;
	}

	public void setHeadOfficeLocationBranchCode(
			BigDecimal headOfficeLocationBranchCode) {
		this.headOfficeLocationBranchCode = headOfficeLocationBranchCode;
	}

	public String getInsuranceIndicator() {
		return insuranceIndicator;
	}

	public void setInsuranceIndicator(String insuranceIndicator) {
		this.insuranceIndicator = insuranceIndicator;
	}

	public BigDecimal getIdExpiryYears() {
		return idExpiryYears;
	}

	public void setIdExpiryYears(BigDecimal idExpiryYears) {
		this.idExpiryYears = idExpiryYears;
	}


	public BigDecimal getPayableGLNumber() {
		return payableGLNumber;
	}

	public void setPayableGLNumber(BigDecimal payableGLNumber) {
		this.payableGLNumber = payableGLNumber;
	}


	public String getLoyalityIndicator() {
		return loyalityIndicator;
	}

	public void setLoyalityIndicator(String loyalityIndicator) {
		this.loyalityIndicator = loyalityIndicator;
	}


	public BigDecimal getRoundFactor() {
		return roundFactor;
	}

	public void setRoundFactor(BigDecimal roundFactor) {
		this.roundFactor = roundFactor;
	}


	public String getAmlCheck() {
		return amlCheck;
	}

	public void setAmlCheck(String amlCheck) {
		this.amlCheck = amlCheck;
	}


	public String getOrsIndicator() {
		return orsIndicator;
	}

	public void setOrsIndicator(String orsIndicator) {
		this.orsIndicator = orsIndicator;
	}


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}
	

	public String getEmailUserName() {
		return emailUserName;
	}

	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public BigDecimal getEmailPortNo() {
		return emailPortNo;
	}

	public void setEmailPortNo(BigDecimal emailPortNo) {
		this.emailPortNo = emailPortNo;
	}

	public String getEmailAliasName() {
		return emailAliasName;
	}

	public void setEmailAliasName(String emailAliasName) {
		this.emailAliasName = emailAliasName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOnlineFeedbackEmail() {
		return onlineFeedbackEmail;
	}

	public void setOnlineFeedbackEmail(String onlineFeedbackEmail) {
		this.onlineFeedbackEmail = onlineFeedbackEmail;
	}


	public String getOnlineDomUrl() {
		return onlineDomUrl;
	}

	public void setOnlineDomUrl(String onlineDomUrl) {
		this.onlineDomUrl = onlineDomUrl;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	@Override
	public String getModelType() {
		return null;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getAmlSystem() {
		return amlSystem;
	}

	public void setAmlSystem(String amlSystem) {
		this.amlSystem = amlSystem;
	}

	public String getAmlCustomerSource() {
		return amlCustomerSource;
	}

	public void setAmlCustomerSource(String amlCustomerSource) {
		this.amlCustomerSource = amlCustomerSource;
	}

	public String getAmlInstanciated() {
		return amlInstanciated;
	}

	public void setAmlInstanciated(String amlInstanciated) {
		this.amlInstanciated = amlInstanciated;
	}

	

	


	
}
