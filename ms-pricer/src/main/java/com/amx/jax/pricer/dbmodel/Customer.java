package com.amx.jax.pricer.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "FS_CUSTOMER")
@Proxy(lazy = false)
public class Customer implements java.io.Serializable {

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

	// Customer Category Discount
	private CustomerCategoryDiscount customerCategoryDiscount;

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
	@JoinColumn(name = "CUSTOMER_CATEGORY_ID")
	public CustomerCategoryDiscount getCustomerCategoryDiscount() {
		return customerCategoryDiscount;
	}

	public void setCustomerCategoryDiscount(CustomerCategoryDiscount customerCategoryDiscount) {
		this.customerCategoryDiscount = customerCategoryDiscount;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", companyId=" + companyId + ", countryId=" + countryId
				+ ", languageId=" + languageId + ", shortName=" + shortName + ", shortNameLocal=" + shortNameLocal
				+ ", amlStatus=" + amlStatus + ", numberOfHits=" + numberOfHits + ", verificationBy=" + verificationBy
				+ ", verificationDate=" + verificationDate + ", amlStatusUpdatedBy=" + amlStatusUpdatedBy
				+ ", amlStatusLastUpdated=" + amlStatusLastUpdated + ", branchCode=" + branchCode + ", activatedInd="
				+ activatedInd + ", activatedDate=" + activatedDate + ", inactivatedDate=" + inactivatedDate
				+ ", title=" + title + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", titleLocal=" + titleLocal + ", firstNameLocal=" + firstNameLocal + ", middleNameLocal="
				+ middleNameLocal + ", lastNameLocal=" + lastNameLocal + ", gender=" + gender + ", dateOfBirth="
				+ dateOfBirth + ", alterEmailId=" + alterEmailId + ", mobile=" + mobile + ", signatureSpecimen="
				+ signatureSpecimen + ", fingerPrintImg=" + fingerPrintImg + ", introducedBy=" + introducedBy
				+ ", medicalInsuranceInd=" + medicalInsuranceInd + ", companyName=" + companyName
				+ ", companyNameLocal=" + companyNameLocal + ", email=" + email + ", crNo=" + crNo + ", placeOfBirth="
				+ placeOfBirth + ", countryOfBirth=" + countryOfBirth + ", fatherName=" + fatherName + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", creationDate=" + creationDate + ", lastUpdated="
				+ lastUpdated + ", tokenKey=" + tokenKey + ", contactPerson=" + contactPerson + ", contactNumber="
				+ contactNumber + ", isActive=" + isActive + ", amlRemarks=" + amlRemarks + ", bcoRemarks=" + bcoRemarks
				+ ", emosCustomer=" + emosCustomer + ", auditStatementDate=" + auditStatementDate
				+ ", auditGrossIncome=" + auditGrossIncome + ", signatureSpecimenClob=" + signatureSpecimenClob
				+ ", remarks=" + remarks + ", sundryDebtorReference=" + sundryDebtorReference + ", deactivatedBy="
				+ deactivatedBy + ", deactivatedDate=" + deactivatedDate + ", kioskPin=" + kioskPin + ", nationalityId="
				+ nationalityId + ", isOnlineUser=" + isOnlineUser + ", customerTypeId=" + customerTypeId
				+ ", identityExpiredDate=" + identityExpiredDate + ", dailyLimit=" + dailyLimit + ", weeklyLimit="
				+ weeklyLimit + ", quaterlyLimit=" + quaterlyLimit + ", montlyLimit=" + montlyLimit + ", halfYearly="
				+ halfYearly + ", annualLimit=" + annualLimit + ", verificationTokenId=" + verificationTokenId
				+ ", loyaltyPoints=" + loyaltyPoints + ", customerReference=" + customerReference
				+ ", smartCardIndicator=" + smartCardIndicator + ", introducedDate=" + introducedDate
				+ ", lastTransactionDate=" + lastTransactionDate + ", pepsIndicator=" + pepsIndicator
				+ ", interComcode=" + interComcode + ", interTrnfyr=" + interTrnfyr + ", interTrnref=" + interTrnref
				+ ", identityInt=" + identityInt + ", identityFor=" + identityFor + ", identityTypeId=" + identityTypeId
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activatedDate == null) ? 0 : activatedDate.hashCode());
		result = prime * result + ((activatedInd == null) ? 0 : activatedInd.hashCode());
		result = prime * result + ((alterEmailId == null) ? 0 : alterEmailId.hashCode());
		result = prime * result + ((amlRemarks == null) ? 0 : amlRemarks.hashCode());
		result = prime * result + ((amlStatus == null) ? 0 : amlStatus.hashCode());
		result = prime * result + ((amlStatusLastUpdated == null) ? 0 : amlStatusLastUpdated.hashCode());
		result = prime * result + ((amlStatusUpdatedBy == null) ? 0 : amlStatusUpdatedBy.hashCode());
		result = prime * result + ((annualLimit == null) ? 0 : annualLimit.hashCode());
		result = prime * result + ((auditGrossIncome == null) ? 0 : auditGrossIncome.hashCode());
		result = prime * result + ((auditStatementDate == null) ? 0 : auditStatementDate.hashCode());
		result = prime * result + ((bcoRemarks == null) ? 0 : bcoRemarks.hashCode());
		result = prime * result + ((branchCode == null) ? 0 : branchCode.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((companyNameLocal == null) ? 0 : companyNameLocal.hashCode());
		result = prime * result + ((contactNumber == null) ? 0 : contactNumber.hashCode());
		result = prime * result + ((contactPerson == null) ? 0 : contactPerson.hashCode());
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((countryOfBirth == null) ? 0 : countryOfBirth.hashCode());
		result = prime * result + ((crNo == null) ? 0 : crNo.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerReference == null) ? 0 : customerReference.hashCode());
		result = prime * result + ((customerTypeId == null) ? 0 : customerTypeId.hashCode());
		result = prime * result + ((dailyLimit == null) ? 0 : dailyLimit.hashCode());
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((deactivatedBy == null) ? 0 : deactivatedBy.hashCode());
		result = prime * result + ((deactivatedDate == null) ? 0 : deactivatedDate.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((emosCustomer == null) ? 0 : emosCustomer.hashCode());
		result = prime * result + ((fatherName == null) ? 0 : fatherName.hashCode());
		result = prime * result + ((fingerPrintImg == null) ? 0 : fingerPrintImg.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((firstNameLocal == null) ? 0 : firstNameLocal.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((halfYearly == null) ? 0 : halfYearly.hashCode());
		result = prime * result + ((identityExpiredDate == null) ? 0 : identityExpiredDate.hashCode());
		result = prime * result + ((identityFor == null) ? 0 : identityFor.hashCode());
		result = prime * result + ((identityInt == null) ? 0 : identityInt.hashCode());
		result = prime * result + ((identityTypeId == null) ? 0 : identityTypeId.hashCode());
		result = prime * result + ((inactivatedDate == null) ? 0 : inactivatedDate.hashCode());
		result = prime * result + ((interComcode == null) ? 0 : interComcode.hashCode());
		result = prime * result + ((interTrnfyr == null) ? 0 : interTrnfyr.hashCode());
		result = prime * result + ((interTrnref == null) ? 0 : interTrnref.hashCode());
		result = prime * result + ((introducedBy == null) ? 0 : introducedBy.hashCode());
		result = prime * result + ((introducedDate == null) ? 0 : introducedDate.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((isOnlineUser == null) ? 0 : isOnlineUser.hashCode());
		result = prime * result + ((kioskPin == null) ? 0 : kioskPin.hashCode());
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((lastNameLocal == null) ? 0 : lastNameLocal.hashCode());
		result = prime * result + ((lastTransactionDate == null) ? 0 : lastTransactionDate.hashCode());
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((loyaltyPoints == null) ? 0 : loyaltyPoints.hashCode());
		result = prime * result + ((medicalInsuranceInd == null) ? 0 : medicalInsuranceInd.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((middleNameLocal == null) ? 0 : middleNameLocal.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((montlyLimit == null) ? 0 : montlyLimit.hashCode());
		result = prime * result + ((nationalityId == null) ? 0 : nationalityId.hashCode());
		result = prime * result + ((numberOfHits == null) ? 0 : numberOfHits.hashCode());
		result = prime * result + ((pepsIndicator == null) ? 0 : pepsIndicator.hashCode());
		result = prime * result + ((placeOfBirth == null) ? 0 : placeOfBirth.hashCode());
		result = prime * result + ((quaterlyLimit == null) ? 0 : quaterlyLimit.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((shortNameLocal == null) ? 0 : shortNameLocal.hashCode());
		result = prime * result + ((signatureSpecimen == null) ? 0 : signatureSpecimen.hashCode());
		result = prime * result + ((signatureSpecimenClob == null) ? 0 : signatureSpecimenClob.hashCode());
		result = prime * result + ((smartCardIndicator == null) ? 0 : smartCardIndicator.hashCode());
		result = prime * result + ((sundryDebtorReference == null) ? 0 : sundryDebtorReference.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titleLocal == null) ? 0 : titleLocal.hashCode());
		result = prime * result + ((tokenKey == null) ? 0 : tokenKey.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((verificationBy == null) ? 0 : verificationBy.hashCode());
		result = prime * result + ((verificationDate == null) ? 0 : verificationDate.hashCode());
		result = prime * result + ((verificationTokenId == null) ? 0 : verificationTokenId.hashCode());
		result = prime * result + ((weeklyLimit == null) ? 0 : weeklyLimit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (activatedDate == null) {
			if (other.activatedDate != null)
				return false;
		} else if (!activatedDate.equals(other.activatedDate))
			return false;
		if (activatedInd == null) {
			if (other.activatedInd != null)
				return false;
		} else if (!activatedInd.equals(other.activatedInd))
			return false;
		if (alterEmailId == null) {
			if (other.alterEmailId != null)
				return false;
		} else if (!alterEmailId.equals(other.alterEmailId))
			return false;
		if (amlRemarks == null) {
			if (other.amlRemarks != null)
				return false;
		} else if (!amlRemarks.equals(other.amlRemarks))
			return false;
		if (amlStatus == null) {
			if (other.amlStatus != null)
				return false;
		} else if (!amlStatus.equals(other.amlStatus))
			return false;
		if (amlStatusLastUpdated == null) {
			if (other.amlStatusLastUpdated != null)
				return false;
		} else if (!amlStatusLastUpdated.equals(other.amlStatusLastUpdated))
			return false;
		if (amlStatusUpdatedBy == null) {
			if (other.amlStatusUpdatedBy != null)
				return false;
		} else if (!amlStatusUpdatedBy.equals(other.amlStatusUpdatedBy))
			return false;
		if (annualLimit == null) {
			if (other.annualLimit != null)
				return false;
		} else if (!annualLimit.equals(other.annualLimit))
			return false;
		if (auditGrossIncome == null) {
			if (other.auditGrossIncome != null)
				return false;
		} else if (!auditGrossIncome.equals(other.auditGrossIncome))
			return false;
		if (auditStatementDate == null) {
			if (other.auditStatementDate != null)
				return false;
		} else if (!auditStatementDate.equals(other.auditStatementDate))
			return false;
		if (bcoRemarks == null) {
			if (other.bcoRemarks != null)
				return false;
		} else if (!bcoRemarks.equals(other.bcoRemarks))
			return false;
		if (branchCode == null) {
			if (other.branchCode != null)
				return false;
		} else if (!branchCode.equals(other.branchCode))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (companyNameLocal == null) {
			if (other.companyNameLocal != null)
				return false;
		} else if (!companyNameLocal.equals(other.companyNameLocal))
			return false;
		if (contactNumber == null) {
			if (other.contactNumber != null)
				return false;
		} else if (!contactNumber.equals(other.contactNumber))
			return false;
		if (contactPerson == null) {
			if (other.contactPerson != null)
				return false;
		} else if (!contactPerson.equals(other.contactPerson))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (countryOfBirth == null) {
			if (other.countryOfBirth != null)
				return false;
		} else if (!countryOfBirth.equals(other.countryOfBirth))
			return false;
		if (crNo == null) {
			if (other.crNo != null)
				return false;
		} else if (!crNo.equals(other.crNo))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerReference == null) {
			if (other.customerReference != null)
				return false;
		} else if (!customerReference.equals(other.customerReference))
			return false;
		if (customerTypeId == null) {
			if (other.customerTypeId != null)
				return false;
		} else if (!customerTypeId.equals(other.customerTypeId))
			return false;
		if (dailyLimit == null) {
			if (other.dailyLimit != null)
				return false;
		} else if (!dailyLimit.equals(other.dailyLimit))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (deactivatedBy == null) {
			if (other.deactivatedBy != null)
				return false;
		} else if (!deactivatedBy.equals(other.deactivatedBy))
			return false;
		if (deactivatedDate == null) {
			if (other.deactivatedDate != null)
				return false;
		} else if (!deactivatedDate.equals(other.deactivatedDate))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emosCustomer == null) {
			if (other.emosCustomer != null)
				return false;
		} else if (!emosCustomer.equals(other.emosCustomer))
			return false;
		if (fatherName == null) {
			if (other.fatherName != null)
				return false;
		} else if (!fatherName.equals(other.fatherName))
			return false;
		if (fingerPrintImg == null) {
			if (other.fingerPrintImg != null)
				return false;
		} else if (!fingerPrintImg.equals(other.fingerPrintImg))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (firstNameLocal == null) {
			if (other.firstNameLocal != null)
				return false;
		} else if (!firstNameLocal.equals(other.firstNameLocal))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (halfYearly == null) {
			if (other.halfYearly != null)
				return false;
		} else if (!halfYearly.equals(other.halfYearly))
			return false;
		if (identityExpiredDate == null) {
			if (other.identityExpiredDate != null)
				return false;
		} else if (!identityExpiredDate.equals(other.identityExpiredDate))
			return false;
		if (identityFor == null) {
			if (other.identityFor != null)
				return false;
		} else if (!identityFor.equals(other.identityFor))
			return false;
		if (identityInt == null) {
			if (other.identityInt != null)
				return false;
		} else if (!identityInt.equals(other.identityInt))
			return false;
		if (identityTypeId == null) {
			if (other.identityTypeId != null)
				return false;
		} else if (!identityTypeId.equals(other.identityTypeId))
			return false;
		if (inactivatedDate == null) {
			if (other.inactivatedDate != null)
				return false;
		} else if (!inactivatedDate.equals(other.inactivatedDate))
			return false;
		if (interComcode == null) {
			if (other.interComcode != null)
				return false;
		} else if (!interComcode.equals(other.interComcode))
			return false;
		if (interTrnfyr == null) {
			if (other.interTrnfyr != null)
				return false;
		} else if (!interTrnfyr.equals(other.interTrnfyr))
			return false;
		if (interTrnref == null) {
			if (other.interTrnref != null)
				return false;
		} else if (!interTrnref.equals(other.interTrnref))
			return false;
		if (introducedBy == null) {
			if (other.introducedBy != null)
				return false;
		} else if (!introducedBy.equals(other.introducedBy))
			return false;
		if (introducedDate == null) {
			if (other.introducedDate != null)
				return false;
		} else if (!introducedDate.equals(other.introducedDate))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (isOnlineUser == null) {
			if (other.isOnlineUser != null)
				return false;
		} else if (!isOnlineUser.equals(other.isOnlineUser))
			return false;
		if (kioskPin == null) {
			if (other.kioskPin != null)
				return false;
		} else if (!kioskPin.equals(other.kioskPin))
			return false;
		if (languageId == null) {
			if (other.languageId != null)
				return false;
		} else if (!languageId.equals(other.languageId))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (lastNameLocal == null) {
			if (other.lastNameLocal != null)
				return false;
		} else if (!lastNameLocal.equals(other.lastNameLocal))
			return false;
		if (lastTransactionDate == null) {
			if (other.lastTransactionDate != null)
				return false;
		} else if (!lastTransactionDate.equals(other.lastTransactionDate))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (loyaltyPoints == null) {
			if (other.loyaltyPoints != null)
				return false;
		} else if (!loyaltyPoints.equals(other.loyaltyPoints))
			return false;
		if (medicalInsuranceInd == null) {
			if (other.medicalInsuranceInd != null)
				return false;
		} else if (!medicalInsuranceInd.equals(other.medicalInsuranceInd))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (middleNameLocal == null) {
			if (other.middleNameLocal != null)
				return false;
		} else if (!middleNameLocal.equals(other.middleNameLocal))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (montlyLimit == null) {
			if (other.montlyLimit != null)
				return false;
		} else if (!montlyLimit.equals(other.montlyLimit))
			return false;
		if (nationalityId == null) {
			if (other.nationalityId != null)
				return false;
		} else if (!nationalityId.equals(other.nationalityId))
			return false;
		if (numberOfHits == null) {
			if (other.numberOfHits != null)
				return false;
		} else if (!numberOfHits.equals(other.numberOfHits))
			return false;
		if (pepsIndicator == null) {
			if (other.pepsIndicator != null)
				return false;
		} else if (!pepsIndicator.equals(other.pepsIndicator))
			return false;
		if (placeOfBirth == null) {
			if (other.placeOfBirth != null)
				return false;
		} else if (!placeOfBirth.equals(other.placeOfBirth))
			return false;
		if (quaterlyLimit == null) {
			if (other.quaterlyLimit != null)
				return false;
		} else if (!quaterlyLimit.equals(other.quaterlyLimit))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (shortNameLocal == null) {
			if (other.shortNameLocal != null)
				return false;
		} else if (!shortNameLocal.equals(other.shortNameLocal))
			return false;
		if (signatureSpecimen == null) {
			if (other.signatureSpecimen != null)
				return false;
		} else if (!signatureSpecimen.equals(other.signatureSpecimen))
			return false;
		if (signatureSpecimenClob == null) {
			if (other.signatureSpecimenClob != null)
				return false;
		} else if (!signatureSpecimenClob.equals(other.signatureSpecimenClob))
			return false;
		if (smartCardIndicator == null) {
			if (other.smartCardIndicator != null)
				return false;
		} else if (!smartCardIndicator.equals(other.smartCardIndicator))
			return false;
		if (sundryDebtorReference == null) {
			if (other.sundryDebtorReference != null)
				return false;
		} else if (!sundryDebtorReference.equals(other.sundryDebtorReference))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (titleLocal == null) {
			if (other.titleLocal != null)
				return false;
		} else if (!titleLocal.equals(other.titleLocal))
			return false;
		if (tokenKey == null) {
			if (other.tokenKey != null)
				return false;
		} else if (!tokenKey.equals(other.tokenKey))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (verificationBy == null) {
			if (other.verificationBy != null)
				return false;
		} else if (!verificationBy.equals(other.verificationBy))
			return false;
		if (verificationDate == null) {
			if (other.verificationDate != null)
				return false;
		} else if (!verificationDate.equals(other.verificationDate))
			return false;
		if (verificationTokenId == null) {
			if (other.verificationTokenId != null)
				return false;
		} else if (!verificationTokenId.equals(other.verificationTokenId))
			return false;
		if (weeklyLimit == null) {
			if (other.weeklyLimit != null)
				return false;
		} else if (!weeklyLimit.equals(other.weeklyLimit))
			return false;
		return true;
	}

}
