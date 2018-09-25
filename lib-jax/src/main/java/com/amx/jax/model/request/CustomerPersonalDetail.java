package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.AbstractModel;

/**
 * Customer personal detail model
 *
 * @author Prashant
 * @since 2018-04-30
 */
public class CustomerPersonalDetail extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Residence country
	 */
	@NotNull
	private BigDecimal countryId;

	/**
	 * nationality
	 */
	@NotNull
	private BigDecimal nationalityId;

	/**
	 * Civil id
	 */
	@NotNull
	@NumberFormat
	private String identityInt;

	/**
	 * Prefix/title
	 */
	@NotNull
	private String title;

	@NotNull
	@Size(min = 1)
	private String firstName;

	@NotNull
	@Size(min = 1)
	private String lastName;

	/** email id */
	@NotNull
	@Email
	private String email;

	/** mobile without country tel prefix code */
	@NotNull
	@Size(min = 1)
	@NumberFormat
	private String mobile;

	/** country telephone prefix */
	@NotNull
	@NumberFormat
	private String telPrefix;

	private String firstNameLocal;

	private String lastNameLocal;

	private Date issueDate;

	private Date expiryDate;

	private Date dateOfBirth;

	private BigDecimal identityTypeId;
	
	private String insurance;
	
	private String watsAppTelePrefix;
	
	private String watsAppMobileNo;

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

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelPrefix() {
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public String getLastNameLocal() {
		return lastNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getWatsAppTelePrefix() {
		return watsAppTelePrefix;
	}

	public void setWatsAppTelePrefix(String watsAppTelePrefix) {
		this.watsAppTelePrefix = watsAppTelePrefix;
	}

	public String getWatsAppMobileNo() {
		return watsAppMobileNo;
	}

	public void setWatsAppMobileNo(String watsAppMobileNo) {
		this.watsAppMobileNo = watsAppMobileNo;
	}

	@Override
	public String toString() {
		return "CustomerPersonalDetail [countryId=" + countryId + ", nationalityId=" + nationalityId + ", identityInt="
				+ identityInt + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", mobile=" + mobile + ", telPrefix=" + telPrefix + ", firstNameLocal=" + firstNameLocal
				+ ", lastNameLocal=" + lastNameLocal + ", issueDate=" + issueDate + ", expiryDate=" + expiryDate
				+ ", dateOfBirth=" + dateOfBirth + ", identityTypeId=" + identityTypeId + ", insurance=" + insurance
				+ ", watsAppTelePrefix=" + watsAppTelePrefix + ", watsAppMobileNo=" + watsAppMobileNo + "]";
	}

	
}
