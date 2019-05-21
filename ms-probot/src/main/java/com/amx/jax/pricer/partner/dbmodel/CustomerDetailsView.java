package com.amx.jax.pricer.partner.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JAX_VW_CUSTOMER_DETAILS")
public class CustomerDetailsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	//Getters ans setters.
	
	@Id
	@Column(name="CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="BRANCH_ID")
	public BigDecimal getBranchId() {
		return branchId;
	}
	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}
	
	@Column(name="CONTACT_NUMBER")
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	@Column(name="CREATION_BRANCH")
	public String getCreationBranch() {
		return creationBranch;
	}
	public void setCreationBranch(String creationBranch) {
		this.creationBranch = creationBranch;
	}
	
	@Column(name="CREATOR")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@Column(name="CUSTOMER_LOCAL_NAME")
	public String getCusLocalName() {
		return cusLocalName;
	}
	public void setCusLocalName(String cusLocalName) {
		this.cusLocalName = cusLocalName;
	}
	
	@Column(name="CUSTOMER_NAME")
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Column(name="CUSTOMER_REFERENCE")
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	
	@Column(name="DATE_OF_BIRTH")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="EMPLOYER_NAME")
	public String getEmployerName() {
		return employerName;
	}
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	
	@Column(name="GENDER")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="ID_EXPIRY_DATE")
	public Date getIdExpiryDate() {
		return idExpiryDate;
	}
	public void setIdExpiryDate(Date idExpiryDate) {
		this.idExpiryDate = idExpiryDate;
	}
	
	@Column(name="ID_NUMBER")
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	@Column(name="ID_TYPE_FOR")
	public String getIdTypeFor() {
		return idTypeFor;
	}
	public void setIdTypeFor(String idTypeFor) {
		this.idTypeFor = idTypeFor;
	}
	
	@Column(name="ID_TYPE_ID")
	public BigDecimal getIdTypeId() {
		return idTypeId;
	}
	public void setIdTypeId(BigDecimal idTypeId) {
		this.idTypeId = idTypeId;
	}
	
	@Column(name="IS_KIOSK_USER")
	public String getIsKioskUser() {
		return isKioskUser;
	}
	public void setIsKioskUser(String isKioskUser) {
		this.isKioskUser = isKioskUser;
	}
	
	@Column(name="IS_MOBILE_USER")
	public String getIsMobileUser() {
		return isMobileUser;
	}
	public void setIsMobileUser(String isMobileUser) {
		this.isMobileUser = isMobileUser;
	}
	
	@Column(name="IS_ONLINE_USER")
	public String getIsOnlineUser() {
		return isOnlineUser;
	}
	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}
	
	@Column(name="IS_PEPS_USER")
	public String getIsPepsUser() {
		return isPepsUser;
	}
	public void setIsPepsUser(String isPepsUser) {
		this.isPepsUser = isPepsUser;
	}
	
	@Column(name="IS_SMART_CARD_USER")
	public String getIsSmartCardUser() {
		return isSmartCardUser;
	}
	public void setIsSmartCardUser(String isSmartCardUser) {
		this.isSmartCardUser = isSmartCardUser;
	}
	
	@Column(name="LOYALTY_POINTS")
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	@Column(name="NATIONALITY")
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	@Column(name="WU_ENROLLMENT_REFERENCE")
	public String getWuEnrollmentReference() {
		return wuEnrollmentReference;
	}
	public void setWuEnrollmentReference(String wuEnrollmentReference) {
		this.wuEnrollmentReference = wuEnrollmentReference;
	}
	
	@Column(name="CITY_NAME")
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@Column(name="DISTRICT_NAME")
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	@Column(name="STATE_NAME")
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@Column(name="CREATION_DATE")
	public Date getCreatinDate() {
		return creatinDate;
	}
	public void setCreatinDate(Date creatinDate) {
		this.creatinDate = creatinDate;
	}
	
	@Column(name="DESIGNATION")
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	@Column(name="ADDRESS_1")
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	@Column(name="STATE_ID")
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	@Column(name="COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	@Column(name="NATIONALITY_ID")
	public BigDecimal getNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}
	
	@Column(name="FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="MIDDLE_NAME")
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	@Column(name="LAST_NAME")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name="SIGNATURE_SPECIMEN_CLOB")
	public Clob getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}
	public void setSignatureSpecimenClob(Clob signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}
	
	@Column(name="CUSTOMER_TYPE_ID")
	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}
	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	
	@Column(name="CUSTOMER_TYPE")
	public String getCustomerTypeCode() {
		return customerTypeCode;
	}
	public void setCustomerTypeCode(String customerTypeCode) {
		this.customerTypeCode = customerTypeCode;
	}
	
}
