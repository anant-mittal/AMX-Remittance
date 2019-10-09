package com.amx.jax.branch.beans;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.client.customer.CustomerManagementClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.response.customer.CustomerShortInfo;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.utils.ArgUtil;

import org.springframework.beans.factory.annotation.Autowired;

public class CustomerContext {

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	CustomerManagementClient customerManagementClient;

	@Autowired
	BranchSession branchSession;

	// private OffsiteCustomerDataDTO customer;
	private CustomerShortInfo customer;

	public BigDecimal getCustomerId() {
		if (customer != null) {
			return customer.getCustomerId();
		}
		return null;
	}

	public void setCustomer(CustomerShortInfo customer) {
		this.customer = customer;
	}

	public CustomerShortInfo getCustomer() {
		return customer;
	}

	public OffsiteCustomerDataDTO getCustomerData() {
		String identityInt = branchSession.getCustomer() != null ? branchSession.getCustomer().getIdentityInt() : "";
		BigDecimal customerId = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("customerId"));

		BigDecimal identityTypeId = branchSession.getCustomer() != null ? branchSession.getCustomer().getIdentityTypeId() : null;
		if (branchSession.getCustomer() != null && !ArgUtil.isEmpty(identityInt) && !ArgUtil.isEmpty(identityTypeId)) {
			AmxApiResponse<OffsiteCustomerDataDTO, Object> offsiteCustomerData = offsiteCustRegClient
					.getOffsiteCustomerDetails(identityInt, identityTypeId,customerId);
			return offsiteCustomerData.getResult();
		}
		return null;

	}

	public CustomerShortInfo refresh() {
		String identity = commonHttpRequest.get("identity");
		BigDecimal customerId = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("customerId"));
		String currentIdentity = branchSession.getCustomer() != null ? branchSession.getCustomer().getIdentityInt() : "";

		if (!ArgUtil.isEmpty(identity) && !identity.equals(currentIdentity)|| !ArgUtil.isEmpty(customerId)) {
			BigDecimal identityType = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("identityType"));
			AmxApiResponse<CustomerShortInfo, Object> customerShortDetail = customerManagementClient
					.getCustomerShortDetail(identity, identityType,customerId);
			setCustomer(customerShortDetail.getResult());

		}
		return customer;
	}

}
