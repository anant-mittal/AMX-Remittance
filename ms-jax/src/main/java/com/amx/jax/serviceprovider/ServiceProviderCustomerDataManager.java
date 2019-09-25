package com.amx.jax.serviceprovider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.userservice.service.UserService;

@Component
public class ServiceProviderCustomerDataManager {

	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	public void setCustomerDtoDbValues(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			ServiceProviderCallRequestDto serviceProviderCallRequestDto) {

		Customer customer = userService.getCustById(metaData.getCustomerId());
		com.amx.jax.model.request.serviceprovider.Customer customerDto = serviceProviderCallRequestDto.getCustomerDto();
		customerDto.setLast_name(customer.getLastName());
		customerDto.setFirst_name(customer.getFirstName());
		customerDto.setMiddle_name(customer.getMiddleName());
		customerDto.setDate_of_birth(customer.getDateOfBirth());
		customerDto.setEmail(customer.getEmail());

	}

}
