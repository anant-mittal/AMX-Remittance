package com.amx.jax.serviceprovider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	public void setCustomerDtoDbValues(Map<String, Object> remitApplParametersMap, ServiceProviderCallRequestDto serviceProviderCallRequestDto) {

	}

}
