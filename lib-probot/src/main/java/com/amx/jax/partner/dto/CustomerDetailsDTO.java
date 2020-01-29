package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

public class CustomerDetailsDTO implements Serializable {

	private static final long serialVersionUID = -8692965709846097701L;
	
	private BigDecimal branchId;
	private BigDecimal customerId;
	private BigDecimal customerReference;
	private BigDecimal idTypeId;
	private BigDecimal loyaltyPoints;
	private BigDecimal countryId;
	private BigDecimal nationalityId;
	private BigDecimal customerTypeId;
	
	private String customerTypeCode;
	private String contactNumber;
	private String creationBranch;
	private String creator;
	private String cusLocalName;
	private String customerName;
	private String email;
	private String employerName;
	private String gender;
	private String idNumber;
	private String idTypeFor;
	private String isKioskUser;
	private String isMobileUser;
	private String isOnlineUser;
	private String isPepsUser;
	private String isSmartCardUser;
	private String nationality;
	private String wuEnrollmentReference;
	private String cityName;
	private String districtName;
	private String stateName;
	private String designation;
	private String address1;
	private String stateId;
	private String countryName;
	private String firstName;
	private String middleName;
	private String lastName;
	
	private Date dateOfBirth;
	private Date idExpiryDate;
	private Date creatinDate;
	
	private Clob signatureSpecimenClob;

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	public BigDecimal getIdTypeId() {
		return idTypeId;
	}

	public void setIdTypeId(BigDecimal idTypeId) {
		this.idTypeId = idTypeId;
	}

	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCreationBranch() {
		return creationBranch;
	}

	public void setCreationBranch(String creationBranch) {
		this.creationBranch = creationBranch;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCusLocalName() {
		return cusLocalName;
	}

	public void setCusLocalName(String cusLocalName) {
		this.cusLocalName = cusLocalName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdTypeFor() {
		return idTypeFor;
	}

	public void setIdTypeFor(String idTypeFor) {
		this.idTypeFor = idTypeFor;
	}

	public String getIsKioskUser() {
		return isKioskUser;
	}

	public void setIsKioskUser(String isKioskUser) {
		this.isKioskUser = isKioskUser;
	}

	public String getIsMobileUser() {
		return isMobileUser;
	}

	public void setIsMobileUser(String isMobileUser) {
		this.isMobileUser = isMobileUser;
	}

	public String getIsOnlineUser() {
		return isOnlineUser;
	}

	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}

	public String getIsPepsUser() {
		return isPepsUser;
	}

	public void setIsPepsUser(String isPepsUser) {
		this.isPepsUser = isPepsUser;
	}

	public String getIsSmartCardUser() {
		return isSmartCardUser;
	}

	public void setIsSmartCardUser(String isSmartCardUser) {
		this.isSmartCardUser = isSmartCardUser;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getWuEnrollmentReference() {
		return wuEnrollmentReference;
	}

	public void setWuEnrollmentReference(String wuEnrollmentReference) {
		this.wuEnrollmentReference = wuEnrollmentReference;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getIdExpiryDate() {
		return idExpiryDate;
	}

	public void setIdExpiryDate(Date idExpiryDate) {
		this.idExpiryDate = idExpiryDate;
	}

	public Date getCreatinDate() {
		return creatinDate;
	}

	public void setCreatinDate(Date creatinDate) {
		this.creatinDate = creatinDate;
	}

	public Clob getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}

	public void setSignatureSpecimenClob(Clob signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}

	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getCustomerTypeCode() {
		return customerTypeCode;
	}
	
	public void setCustomerTypeCode(String customerTypeCode) {
		this.customerTypeCode = customerTypeCode;
	}
	
}
