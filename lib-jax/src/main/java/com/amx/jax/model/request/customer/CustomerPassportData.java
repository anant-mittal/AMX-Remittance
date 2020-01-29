package com.amx.jax.model.request.customer;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerPassportData {

	@Size(max = 15)
	@ApiMockModelProperty(example = "J4161060")
	String passportNumber;

	@Past
	Date passportIssueDate;

	Date passportExpiryDate;

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public Date getPassportIssueDate() {
		return passportIssueDate;
	}

	public void setPassportIssueDate(Date passportIssueDate) {
		this.passportIssueDate = passportIssueDate;
	}

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

}
