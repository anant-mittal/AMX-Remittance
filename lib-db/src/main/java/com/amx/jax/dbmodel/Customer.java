package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;

import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.dict.Communicatable;
import com.amx.jax.util.AmxDBConstants.Status;

@Entity
@Table(name = "FS_CUSTOMER")
@Proxy(lazy = false)
public class Customer implements java.io.Serializable, Communicatable {

	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private BigDecimal companyId;
	private BigDecimal countryId;

	private BigDecimal languageId;

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
	// private Clob signatureSpecimenClob;

	private String signatureSpecimenClob;
	private String remarks;

	private ArticleDetails fsArticleDetails;
	private String sundryDebtorReference;
	private String deactivatedBy;
	private Date deactivatedDate;
	private String kioskPin;
	private BigDecimal nationalityId;
	private String isOnlineUser;
	private BigDecimal customerTypeId;
	private Date identityExpiredDate;

	/**
	 * Added following field for CR. DAILY_LIMIT WEEKLY_LIMIT MONTHLY_LIMIT
	 * QUARTERLY_LIMIT HALF_YEARLY_LIMIT ANNUAL_LIMIT
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
	private BigDecimal identityFor;
	private BigDecimal identityTypeId;

	// WhatsApp and Mobile Number related fields are add
	private String prefixCodeMobile;
	private BigDecimal mobileOther;
	private String prefixCodeMobileOther;
	private String isMobileWhatsApp;
	private String isMobileOtherWhatsApp;

	// income related fields are added

	private BigDecimal annualIncomeFrom;
	private BigDecimal annualIncomeTo;
	private String annualIncomeUpdatedBy;
	private Date annualIncomeUpdatedDate;
	private String isBusinessCardVerified;

	private String customerVatNumber;
	private String premInsurance;

	// annual transaction limit fields added

	private BigDecimal annualTransactionLimitFrom;
	private BigDecimal annualTransactionLimitTo;
	private Date annualTransactionUpdatedDate;

	@Column(name = "ANNUAL_TRNXLIMIT_FROM")
	public BigDecimal getAnnualTransactionLimitFrom() {
		return annualTransactionLimitFrom;
	}

	public void setAnnualTransactionLimitFrom(BigDecimal annualTransactionLimitFrom) {
		this.annualTransactionLimitFrom = annualTransactionLimitFrom;
	}

	@Column(name = "ANNUAL_TRNXLIMIT_TO")
	public BigDecimal getAnnualTransactionLimitTo() {
		return annualTransactionLimitTo;
	}

	public void setAnnualTransactionLimitTo(BigDecimal annualTransactionLimitTo) {
		this.annualTransactionLimitTo = annualTransactionLimitTo;
	}

	@Column(name = "ANNUAL_TRNXLIMIT_UPDATED_DATE")
	public Date getAnnualTransactionUpdatedDate() {
		return annualTransactionUpdatedDate;
	}

	public void setAnnualTransactionUpdatedDate(Date annualTransactionUpdatedDate) {
		this.annualTransactionUpdatedDate = annualTransactionUpdatedDate;
	}

	public String getIsBusinessCardVerified() {
		return isBusinessCardVerified;
	}

	public void setIsBusinessCardVerified(String isBusinessCardVerified) {
		this.isBusinessCardVerified = isBusinessCardVerified;
	}

	public BigDecimal getAnnualIncomeFrom() {
		return annualIncomeFrom;
	}

	public void setAnnualIncomeFrom(BigDecimal annualIncomeFrom) {
		this.annualIncomeFrom = annualIncomeFrom;
	}

	public BigDecimal getAnnualIncomeTo() {
		return annualIncomeTo;
	}

	public void setAnnualIncomeTo(BigDecimal annualIncomeTo) {
		this.annualIncomeTo = annualIncomeTo;
	}

	public String getAnnualIncomeUpdatedBy() {
		return annualIncomeUpdatedBy;
	}

	public void setAnnualIncomeUpdatedBy(String annualIncomeUpdatedBy) {
		this.annualIncomeUpdatedBy = annualIncomeUpdatedBy;
	}

	public Date getAnnualIncomeUpdatedDate() {
		return annualIncomeUpdatedDate;
	}

	public void setAnnualIncomeUpdatedDate(Date annualIncomeUpdatedDate) {
		this.annualIncomeUpdatedDate = annualIncomeUpdatedDate;
	}

	private IncomeRangeMaster fsIncomeRangeMaster;

	/* Registration Type added */
	private CustomerRegistrationType customerRegistrationType;

	public Customer() {
	}

	public Customer(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Id
	@GeneratedValue(generator = "fs_customer_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "fs_customer_seq", sequenceName = "FS_CUSTOMER_SEQ", allocationSize = 1)
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

	@Temporal(TemporalType.DATE)
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

	@Column(name = "SIGNATURE_SPECIMEN_CLOB")
	public String getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}

	public void setSignatureSpecimenClob(String signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "KIOSK_PIN")
	public String getKioskPin() {
		return kioskPin;
	}

	public void setKioskPin(String kioskPin) {
		this.kioskPin = kioskPin;
	}

	@Column(name = "DELETED_USER")
	public String getDeactivatedBy() {
		return deactivatedBy;
	}

	public void setDeactivatedBy(String deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}

	@Column(name = "DELETED_DATE")
	public Date getDeactivatedDate() {
		return deactivatedDate;
	}

	public void setDeactivatedDate(Date deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}

	@Column(name = "COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "CUSTOMER_REFERENCE")
	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	@Column(name = "IS_ONLINE_USER")
	public String getIsOnlineUser() {
		return isOnlineUser;
	}

	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}

	@Column(name = "IDENTITY_INT")
	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Column(name = "NATIONALITY")
	public BigDecimal getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "IDENTITY_EXPIRY_DATE")
	public Date getIdentityExpiredDate() {
		return identityExpiredDate;
	}

