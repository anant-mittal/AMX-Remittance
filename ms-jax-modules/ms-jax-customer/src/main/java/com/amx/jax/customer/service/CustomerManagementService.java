package com.amx.jax.customer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;

@Service
public class CustomerManagementService {

	@Autowired
	CustomerManagementManager customerManagementManager;

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetail(String identityInt,
			BigDecimal identityTypeId) {
		OffsiteCustomerDataDTO dto = customerManagementManager.getCustomerDeatils(identityInt, identityTypeId);
		return AmxApiResponse.build(dto);
	}
}
