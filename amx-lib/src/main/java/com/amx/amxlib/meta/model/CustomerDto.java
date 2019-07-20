package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.BeneficiaryListDTO;

public class CustomerDto implements Serializable {
	
	private static final long serialVersionUID = 6882188565866584762L;
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
	
	private String signatureSpecimenClob;
	private String remarks; 
	
	private String sundryDebtorReference;
	private String deactivatedBy;
	private Date deactivatedDate;
	private String kioskPin;
	private BigDecimal nationalityId;
	private String isOnlineUser;
	private String engFullName;
	
	
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
	private Date identityExpiredDate;
	private BigDecimal customerTotalNoOfTrnx;
	
	/** Local Contact details **/
	private String localContactCountry;
	private String localContactState;
	private String localContactDistrict;
	private String localContactCity;
	private String localContactBuilding;
	private String localContatFlat;
	private String street;
	private String house;
	private String blockNo;
	
	private BeneficiaryListDTO defaultBeneDto;
	
	private BigDecimal totalTrnxCount;
	
	
	
	
	
	
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getShortNameLocal() {
		return shortNameLocal;
	}
	public void setShortNameLocal(String shortNameLocal) {
		this.shortNameLocal = shortNameLocal;
	}
	public String getAmlStatus() {
		return amlStatus;
	}
	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}
	public BigDecimal getNumberOfHits() {
		return numberOfHits;
	}
	public void setNumberOfHits(BigDecimal numberOfHits) {
		this.numberOfHits = numberOfHits;
	}
	public String getVerificationBy() {
		return verificationBy;
	}
	public void setVerificationBy(String verificationBy) {
		this.verificationBy = verificationBy;
	}
	public Date getVerificationDate() {
		return verificationDate;
	}
	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}
	public String getAmlStatusUpdatedBy() {
		return amlStatusUpdatedBy;
	}
	public void setAmlStatusUpdatedBy(String amlStatusUpdatedBy) {
		this.amlStatusUpdatedBy = amlStatusUpdatedBy;
	}
	public Date getAmlStatusLastUpdated() {
		return amlStatusLastUpdated;
	}
	public void setAmlStatusLastUpdated(Date amlStatusLastUpdated) {
		this.amlStatusLastUpdated = amlStatusLastUpdated;
	}
	public BigDecimal getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(BigDecimal branchCode) {
		this.branchCode = branchCode;
	}
	public String getActivatedInd() {
		return activatedInd;
	}
	public void setActivatedInd(String activatedInd) {
		this.activatedInd = activatedInd;
	}
	public Date getActivatedDate() {
		return activatedDate;
	}
	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}
	public Date getInactivatedDate() {
		return inactivatedDate;
	}
	public void setInactivatedDate(Date inactivatedDate) {
		this.inactivatedDate = inactivatedDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTitleLocal() {
		return titleLocal;
	}
	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}
	public String getFirstNameLocal() {
		return firstNameLocal;
	}
	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}
	public String getMiddleNameLocal() {
		return middleNameLocal;
	}
	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}
	public String getLastNameLocal() {
		return lastNameLocal;
	}
	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAlterEmailId() {
		return alterEmailId;
	}
	public void setAlterEmailId(String alterEmailId) {
		this.alterEmailId = alterEmailId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSignatureSpecimen() {
		return signatureSpecimen;
	}
	public void setSignatureSpecimen(String signatureSpecimen) {
		this.signatureSpecimen = signatureSpecimen;
	}
	public String getFingerPrintImg() {
		return fingerPrintImg;
	}
	public void setFingerPrintImg(String fingerPrintImg) {
		this.fingerPrintImg = fingerPrintImg;
	}
	public String getIntroducedBy() {
		return introducedBy;
	}
	public void setIntroducedBy(String introducedBy) {
		this.introducedBy = introducedBy;
	}
	public String getMedicalInsuranceInd() {
		return medicalInsuranceInd;
	}
	public void setMedicalInsuranceInd(String medicalInsuranceInd) {
		this.medicalInsuranceInd = medicalInsuranceInd;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNameLocal() {
		return companyNameLocal;
	}
	public void setCompanyNameLocal(String companyNameLocal) {
		this.companyNameLocal = companyNameLocal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCrNo() {
		return crNo;
	}
	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	public String getCountryOfBirth() {
		return countryOfBirth;
	}
	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getTokenKey() {
		return tokenKey;
	}
	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public BigDecimal getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(BigDecimal contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getAmlRemarks() {
		return amlRemarks;
	}
	public void setAmlRemarks(String amlRemarks) {
		this.amlRemarks = amlRemarks;
	}
	public String getBcoRemarks() {
		return bcoRemarks;
	}
	public void setBcoRemarks(String bcoRemarks) {
		this.bcoRemarks = bcoRemarks;
	}
	public String getEmosCustomer() {
		return emosCustomer;
	}
	public void setEmosCustomer(String emosCustomer) {
		this.emosCustomer = emosCustomer;
	}
	public Date getAuditStatementDate() {
		return auditStatementDate;
	}
	public void setAuditStatementDate(Date auditStatementDate) {
		this.auditStatementDate = auditStatementDate;
	}
	public BigDecimal getAuditGrossIncome() {
		return auditGrossIncome;
	}
	public void setAuditGrossIncome(BigDecimal auditGrossIncome) {
		this.auditGrossIncome = auditGrossIncome;
	}
	public String getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}
	public void setSignatureSpecimenClob(String signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSundryDebtorReference() {
		return sundryDebtorReference;
	}
	public void setSundryDebtorReference(String sundryDebtorReference) {
		this.sundryDebtorReference = sundryDebtorReference;
	}
	public String getDeactivatedBy() {
		return deactivatedBy;
	}
	public void setDeactivatedBy(String deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}
	public Date getDeactivatedDate() {
		return deactivatedDate;
	}
	public void setDeactivatedDate(Date deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}
	public String getKioskPin() {
		return kioskPin;
	}
	public void setKioskPin(String kioskPin) {
		this.kioskPin = kioskPin;
	}
	public BigDecimal getNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}
	public String getIsOnlineUser() {
		return isOnlineUser;
	}
	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}
	public String getEngFullName() {
		return engFullName;
	}
	public void setEngFullName(String engFullName) {
		this.engFullName = engFullName;
	}
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	public String getSmartCardIndicator() {
		return smartCardIndicator;
	}
	public void setSmartCardIndicator(String smartCardIndicator) {
		this.smartCardIndicator = smartCardIndicator;
	}
	public Date getIntroducedDate() {
		return introducedDate;
	}
	public void setIntroducedDate(Date introducedDate) {
		this.introducedDate = introducedDate;
	}
	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}
	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}
	public String getPepsIndicator() {
		return pepsIndicator;
	}
	public void setPepsIndicator(String pepsIndicator) {
		this.pepsIndicator = pepsIndicator;
	}
	public BigDecimal getInterComcode() {
		return interComcode;
	}
	public void setInterComcode(BigDecimal interComcode) {
		this.interComcode = interComcode;
	}
	public BigDecimal getInterTrnfyr() {
		return interTrnfyr;
	}
	public void setInterTrnfyr(BigDecimal interTrnfyr) {
		this.interTrnfyr = interTrnfyr;
	}
	public BigDecimal getInterTrnref() {
		return interTrnref;
	}
	public void setInterTrnref(BigDecimal interTrnref) {
		this.interTrnref = interTrnref;
	}
	public String getIdentityInt() {
		return identityInt;
	}
	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}
	public Date getIdentityExpiredDate() {
		return identityExpiredDate;
	}
	public void setIdentityExpiredDate(Date identityExpiredDate) {
		this.identityExpiredDate = identityExpiredDate;
	}
	public BigDecimal getCustomerTotalNoOfTrnx() {
		return customerTotalNoOfTrnx;
	}
	public void setCustomerTotalNoOfTrnx(BigDecimal customerTotalNoOfTrnx) {
		this.customerTotalNoOfTrnx = customerTotalNoOfTrnx;
	}
	public String getLocalContactCountry() {
		return localContactCountry;
	}
	public void setLocalContactCountry(String localContactCountry) {
		this.localContactCountry = localContactCountry;
	}
	public String getLocalContactState() {
		return localContactState;
	}
	public void setLocalContactState(String localContactState) {
		this.localContactState = localContactState;
	}
	public String getLocalContactDistrict() {
		return localContactDistrict;
	}
	public void setLocalContactDistrict(String localContactDistrict) {
		this.localContactDistrict = localContactDistrict;
	}
	public String getLocalContactCity() {
		return localContactCity;
	}
	public void setLocalContactCity(String localContactCity) {
		this.localContactCity = localContactCity;
	}
	public String getLocalContactBuilding() {
		return localContactBuilding;
	}
	public void setLocalContactBuilding(String localContactBuilding) {
		this.localContactBuilding = localContactBuilding;
	}
	public String getLocalContatFlat() {
		return localContatFlat;
	}
	public void setLocalContatFlat(String localContatFlat) {
		this.localContatFlat = localContatFlat;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getBlockNo() {
		return blockNo;
	}
	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}
	public BeneficiaryListDTO getDefaultBeneDto() {
		return defaultBeneDto;
	}
	public void setDefaultBeneDto(BeneficiaryListDTO defaultBeneDto) {
		this.defaultBeneDto = defaultBeneDto;
	}
	public BigDecimal getTotalTrnxCount() {
		return totalTrnxCount;
	}
	public void setTotalTrnxCount(BigDecimal totalTrnxCount) {
		this.totalTrnxCount = totalTrnxCount;
	}
	

}
