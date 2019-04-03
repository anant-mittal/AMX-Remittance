package com.amx.jax.dbmodel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VW_EX_CORE_CUSTOMER_DETAILS")
public class CustomerCoreDetailsView implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerID;
	
	@Column(name="CUSTOMER_REFERENCE")
	private BigDecimal customerReference;
	
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name="CUSTOMER_LOCAL_NAME")
	private String customerLocalName;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="NATIONALITY")
	private String nationalty;
	
	@Column(name="DATE_OF_BIRTH")
	private Date birthDate;
	
	@Column(name="CONTACT_NUMBER")
	private String contactNumber;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="CITY_NAME")
	private String cityName;
	
	@Column(name="DISTRICT_NAME")
	private String districtName;
	
	@Column(name="STATE_NAME")
	private String stateName;
	
	@Column(name="DESIGNATION")
	private String designation;
	
	@Column(name="EMPLOYER_NAME")
	private String employeeName;
	
	@Column(name="ID_TYPE_ID")
	private BigDecimal idTypeId;
	
	@Column(name="ID_TYPE_FOR")
	private String idTypeFor;
	
	@Column(name="ID_NUMBER")
	private String idNumber;
	
	@Column(name="ID_EXPIRY_DATE")
	private Date expiryDate;
	
	@Column(name="BRANCH_ID")
	private BigDecimal branchId;
	
	@Column(name="CREATION_BRANCH")
	private String creationBranch;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="CREATOR")
	private String creator;
	
	@Column(name="LOYALTY_POINTS")
	private BigDecimal loyalityPoints;
	
	@Column(name="IS_SMART_CARD_USER")
	private String isSmartCardNumber;
	
	@Column(name="WU_ENROLLMENT_REFERENCE")
	private String wuEnrollemntReference;
	
	@Column(name="IS_KIOSK_USER")
	private String isKioskUser;
	
	@Column(name="IS_ONLINE_USER")
	private String isonlineUser;
	
	@Column(name="IS_MOBILE_USER")
	private String isMobileUser;
	
	@Column(name="IS_PEPS_USER")
	private String isPepsUser;
	
	@Column(name="ADDRESS_1")
	private String address;
	
	@Column(name="COUNTRY_NAME")
	private String countryName;
	
	@Column(name="STATE_ID")
	private BigDecimal stateID;
	
	@Column(name="COUNTRY_ID")
	private BigDecimal countryID;
	
	@Column(name="CUSTOMER_CATEGORY_ID")
	private BigDecimal customerCategoryID;
	
	@Column(name="CUSTOMER_CATEGORY")
	private String customerCategory;
	
	
	@Column(name="CORPORATE_DISCOUNT_AMOUNT")
	private BigDecimal corporateDiscountAmount;
	
	
	
	public BigDecimal getCustomerID() {
		return customerID;
	}
	public void setCustomerID(BigDecimal customerID) {
		this.customerID = customerID;
	}
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerLocalName() {
		return customerLocalName;
	}
	public void setCustomerLocalName(String customerLocalName) {
		this.customerLocalName = customerLocalName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNationalty() {
		return nationalty;
	}
	public void setNationalty(String nationalty) {
		this.nationalty = nationalty;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public BigDecimal getIdTypeId() {
		return idTypeId;
	}
	public void setIdTypeId(BigDecimal idTypeId) {
		this.idTypeId = idTypeId;
	}
	public String getIdTypeFor() {
		return idTypeFor;
	}
	public void setIdTypeFor(String idTypeFor) {
		this.idTypeFor = idTypeFor;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public BigDecimal getBranchId() {
		return branchId;
	}
	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}
	public String getCreationBranch() {
		return creationBranch;
	}
	public void setCreationBranch(String creationBranch) {
		this.creationBranch = creationBranch;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public BigDecimal getLoyalityPoints() {
		return loyalityPoints;
	}
	public void setLoyalityPoints(BigDecimal loyalityPoints) {
		this.loyalityPoints = loyalityPoints;
	}
	public String getIsSmartCardNumber() {
		return isSmartCardNumber;
	}
	public void setIsSmartCardNumber(String isSmartCardNumber) {
		this.isSmartCardNumber = isSmartCardNumber;
	}
	public String getWuEnrollemntReference() {
		return wuEnrollemntReference;
	}
	public void setWuEnrollemntReference(String wuEnrollemntReference) {
		this.wuEnrollemntReference = wuEnrollemntReference;
	}
	public String getIsKioskUser() {
		return isKioskUser;
	}
	public void setIsKioskUser(String isKioskUser) {
		this.isKioskUser = isKioskUser;
	}
	public String getIsonlineUser() {
		return isonlineUser;
	}
	public void setIsonlineUser(String isonlineUser) {
		this.isonlineUser = isonlineUser;
	}
	public String getIsMobileUser() {
		return isMobileUser;
	}
	public void setIsMobileUser(String isMobileUser) {
		this.isMobileUser = isMobileUser;
	}
	public String getIsPepsUser() {
		return isPepsUser;
	}
	public void setIsPepsUser(String isPepsUser) {
		this.isPepsUser = isPepsUser;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public BigDecimal getStateID() {
		return stateID;
	}
	public void setStateID(BigDecimal stateID) {
		this.stateID = stateID;
	}
	public BigDecimal getCountryID() {
		return countryID;
	}
	public void setCountryID(BigDecimal countryID) {
		this.countryID = countryID;
	}
	public BigDecimal getCustomerCategoryID() {
		return customerCategoryID;
	}
	public void setCustomerCategoryID(BigDecimal customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
	}
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}
	public BigDecimal getCorporateDiscountAmount() {
		return corporateDiscountAmount;
	}
	public void setCorporateDiscountAmount(BigDecimal corporateDiscountAmount) {
		this.corporateDiscountAmount = corporateDiscountAmount;
	}
	
	
	
	
	
}