	public void setIdentityExpiredDate(Date identityExpiredDate) {
		this.identityExpiredDate = identityExpiredDate;
	}

	@Column(name = "CUSTOMER_TYPE_ID")
	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	@Column(name = "IDENTITY_FOR")
	public BigDecimal getIdentityFor() {
		return identityFor;
	}

	public void setIdentityFor(BigDecimal identityFor) {
		this.identityFor = identityFor;
	}

	@Column(name = "IDENTITY_TYPE_ID")
	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_DETAIL_ID")
	public ArticleDetails getFsArticleDetails() {
		return fsArticleDetails;
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

	@Column(name = "REGISTRATION_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CustomerRegistrationType getCustomerRegistrationType() {
		return customerRegistrationType;
	}

	public void setCustomerRegistrationType(CustomerRegistrationType customerRegistrationType) {
		this.customerRegistrationType = customerRegistrationType;
	}

	@Column(name = "PREFIX_CODE_MOBILE")
	public String getPrefixCodeMobile() {
		return prefixCodeMobile;
	}

	public void setPrefixCodeMobile(String prefixCodeMobile) {
		this.prefixCodeMobile = prefixCodeMobile;
	}

	@Column(name = "MOBILE_OTH")
	public BigDecimal getMobileOther() {
		return mobileOther;
	}

	public void setMobileOther(BigDecimal mobileOther) {
		this.mobileOther = mobileOther;
	}

	@Column(name = "PREFIX_CODE_MOBILE_OTH")
	public String getPrefixCodeMobileOther() {
		return prefixCodeMobileOther;
	}

	public void setPrefixCodeMobileOther(String prefixCodeMobileOther) {
		this.prefixCodeMobileOther = prefixCodeMobileOther;
	}

	@Column(name = "IS_MOBILE_WHATSAPP")
	public String getIsMobileWhatsApp() {
		return isMobileWhatsApp;
	}

	public void setIsMobileWhatsApp(String isMobileWhatsApp) {
		this.isMobileWhatsApp = isMobileWhatsApp;
	}

	@Column(name = "IS_MOBILE_OTH_WHATSAPP")
	public String getIsMobileOtherWhatsApp() {
		return isMobileOtherWhatsApp;
	}

	public void setIsMobileOtherWhatsApp(String isMobileOtherWhatsApp) {
		this.isMobileOtherWhatsApp = isMobileOtherWhatsApp;
	}

	@PrePersist
	public void prePersist() {
		this.lastUpdated = new Date();
	}

	private String whatsapp;

	@Column(name = "WHATSAPP_NO")
	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	private String whatsappPrefix;

	@Column(name = "WHATSAPP_PREFIX_CODE")
	public String getWhatsappPrefix() {
		return whatsappPrefix;
	}

	public void setWhatsappPrefix(String whatsappPrefix) {
		this.whatsappPrefix = whatsappPrefix;
	}

	private Status whatsAppVerified;

	@Column(name = "WHATSAPP_VERIFIED")
	@Enumerated(value = EnumType.STRING)
	public Status getWhatsAppVerified() {
		return whatsAppVerified;
	}

	public void setWhatsAppVerified(Status whatsAppVerified) {
		this.whatsAppVerified = whatsAppVerified;
	}

	public boolean canSendWhatsApp() {
		return !(Status.D.equals(this.whatsAppVerified) || Status.N.equals(this.whatsAppVerified));
	}

	private Status emailVerified;

	@Column(name = "EMAIL_VERIFIED")
	@Enumerated(value = EnumType.STRING)
	public Status getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Status emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean canSendEmail() {
		return !(Status.D.equals(this.emailVerified) || Status.N.equals(this.emailVerified));
	}

	private Status mobileVerified;

	@Column(name = "MOBILE_VERIFIED")
	@Enumerated(value = EnumType.STRING)
	public Status getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(Status mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public boolean canSendMobile() {
		return !(Status.D.equals(this.mobileVerified) || Status.N.equals(this.mobileVerified));
	}

	@Column(name = "VAT_NUMBER")
	public String getCustomerVatNumber() {
		return customerVatNumber;
	}

	@Column(name = "PREM_INSURANCE")
	public String getPremInsurance() {
		return premInsurance;
	}

	public void setPremInsurance(String premInsurance) {
		this.premInsurance = premInsurance;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", email=" + email +
				", emailVerified=" + emailVerified + ", mobileVerified="
				+ mobileVerified + "]";
	}

	public void setCustomerVatNumber(String customerVatNumber) {
		this.customerVatNumber = customerVatNumber;
	}

}
