package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

public class UpdateCustomerPersonalDetailRequest {

	Date dateOfBirth;
	Boolean insurance;
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
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid whatspapp")
	BigDecimal watsAppMobileNo;

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

	public Boolean getInsurance() {
		return insurance;
	}

	public void setInsurance(Boolean insurance) {
		this.insurance = insurance;
	}

	public Boolean getPepsIndicator() {
		return pepsIndicator;
	}

	public void setPepsIndicator(Boolean pepsIndicator) {
		this.pepsIndicator = pepsIndicator;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
