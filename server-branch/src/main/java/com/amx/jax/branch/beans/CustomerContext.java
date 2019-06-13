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
		if (!ArgUtil.isEmpty(identity)) {
			BigDecimal identityType = ArgUtil.parseAsBigDecimal(commonHttpRequest.get("identityType"));

			AmxApiResponse<OffsiteCustomerDataDTO, Object> customerResponse = offsiteCustRegClient
					.getOffsiteCustomerDetails(
							identity,
							identityType);
			setCustomer(customerResponse.getResult());
		}
		return customer;
	}

}
