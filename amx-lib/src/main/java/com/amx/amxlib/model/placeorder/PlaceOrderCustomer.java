package com.amx.amxlib.model.placeorder;

import java.math.BigDecimal;

public class PlaceOrderCustomer {

	private BigDecimal customerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;

	/**
	 * 
	 */
	public PlaceOrderCustomer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param customerId
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 */
	public PlaceOrderCustomer(BigDecimal customerId, String firstName, String middleName, String lastName,
			String email) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
