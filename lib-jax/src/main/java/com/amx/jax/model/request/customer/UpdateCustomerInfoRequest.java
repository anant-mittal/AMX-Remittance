package com.amx.jax.model.request.customer;

import javax.validation.Valid;

public class UpdateCustomerInfoRequest {

	@Valid
	CustomerPersonalDetailUpdateModel customerPersonalDetail;

	@Valid
	LocalAddressDetailsUpdateModel localAddressDetails;

	HomeAddressDetailsUpdateModel homeAddressDestails;

	@Valid
	CustomerEmploymentDetailsUpdateModel customerEmploymentDetails;

	public CustomerPersonalDetailUpdateModel getCustomerPersonalDetail() {
		return customerPersonalDetail;
	}

	public void setCustomerPersonalDetail(CustomerPersonalDetailUpdateModel customerPersonalDetail) {
		this.customerPersonalDetail = customerPersonalDetail;
	}

	public LocalAddressDetailsUpdateModel getLocalAddressDetails() {
		return localAddressDetails;
	}

	public void setLocalAddressDetails(LocalAddressDetailsUpdateModel localAddressDetails) {
		this.localAddressDetails = localAddressDetails;
	}

	public HomeAddressDetailsUpdateModel getHomeAddressDestails() {
		return homeAddressDestails;
	}

	public void setHomeAddressDestails(HomeAddressDetailsUpdateModel homeAddressDestails) {
		this.homeAddressDestails = homeAddressDestails;
	}

	public CustomerEmploymentDetailsUpdateModel getCustomerEmploymentDetails() {
		return customerEmploymentDetails;
	}

	public void setCustomerEmploymentDetails(CustomerEmploymentDetailsUpdateModel customerEmploymentDetails) {
		this.customerEmploymentDetails = customerEmploymentDetails;
	}
}
