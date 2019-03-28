package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.AbstractModel;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.swagger.ApiMockModelProperty;

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
	@ApiMockModelProperty(example = "91")
	@NotNull(message = "CountryId may not be null")
	private BigDecimal countryId;

	/**
	 * nationality
	 */
	@ApiMockModelProperty(example = "91")
	@NotNull(message = "NationalityId may not be null")
	private BigDecimal nationalityId;

	/**
	 * Civil id
	 */
	@NotNull(message = "IdentityInt may not be null")
	@NumberFormat
	@ApiMockModelProperty(example = "268020406638")
	private String identityInt;

	/**
	 * Prefix/title
	 */
	@NotNull(message = "Title may not be null")
	@ApiMockModelProperty(example = "180")
	private String title;

	@NotNull(message = "FirstName may not be null")
	@Size(min = 1)
	@ApiMockModelProperty(example = "Rakesh")
	private String firstName;

	@NotNull(message = "LastName may not be null")
	@Size(min = 1)
	@ApiMockModelProperty(example = "Parmar")
	private String lastName;

	/** email id */
	@NotNull(message = "Email may not be null")
	@Email
	@ApiMockModelProperty(example = "rakesh@gmail.com", required = false)
	private String email;

	/** mobile without country tel prefix code */
	@NotNull(message = "Mobile may not be null")
	@Size(min = 1)
	@NumberFormat
	@ApiMockModelProperty(example = "98714345")
	private String mobile;

	/** country telephone prefix */
	@NotNull(message = "TelPrefix may not be null")
	@NumberFormat
	@ApiMockModelProperty(example = "965")
	private String telPrefix;

	@ApiMockModelProperty(example = "يبلءيا")
	private String firstNameLocal;

	@ApiMockModelProperty(example = "ءسيقبا")
	private String lastNameLocal;

	@ApiMockModelProperty(example = "2018-09-12")
	private Date issueDate;

	@ApiMockModelProperty(example = "2018-09-12")
	private Date expiryDate;

	@ApiMockModelProperty(example = "2018-09-12")
	private Date dateOfBirth;

	@ApiMockModelProperty(example = "198")
	private BigDecimal identityTypeId;

	@ApiMockModelProperty(example = "Y")
	private String insurance;

	@ApiMockModelProperty(example = "91")
	private String watsAppTelePrefix;

	@ApiMockModelProperty(example = "9321484252")
	private BigDecimal watsAppMobileNo;
	
	@ApiMockModelProperty(example = "Y")
	private String isWatsApp;
	
	@ApiMockModelProperty(example = "OFF_CUSTOMER")
	private CustomerRegistrationType registrationType;
	
	BigDecimal customerId;
	String customerSignature;
	
	private ResourceDTO customerCategory;

	public CustomerRegistrationType getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(CustomerRegistrationType registrationType) {
		this.registrationType = registrationType;
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

	public BigDecimal getWatsAppMobileNo() {
		return watsAppMobileNo;
	}

	public void setWatsAppMobileNo(BigDecimal watsAppMobileNo) {
		this.watsAppMobileNo = watsAppMobileNo;
	}
	
	public String getIsWatsApp() {
		return isWatsApp;
	}

	public void setIsWatsApp(String isWatsApp) {
		this.isWatsApp = isWatsApp;
	}

	@Override
	public String toString() {
		return "CustomerPersonalDetail [countryId=" + countryId + ", nationalityId=" + nationalityId + ", identityInt="
				+ identityInt + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", mobile=" + mobile + ", telPrefix=" + telPrefix + ", firstNameLocal=" + firstNameLocal
				+ ", lastNameLocal=" + lastNameLocal + ", issueDate=" + issueDate + ", expiryDate=" + expiryDate
				+ ", dateOfBirth=" + dateOfBirth + ", identityTypeId=" + identityTypeId + ", insurance=" + insurance
				+ ", watsAppTelePrefix=" + watsAppTelePrefix + ", watsAppMobileNo=" + watsAppMobileNo  
				+ ", isWatsApp=" + isWatsApp + "]";
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public ResourceDTO getCustomerCategory() {
		return customerCategory;
	}
	
	public void setCustomerCategory(ResourceDTO customerCategory) {
		this.customerCategory = customerCategory;
	}

}
