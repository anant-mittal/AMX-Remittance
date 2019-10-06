package com.amx.jax.serviceprovider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.partner.dto.CustomerDetailsDTO;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.userservice.service.UserService;

@Component
public class ServiceProviderCustomerDataManager {

	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	PartnerTransactionManager partnerTransactionManager;

	public void setCustomerDtoDbValues(Map<String, Object> remitApplParametersMap, ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		CustomerDetailsDTO customerDetailsDTO = partnerTransactionManager.fetchCustomerDetails(metaData.getCustomerId());
		Customer customer = serviceProviderCallRequestDto.getCustomerDto();
		if(customerDetailsDTO.getCustomerReference() != null) {
			customer.setCustomer_reference(customerDetailsDTO.getCustomerReference().toString());
		}
	}

}
