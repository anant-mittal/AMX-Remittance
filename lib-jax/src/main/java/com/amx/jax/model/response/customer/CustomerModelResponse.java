package com.amx.jax.model.response.customer;

import java.math.BigDecimal;

public class CustomerModelResponse {

	BigDecimal customerId;
	PersonInfo personInfo;
	CustomerFlags customerFlags;

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
}
