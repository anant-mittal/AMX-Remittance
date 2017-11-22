package com.amx.jax.dbmodel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="VIEW_EX_ONLINE_CUSTOMER_CHECK")
public class ViewOnlineCustomerCheck  implements Serializable{
	
	 
	private static final long serialVersionUID = 1L;
	private BigDecimal companyId;
	private BigDecimal countryId;
	private BigDecimal customerId;
	private BigDecimal customerRference;
	private String civilId;
	private String mobile;
	private String email;
	private String englishName;
	private String isOnlineUSer;
	private String amlStatus;
	private String onlineRegistrationStatus;
	private String userName;
	private String password;
	private String secondaryPassword;
	
	private BigDecimal securityQuestion1;
	private BigDecimal securityQuestion2;
	private BigDecimal securityQuestion3;
	private BigDecimal securityQuestion4;
	private BigDecimal securityQuestion5;
	
	
	private String securityAnswer1;
	private String securityAnswer2;
	private String securityAnswer3;
	private String securityAnswer4;
	private String securityAnswer5;	
	private String userType;
	private String caption;
	private String imageUrl;
	private String loginType;
	private Date lockDt;
	private BigDecimal lockCnt;
	private BigDecimal onlineCustomerId;
	private BigDecimal customerTypeId;
	
	
	private String firstName;
	private String middleName;
	private String lastName;
	
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String localFullName;
	private String dateOfBirth;
	private String idExpirtyDate;
	private BigDecimal nationality;
	private BigDecimal loyaltyPoints;
	
	
	
	
	
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
	@Id
	@Column(name="CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	@Column(name="IDENTITY_INT")
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	@Column(name="MOBILE")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="ENGNAME")
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	@Column(name="IS_ONLINE_USER")
	public String getIsOnlineUSer() {
		return isOnlineUSer;
	}
	public void setIsOnlineUSer(String isOnlineUSer) {
		this.isOnlineUSer = isOnlineUSer;
	}
	@Column(name="AML_STATUS")
	public String getAmlStatus() {
		return amlStatus;
	}
	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}
	@Column(name="ONLINE_REGISTRATION_STATUS")
	public String getOnlineRegistrationStatus() {
		return onlineRegistrationStatus;
	}
	public void setOnlineRegistrationStatus(String onlineRegistrationStatus) {
		this.onlineRegistrationStatus = onlineRegistrationStatus;
	}
	@Column(name="USER_NAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="SECURITY_QUESTION1")
	public BigDecimal getSecurityQuestion1() {
		return securityQuestion1;
	}
	public void setSecurityQuestion1(BigDecimal securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}
	@Column(name="SECURITY_QUESTION2")
	public BigDecimal getSecurityQuestion2() {
		return securityQuestion2;
	}
	public void setSecurityQuestion2(BigDecimal securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}
	@Column(name="SECURITY_QUESTION3")
	public BigDecimal getSecurityQuestion3() {
		return securityQuestion3;
	}
	public void setSecurityQuestion3(BigDecimal securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}
	@Column(name="SECURITY_QUESTION4")
	public BigDecimal getSecurityQuestion4() {
		return securityQuestion4;
	}
	public void setSecurityQuestion4(BigDecimal securityQuestion4) {
		this.securityQuestion4 = securityQuestion4;
	}
	@Column(name="SECURITY_QUESTION5")
	public BigDecimal getSecurityQuestion5() {
		return securityQuestion5;
	}
	public void setSecurityQuestion5(BigDecimal securityQuestion5) {
		this.securityQuestion5 = securityQuestion5;
	}
	@Column(name="SECURITY_ANSWER1")
	public String getSecurityAnswer1() {
		return securityAnswer1;
	}
	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}
	@Column(name="SECURITY_ANSWER2")
	public String getSecurityAnswer2() {
		return securityAnswer2;
	}
	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}
	@Column(name="SECURITY_ANSWER3")
	public String getSecurityAnswer3() {
		return securityAnswer3;
	}
	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}
	@Column(name="SECURITY_ANSWER4")
	public String getSecurityAnswer4() {
		return securityAnswer4;
	}
	public void setSecurityAnswer4(String securityAnswer4) {
		this.securityAnswer4 = securityAnswer4;
	}
	@Column(name="SECURITY_ANSWER5")
	public String getSecurityAnswer5() {
		return securityAnswer5;
	}
	public void setSecurityAnswer5(String securityAnswer5) {
		this.securityAnswer5 = securityAnswer5;
	}
	
	@Column(name="USER_TYPE")
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Column(name="CAPTION")
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	@Column(name="IMAGE_PATH")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Column(name="LOGIN_TYP")
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	@Column(name="LOCK_DT")
	public Date getLockDt() {
		return lockDt;
	}
	public void setLockDt(Date lockDt) {
		this.lockDt = lockDt;
	}
	@Column(name="LOCK_CNT")
	public BigDecimal getLockCnt() {
		return lockCnt;
	}
	public void setLockCnt(BigDecimal lockCnt) {
		this.lockCnt = lockCnt;
	}
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="SECONDARY_PASSWORD")
	public String getSecondaryPassword() {
		return secondaryPassword;
	}
	
	public void setSecondaryPassword(String secondaryPassword) {
		this.secondaryPassword = secondaryPassword;
	}
	@Column(name="CUST_LOGIN_ID")
	public BigDecimal getOnlineCustomerId() {
		return onlineCustomerId;
	}
	public void setOnlineCustomerId(BigDecimal onlineCustomerId) {
		this.onlineCustomerId = onlineCustomerId;
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
	@Column(name="FIRST_NAME_LOCAL")
	public String getFirstNameLocal() {
		return firstNameLocal;
	}
	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}
	@Column(name="MIDDLE_NAME_LOCAL")
	public String getMiddleNameLocal() {
		return middleNameLocal;
	}
	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}
	@Column(name="LAST_NAME_LOCAL")
	public String getLastNameLocal() {
		return lastNameLocal;
	}
	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}
	@Column(name="LOCALNAME")
	public String getLocalFullName() {
		return localFullName;
	}
	public void setLocalFullName(String localFullName) {
		this.localFullName = localFullName;
	}
	@Column(name="DATE_OF_BIRTH")
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	@Column(name="IDENTITY_EXPIRY_DATE")
	public String getIdExpirtyDate() {
		return idExpirtyDate;
	}
	public void setIdExpirtyDate(String idExpirtyDate) {
		this.idExpirtyDate = idExpirtyDate;
	}
	@Column(name="CUSTOMER_REFERENCE")
	public BigDecimal getCustomerRference() {
		return customerRference;
	}
	public void setCustomerRference(BigDecimal customerRference) {
		this.customerRference = customerRference;
	}
	@Column(name="NATIONALITY")
	public BigDecimal getNationality() {
		return nationality;
	}
	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
	}
	@Column(name="CUSTOMER_TYPE_ID")
	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}
	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	@Column(name="LOYALTY_POINTS")
	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
}