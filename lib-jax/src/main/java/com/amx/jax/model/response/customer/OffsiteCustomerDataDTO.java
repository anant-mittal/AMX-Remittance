package com.amx.jax.model.response.customer;

import java.math.BigDecimal;

import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;

public class OffsiteCustomerDataDTO {

	private String identityInt;
	
	private BigDecimal identityTypeId;

	CustomerPersonalDetail customerPersonalDetail;
	
	LocalAddressDetails localAddressDetails;
	
	HomeAddressDetails homeAddressDestails;

	CustomerEmploymentDetails customerEmploymentDetails;
	CustomerFlags customerFlags;
	String statusKey;

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getIdentityTypeId() {
		return identityTypeId;
	}

	public void setIdentityTypeId(BigDecimal identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

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

	public CustomerFlags getCustomerFlags() {
		return customerFlags;
	}

	public void setCustomerFlags(CustomerFlags customerFlags) {
		this.customerFlags = customerFlags;
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}
	
}
