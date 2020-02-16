package com.amx.jax.model.request;

import java.math.BigDecimal;

import javax.validation.Valid;

import com.amx.jax.model.request.customer.ICustomerContactData;

public class CustomerInfoRequest implements ICustomerContactData {
	@Valid
	CustomerPersonalDetail customerPersonalDetail;
	
	@Valid
	LocalAddressDetails localAddressDetails;
	
	HomeAddressDetails homeAddressDetails;
	
	@Deprecated
	HomeAddressDetails homeAddressDestails;
	
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
				+ localAddressDetails + ", homeAddressDetails=" + homeAddressDetails + ", customerEmploymentDetails="
				+ customerEmploymentDetails + "]";
	}

	public HomeAddressDetails getHomeAddressDetails() {
		return homeAddressDetails;
	}

	public void setHomeAddressDetails(HomeAddressDetails homeAddressDetails) {
		this.homeAddressDetails = homeAddressDetails;
	}

	public HomeAddressDetails getHomeAddressDestails() {
		return homeAddressDestails;
	}

	public void setHomeAddressDestails(HomeAddressDetails homeAddressDestails) {
		this.homeAddressDestails = homeAddressDestails;
	}

	@Override
	public String getEmail() {
		return customerPersonalDetail != null ? customerPersonalDetail.getEmail() : null;
	}

	@Override
	public String getMobile() {
		return customerPersonalDetail != null ? customerPersonalDetail.getMobile() : null;
	}

	@Override
	public String getTelPrefix() {
		return customerPersonalDetail != null ? customerPersonalDetail.getTelPrefix() : null;
	}

	@Override
	public String getWatsAppTelePrefix() {
		return customerPersonalDetail != null ? customerPersonalDetail.getWatsAppTelePrefix() : null;
	}

	@Override
	public BigDecimal getWatsAppMobileNo() {
		return customerPersonalDetail != null ? customerPersonalDetail.getWatsAppMobileNo() : null;
	}

}
