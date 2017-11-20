package com.amx.jax.dbmodel;



import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*import com.amg.exchange.common.model.BizComponentData;
import com.amg.exchange.common.model.CompanyMaster;
import com.amg.exchange.common.model.CountryMaster;
import com.amg.exchange.common.model.LanguageType;
import com.amg.exchange.treasury.model.ArticleDetails;*/




/*******************************************************************************************************************

		 File		: Customer.java
 
		 Project	: AlmullaExchange

		 Package	: com.amg.exchange.model
 
		 Created	:	
 						Date	: 29-May-2014 5:03:37 pm
		 				By		: Justin Vincent
 						Revision:
 
 		 Last Change:
 						Date	: 29-JAN-2015 
 						By		: Nazish Ehsan Hashmi
		 				Revision:

		 Description: TODO 

********************************************************************************************************************/

@Entity
@Table(name = "FS_CUSTOMER" )
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private BigDecimal companyId;
	private BigDecimal countryId;
	

	//private LanguageType fsLanguageType;

	
	
	
	
	
	private String shortName;
	private String shortNameLocal;
	private String amlStatus;
	private BigDecimal numberOfHits;
	private String verificationBy;
	private Date verificationDate;
	private String amlStatusUpdatedBy;
	private Date amlStatusLastUpdated;
	private BigDecimal branchCode;
	private String activatedInd;
	private Date activatedDate;
	private Date inactivatedDate;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String titleLocal;
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String gender;
	private Date dateOfBirth;
	private String alterEmailId;
	private String mobile;
	private String signatureSpecimen;
	private String fingerPrintImg;
	private String introducedBy;
	private String medicalInsuranceInd;
	private String companyName;
	private String companyNameLocal;
	private String email;
	private String crNo;
	private String placeOfBirth;
	private String countryOfBirth;
	private String fatherName;
	private String createdBy;
	private String updatedBy;
	private Date creationDate;
	private Date lastUpdated;
	private String tokenKey;
	private String contactPerson;
	private BigDecimal contactNumber;
	private String isActive;
	private String amlRemarks;
	private String bcoRemarks;
	private String emosCustomer;
	private Date auditStatementDate;
	private BigDecimal auditGrossIncome;
	//private Clob signatureSpecimenClob;
	private String remarks; 
	
	//private ArticleDetails fsArticleDetails;
	private String sundryDebtorReference;
	private String deactivatedBy;
	private Date deactivatedDate;
	private String kioskPin;
	
	private String isOnlineUser;
	
	
	

	/**
	 * Added following field for CR.
	 * DAILY_LIMIT 
	 * WEEKLY_LIMIT 
	 * MONTHLY_LIMIT 
	 * QUARTERLY_LIMIT
	 * HALF_YEARLY_LIMIT
	 * ANNUAL_LIMIT
	 * 
	 **/
	
	private BigDecimal dailyLimit;
	private BigDecimal weeklyLimit;
	private BigDecimal quaterlyLimit;
	private BigDecimal montlyLimit;
	private BigDecimal halfYearly;
	private BigDecimal annualLimit;
	private String verificationTokenId;
	
	/* Loyalty Ponits added */
	
	private BigDecimal loyaltyPoints;
	private BigDecimal customerReference;
	private String smartCardIndicator;
	
	private Date introducedDate;
	private Date lastTransactionDate;
	private String pepsIndicator;
	private BigDecimal interComcode;
	private BigDecimal interTrnfyr;
	private BigDecimal interTrnref;
	private String identityInt;
	
	
	//private IncomeRangeMaster fsIncomeRangeMaster;
	

	public Customer() {
	}

	public Customer(BigDecimal customerId) {
		this.customerId = customerId;
	}

	

	@Id
	@GeneratedValue(generator="fs_customer_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_customer_seq" ,sequenceName="FS_CUSTOMER_SEQ",allocationSize=1)
	@Column(name = "CUSTOMER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	
	
	@Column(name = "SHORT_NAME", length = 200)
	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name = "SHORT_NAME_LOCAL", length = 200)
	public String getShortNameLocal() {
		return this.shortNameLocal;
	}

	public void setShortNameLocal(String shortNameLocal) {
		this.shortNameLocal = shortNameLocal;
	}

	@Column(name = "AML_STATUS", length = 15)
	public String getAmlStatus() {
		return this.amlStatus;
	}

	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}

	@Column(name = "NUMBER_OF_HITS", precision = 10, scale = 0)
	public BigDecimal getNumberOfHits() {
		return this.numberOfHits;
	}

	public void setNumberOfHits(BigDecimal numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	@Column(name = "VERIFICATION_BY", length = 30)
	public String getVerificationBy() {
		return this.verificationBy;
	}

	public void setVerificationBy(String verificationBy) {
		this.verificationBy = verificationBy;
	}

	@Column(name = "VERIFICATION_DATE")
	public Date getVerificationDate() {
		return this.verificationDate;
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	@Column(name = "AML_STATUS_UPDATED_BY", length = 20)
	public String getAmlStatusUpdatedBy() {
		return this.amlStatusUpdatedBy;
	}

	public void setAmlStatusUpdatedBy(String amlStatusUpdatedBy) {
		this.amlStatusUpdatedBy = amlStatusUpdatedBy;
	}

	@Column(name = "AML_STATUS_LAST_UPDATED")
	public Date getAmlStatusLastUpdated() {
		return this.amlStatusLastUpdated;
	}

	public void setAmlStatusLastUpdated(Date amlStatusLastUpdated) {
		this.amlStatusLastUpdated = amlStatusLastUpdated;
	}

@Column(name = "BRANCH_CODE", precision = 6, scale = 0)
	public BigDecimal getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(BigDecimal branchCode) {
		this.branchCode = branchCode;
	}

	@Column(name = "ACTIVATED_IND", length = 1)
	public String getActivatedInd() {
		return this.activatedInd;
	}

	public void setActivatedInd(String activatedInd) {
		this.activatedInd = activatedInd;
	}

	@Column(name = "ACTIVATED_DATE")
	public Date getActivatedDate() {
		return this.activatedDate;
	}

	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}

	@Column(name = "INACTIVATED_DATE")
	public Date getInactivatedDate() {
		return this.inactivatedDate;
	}

	public void setInactivatedDate(Date inactivatedDate) {
		this.inactivatedDate = inactivatedDate;
	}

	@Column(name = "TITLE", length = 10)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FIRST_NAME", length = 200)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MIDDLE_NAME", length = 200)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LAST_NAME", length = 200)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "TITLE_LOCAL", length = 200)
	public String getTitleLocal() {
		return this.titleLocal;
	}

	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}

	@Column(name = "FIRST_NAME_LOCAL", length = 200)
	public String getFirstNameLocal() {
		return this.firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	@Column(name = "MIDDLE_NAME_LOCAL", length = 200)
	public String getMiddleNameLocal() {
		return this.middleNameLocal;
	}

	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}

	@Column(name = "LAST_NAME_LOCAL", length = 200)
	public String getLastNameLocal() {
		return this.lastNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	@Column(name = "GENDER", length = 10)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "DATE_OF_BIRTH")
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	@Column(name = "ALTER_EMAIL_ID", length = 200)
	public String getAlterEmailId() {
		return this.alterEmailId;
	}

	public void setAlterEmailId(String alterEmailId) {
		this.alterEmailId = alterEmailId;
	}

	@Column(name = "MOBILE", length = 12)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "SIGNATURE_SPECIMEN", length = 4000)
	public String getSignatureSpecimen() {
		return this.signatureSpecimen;
	}

	public void setSignatureSpecimen(String signatureSpecimen) {
		this.signatureSpecimen = signatureSpecimen;
	}

	@Column(name = "FINGER_PRINT_IMG", length = 200)
	public String getFingerPrintImg() {
		return this.fingerPrintImg;
	}

	public void setFingerPrintImg(String fingerPrintImg) {
		this.fingerPrintImg = fingerPrintImg;
	}

	@Column(name = "INTRODUCED_BY", length = 100)
	public String getIntroducedBy() {
		return this.introducedBy;
	}

	public void setIntroducedBy(String introducedBy) {
		this.introducedBy = introducedBy;
	}

	@Column(name = "MEDICAL_INSURANCE_IND", length = 100)
	public String getMedicalInsuranceInd() {
		return this.medicalInsuranceInd;
	}

	public void setMedicalInsuranceInd(String medicalInsuranceInd) {
		this.medicalInsuranceInd = medicalInsuranceInd;
	}

	@Column(name = "COMPANY_NAME", length = 200)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "COMPANY_NAME_LOCAL", length = 200)
	public String getCompanyNameLocal() {
		return this.companyNameLocal;
	}

	public void setCompanyNameLocal(String companyNameLocal) {
		this.companyNameLocal = companyNameLocal;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CR_NO", length = 20)
	public String getCrNo() {
		return this.crNo;
	}

	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}

	@Column(name = "PLACE_OF_BIRTH", length = 100)
	public String getPlaceOfBirth() {
		return this.placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	@Column(name = "COUNTRY_OF_BIRTH", length = 100)
	public String getCountryOfBirth() {
		return this.countryOfBirth;
	}

	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	@Column(name = "FATHER_NAME", length = 200)
	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	@Column(name = "CREATED_BY", length = 30)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", length = 30)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "CREATION_DATE")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "LAST_UPDATED")
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
	
	@Column(name = "TOKEN_KEY", length = 50)
	public String getTokenKey() {
		return this.tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
	
	@Column(name = "CONTACT_PERSON", length = 50)
	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	@Column(name = "CONTACT_NUMBER", precision = 22, scale = 0)
	public BigDecimal getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(BigDecimal contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	@Column(name = "DAILY_TRANSACTION_LIMIT")
	public BigDecimal getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(BigDecimal dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	@Column(name = "WEEKLY_TRANSACTION_LIMIT")
	public BigDecimal getWeeklyLimit() {
		return weeklyLimit;
	}

	public void setWeeklyLimit(BigDecimal weeklyLimit) {
		this.weeklyLimit = weeklyLimit;
	}
	@Column(name = "QUARTERLY_TRANSACTION_LIMIT")
	public BigDecimal getQuaterlyLimit() {
		return quaterlyLimit;
	}
	public void setQuaterlyLimit(BigDecimal quaterlyLimit) {
		this.quaterlyLimit = quaterlyLimit;
	}
	@Column(name = "MONTHLY_TRANSACTION_LIMIT")
	public BigDecimal getMontlyLimit() {
		return montlyLimit;
	}

	public void setMontlyLimit(BigDecimal montlyLimit) {
		this.montlyLimit = montlyLimit;
	}
	@Column(name = "HALF_YEARLY_TRANSACTION_LIMIT")
	public BigDecimal getHalfYearly() {
		return halfYearly;
	}

	public void setHalfYearly(BigDecimal halfYearly) {
		this.halfYearly = halfYearly;
	}
	@Column(name = "ANNUAL_TRANSACTION_LIMIT")
	public BigDecimal getAnnualLimit() {
		return annualLimit;
	}

	public void setAnnualLimit(BigDecimal annualLimit) {
		this.annualLimit = annualLimit;
	}
	
	@Column(name = "VERIFICATION_TOKEN")
	public String getVerificationTokenId() {
		return verificationTokenId;
	}

	public void setVerificationTokenId(String verificationTokenId) {
		this.verificationTokenId = verificationTokenId;
	}

	
	@Column(name = "LOYALTY_POINTS")
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	

	@Column(name = "SMART_CARD_INDICATOR")
	public String getSmartCardIndicator() {
		return smartCardIndicator;
	}

	public void setSmartCardIndicator(String smartCardIndicator) {
		this.smartCardIndicator = smartCardIndicator;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "INTRODUCED_DATE")
	public Date getIntroducedDate() {
		return introducedDate;
	}

	public void setIntroducedDate(Date introducedDate) {
		this.introducedDate = introducedDate;
	}
	
	@Column(name = "LAST_TRANSACTION_DATE")
	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	@Column(name = "PEPS_INDICATOR")
	public String getPepsIndicator() {
		return pepsIndicator;
	}

	public void setPepsIndicator(String pepsIndicator) {
		this.pepsIndicator = pepsIndicator;
	}

	@Column(name = "INTR_COMCOD", length = 2)
	public BigDecimal getInterComcode() {
		return interComcode;
	}

	public void setInterComcode(BigDecimal interComcode) {
		this.interComcode = interComcode;
	}

	@Column(name = "INTR_TRNFYR", length = 4)
	public BigDecimal getInterTrnfyr() {
		return interTrnfyr;
	}

	public void setInterTrnfyr(BigDecimal interTrnfyr) {
		this.interTrnfyr = interTrnfyr;
	}

	@Column(name = "INTR_TRNREF", length = 8)
	public BigDecimal getInterTrnref() {
		return interTrnref;
	}

	public void setInterTrnref(BigDecimal interTrnref) {
		this.interTrnref = interTrnref;
	}

	@Column(name = "SUNDRY_DEPTOR_REFERENCE")
	public String getSundryDebtorReference() {
		return sundryDebtorReference;
	}

	public void setSundryDebtorReference(String sundryDebtorReference) {
		this.sundryDebtorReference = sundryDebtorReference;
	}
	
	@Column(name = "AML_REMARKS")
	public String getAmlRemarks() {
		return amlRemarks;
	}

	public void setAmlRemarks(String amlRemarks) {
		this.amlRemarks = amlRemarks;
	}
	
	@Column(name = "BCO_REMARKS")
	public String getBcoRemarks() {
		return bcoRemarks;
	}

	public void setBcoRemarks(String bcoRemarks) {
		this.bcoRemarks = bcoRemarks;
	}

	@Column(name = "EMOS_CUST")
	public String getEmosCustomer() {
		return emosCustomer;
	}

	public void setEmosCustomer(String emosCustomer) {
		this.emosCustomer = emosCustomer;
	}

	@Column(name = "AUDIT_STATEMENT_DT")
	public Date getAuditStatementDate() {
		return auditStatementDate;
	}

	public void setAuditStatementDate(Date auditStatementDate) {
		this.auditStatementDate = auditStatementDate;
	}
	

	@Column(name = "AUDIT_GROSS_INCOME")
	public BigDecimal getAuditGrossIncome() {
		return auditGrossIncome;
	}

	public void setAuditGrossIncome(BigDecimal auditGrossIncome) {
		this.auditGrossIncome = auditGrossIncome;
	}

	/*@Column(name = "SIGNATURE_SPECIMEN_CLOB")
	public Clob getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}

	public void setSignatureSpecimenClob(Clob signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}*/
	
	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="KIOSK_PIN")
	public String getKioskPin() {
		return kioskPin;
	}

	public void setKioskPin(String kioskPin) {
		this.kioskPin = kioskPin;
	}
	@Column(name="DELETED_USER")
	public String getDeactivatedBy() {
		return deactivatedBy;
	}

	public void setDeactivatedBy(String deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}
	@Column(name="DELETED_DATE")
	public Date getDeactivatedDate() {
		return deactivatedDate;
	}

	public void setDeactivatedDate(Date deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}

	@Column(name="COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
    
	@Column(name="CUSTOMER_REFERENCE")
	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	@Column(name="IS_ONLINE_USER")
	public String getIsOnlineUser() {
		return isOnlineUser;
	}

	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}

	@Column(name="IDENTITY_INT")
	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_DETAIL_ID")
	public ArticleDetails getFsArticleDetails() {
		return this.fsArticleDetails;
	}

	public void setFsArticleDetails(ArticleDetails fsArticleDetails) {
		this.fsArticleDetails = fsArticleDetails;
	}
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INCOME_RANGE_ID")
	public IncomeRangeMaster getFsIncomeRangeMaster() {
		return fsIncomeRangeMaster;
	}

	public void setFsIncomeRangeMaster(IncomeRangeMaster fsIncomeRangeMaster) {
		this.fsIncomeRangeMaster = fsIncomeRangeMaster;
	}
	*/
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID")
	public LanguageType getFsLanguageType() {
		return this.fsLanguageType;
	}

	public void setFsLanguageType(LanguageType fsLanguageType) {
		this.fsLanguageType = fsLanguageType;
	}*/

	
	
}
