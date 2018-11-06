package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.AbstractModel;

import io.swagger.annotations.ApiModelProperty;

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
	@ApiModelProperty(example="91")
	@NotNull(message="CountryId may not be null")
	private BigDecimal countryId;

	/**
	 * nationality
	 */
	@ApiModelProperty(example="91")
	private BigDecimal nationalityId;

	/**
	 * Civil id
	 */
	@NotNull(message="IdentityInt may not be null")
	@NumberFormat
	@ApiModelProperty(example="268020406638")
	private String identityInt;

	/**
	 * Prefix/title
	 */
	@NotNull(message="Title may not be null")
	@ApiModelProperty(example="180")
	private String title;

	@NotNull(message="FirstName may not be null")
	@Size(min = 1)
	@ApiModelProperty(example="Rakesh")
	private String firstName;

	@NotNull(message="LastName may not be null")
	@Size(min = 1)
	@ApiModelProperty(example="Parmar")
	private String lastName;

	/** email id */
	@Email
	@ApiModelProperty(example="rakesh@gmail.com")
	private String email;

	/** mobile without country tel prefix code */
	@NotNull(message="Mobile may not be null")
	@Size(min = 1)
	@NumberFormat
	@ApiModelProperty(example="98714345")
	private String mobile;

	/** country telephone prefix */
	@NotNull(message="TelPrefix may not be null")
	@NumberFormat
	@ApiModelProperty(example="965")
	private String telPrefix;

	@ApiModelProperty(example="يبلءيا")
	private String firstNameLocal;

	@ApiModelProperty(example="ءسيقبا")
	private String lastNameLocal;

	@ApiModelProperty(example="2018-09-12")
	private Date issueDate;

	@ApiModelProperty(example="2018-09-12")
	private Date expiryDate;

	@NotNull(message="dateOfBirth may not be null")
	@ApiModelProperty(example="2018-09-12")
	private Date dateOfBirth;

	@ApiModelProperty(example="198")
	private BigDecimal identityTypeId;
	
	@ApiModelProperty(example="Y")
	private String insurance;
	
	@ApiModelProperty(example="91")
	private String watsAppTelePrefix;
	
	@ApiModelProperty(example="9321484252")
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
