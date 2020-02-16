package com.amx.jax.model.response.customer;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;


public class CustomerModelResponse {

	BigDecimal customerId;
	PersonInfo personInfo;
	CustomerFlags customerFlags;
	List<SecurityQuestionModel> securityquestions;
	ResourceDTO customerCategory;
	

	public CustomerModelResponse() {
		super();
	}

	public CustomerModelResponse(PersonInfo personInfo, CustomerFlags customerFlags) {
		super();
		this.personInfo = personInfo;
		this.customerFlags = customerFlags;
	}

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

	public CustomerFlags getCustomerFlags() {
		return customerFlags;
	}

	public void setCustomerFlags(CustomerFlags customerFlags) {
		this.customerFlags = customerFlags;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public List<SecurityQuestionModel> getSecurityquestions() {
		return securityquestions;
	}

	public void setSecurityquestions(List<SecurityQuestionModel> securityquestions) {
		this.securityquestions = securityquestions;
	}

	public ResourceDTO getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(ResourceDTO customerCategory) {
		this.customerCategory = customerCategory;
	}

	
	
}
