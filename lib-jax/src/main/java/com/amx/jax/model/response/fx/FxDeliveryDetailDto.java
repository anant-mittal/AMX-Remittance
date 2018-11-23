package com.amx.jax.model.response.fx;

public class FxDeliveryDetailDto {

	String firstName;
	String lastName;
	String mobile;
	String timeslot;
	ShippingAddressDto address;
	String transactionRefId;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}

	public ShippingAddressDto getAddress() {
		return address;
	}

	public void setAddress(ShippingAddressDto address) {
		this.address = address;
	}

	public String getTransactionRefId() {
		return transactionRefId;
	}

	public void setTransactionRefId(String transactionRefId) {
		this.transactionRefId = transactionRefId;
	}

}
