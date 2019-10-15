package com.amx.jax.model.response.customer;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.CustomerStatusModel;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LastLoginDetails;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.request.PolicyDetails;

public class OffsiteCustomerDataDTO {

	private String identityInt;
	
	private BigDecimal identityTypeId;

	CustomerPersonalDetail customerPersonalDetail;
	
	LocalAddressDetails localAddressDetails;
	
	HomeAddressDetails homeAddressDetails;

	CustomerEmploymentDetails customerEmploymentDetails;
	CustomerFlags customerFlags;
	List<CustomerDocumentInfo>  customerDocuments;
	CustomerStatusModel customerStatusModel;
	String statusKey;
	LastLoginDetails lastLoginDetails;
	PolicyDetails policyDetails;
	
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

	public List<CustomerDocumentInfo> getCustomerDocuments() {
		return customerDocuments;
	}

	public void setCustomerDocuments(List<CustomerDocumentInfo> customerDocuments) {
		this.customerDocuments = customerDocuments;
	}

	public CustomerStatusModel getCustomerStatusModel() {
		return customerStatusModel;
	}

	public void setCustomerStatusModel(CustomerStatusModel customerStatusModel) {
		this.customerStatusModel = customerStatusModel;
	}

	public HomeAddressDetails getHomeAddressDetails() {
		return homeAddressDetails;
	}

	public void setHomeAddressDetails(HomeAddressDetails homeAddressDetails) {
		this.homeAddressDetails = homeAddressDetails;
	}

	public LastLoginDetails getLastLoginDetails() {
		return lastLoginDetails;
	}

	public void setLastLoginDetails(LastLoginDetails lastLoginDetails) {
		this.lastLoginDetails = lastLoginDetails;
	}

	public PolicyDetails getPolicyDetails() {
		return policyDetails;
	}

	public void setPolicyDetails(PolicyDetails policyDetails) {
		this.policyDetails = policyDetails;
	}
	
	
}
