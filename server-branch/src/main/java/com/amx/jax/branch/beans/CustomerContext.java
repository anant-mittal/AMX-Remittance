package com.amx.jax.branch.beans;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.utils.ArgUtil;

public class CustomerContext {

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	BranchSession branchSession;

	private OffsiteCustomerDataDTO customer;

	public BigDecimal getCustomerId() {
		if (customer != null && customer.getCustomerPersonalDetail() != null) {
			return customer.getCustomerPersonalDetail().getCustomerId();
		}
		return null;
	}

	public void setCustomer(OffsiteCustomerDataDTO customer) {
		this.customer = customer;
	}

	public OffsiteCustomerDataDTO getCustomer() {
		return customer;
	}

	public OffsiteCustomerDataDTO refresh() {
		String identity = commonHttpRequest.get("identity");
		BigDecimal customerId = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("customerId"));

		if (!ArgUtil.isEmpty(identity) || !ArgUtil.isEmpty(customerId)) {
			BigDecimal identityType = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("identityType"));

			AmxApiResponse<OffsiteCustomerDataDTO, Object> customerResponse = offsiteCustRegClient
					.getOffsiteCustomerDetails(
							identity,
							identityType,customerId);
			setCustomer(customerResponse.getResult());
			branchSession.setCustomer(customerResponse.getResult());
		}
		return customer;
	}

}
