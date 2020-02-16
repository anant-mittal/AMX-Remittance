package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.amx.jax.model.request.customer.CustomerPassportData;
import com.amx.jax.model.request.customer.ICustomerContactData;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UpdateCustomerPersonalDetailRequest implements ICustomerContactData {

	Date dateOfBirth;
	String insurance;
	@JsonIgnore
	Boolean insuranceInd;
	String customerSignature;
	Boolean pepsIndicator;
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid Mobile No")
	String mobile;
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid Tele Prefix")
	String telPrefix;
	@Email
	String email;
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid whatsapp Prefix")
	String watsAppTelePrefix;
	BigDecimal watsAppMobileNo;
	@Valid
	CustomerPassportData customerPassportData;
	@Size(max = 200)
	String firstName;
	@Size(max = 200)
	String lastName;
	@Size(max = 200)
	String firstNameLocal;
	@Size(max = 200)
	String lastNameLocal;

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public Boolean getPepsIndicator() {
		return pepsIndicator;
	}

	public void setPepsIndicator(Boolean pepsIndicator) {
		this.pepsIndicator = pepsIndicator;
	}

	@Override
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String getTelPrefix() {
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getWatsAppTelePrefix() {
		return watsAppTelePrefix;
	}

	public void setWatsAppTelePrefix(String watsAppTelePrefix) {
		this.watsAppTelePrefix = watsAppTelePrefix;
	}

	@Override
	public BigDecimal getWatsAppMobileNo() {
		return watsAppMobileNo;
	}

	public void setWatsAppMobileNo(BigDecimal watsAppMobileNo) {
		this.watsAppMobileNo = watsAppMobileNo;
	}

	public CustomerPassportData getCustomerPassportData() {
		return customerPassportData;
	}

	public void setCustomerPassportData(CustomerPassportData customerPassportData) {
		this.customerPassportData = customerPassportData;
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

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
		if (insurance != null) {
			this.insuranceInd = "Y".equalsIgnoreCase(insurance);
		}
	}

	public Boolean getInsuranceInd() {
		return insuranceInd;
	}

	public void setInsuranceInd(Boolean insuranceInd) {
		this.insuranceInd = insuranceInd;
	}

}
