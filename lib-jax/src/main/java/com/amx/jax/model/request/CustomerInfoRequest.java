package com.amx.jax.model.request;

public class CustomerInfoRequest {
	CustomerPersonalDetail customerPersonalDetail;
	LocalAddressDetails localAddressDetails;
	HomeAddressDetails homeAddressDestails;
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

	public HomeAddressDetails getHomeAddressDestails() {
		return homeAddressDestails;
	}

	public void setHomeAddressDestails(HomeAddressDetails homeAddressDestails) {
		this.homeAddressDestails = homeAddressDestails;
	}

	public CustomerEmploymentDetails getCustomerEmploymentDetails() {
		return customerEmploymentDetails;
	}

	public void setCustomerEmploymentDetails(CustomerEmploymentDetails customerEmploymentDetails) {
		this.customerEmploymentDetails = customerEmploymentDetails;
	}

	/*
	 * public OtpData getOtpData() { return otpData; } public void
	 * setOtpData(OtpData otpData) { this.otpData = otpData; }
	 */
	@Override
	public String toString() {
		return "CustomerInfoRequest [customerPersonalDetail=" + customerPersonalDetail + ", localAddressDetails="
				+ localAddressDetails + ", homeAddressDestails=" + homeAddressDestails + ", customerEmploymentDetails="
				+ customerEmploymentDetails + "]";
	}

}
