package com.amx.jax.model.request;

import javax.validation.Valid;

public class CustomerInfoRequest {
	@Valid
	CustomerPersonalDetail customerPersonalDetail;
	
	@Valid
	LocalAddressDetails localAddressDetails;
	
	HomeAddressDetails homeAddressDetails;
	
	@Valid
	CustomerEmploymentDetails customerEmploymentDetails;

	/* OtpData otpData; */
	public CustomerPersonalDetail getCustomerPersonalDetail() {
		return customerPersonalDetail;
	}

	public void setCustomerPersonalDetail(CustomerPersonalDetail customerPersonalDetail) {
		this.customerPersonalDetail = customerPersonalDetail;
	}

	public LocalAddressDetails getLocalAddressDetails() {
		return localAddressDetails;
	}

	public void setLocalAddressDetails(LocalAddressDetails localAddressDetails) {
		this.localAddressDetails = localAddressDetails;
	}

	public CustomerEmploymentDetails getCustomerEmploymentDetails() {
		return customerEmploymentDetails;
	}

	public void setCustomerEmploymentDetails(CustomerEmploymentDetails customerEmploymentDetails) {
		this.customerEmploymentDetails = customerEmploymentDetails;
	}

	@Override
	public String toString() {
		return "CustomerInfoRequest [customerPersonalDetail=" + customerPersonalDetail + ", localAddressDetails="
				+ localAddressDetails + ", homeAddressDestails=" + homeAddressDetails + ", customerEmploymentDetails="
				+ customerEmploymentDetails + "]";
	}

	public HomeAddressDetails getHomeAddressDetails() {
		return homeAddressDetails;
	}

	public void setHomeAddressDetails(HomeAddressDetails homeAddressDetails) {
		this.homeAddressDetails = homeAddressDetails;
	}

}
