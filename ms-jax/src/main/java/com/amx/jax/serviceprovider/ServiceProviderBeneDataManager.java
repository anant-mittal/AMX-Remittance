package com.amx.jax.serviceprovider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.userservice.service.UserService;

@Component
public class ServiceProviderBeneDataManager {

	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	public void setBeneficiaryDtoDbValues(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		Customer customer = userService.getCustById(metaData.getCustomerId());
		Map<String, FlexFieldDto> flexFieldDtoMap = request.getFlexFieldDtoMap();
		BenificiaryListView beneficiary = beneficiaryService.getBeneByIdNo(request.getBeneId());
		Benificiary beneDto = serviceProviderCallRequestDto.getBeneficiaryDto();
		beneDto.setLast_name(customer.getLastName());
		beneDto.setFirst_name(customer.getFirstName());
		beneDto.setMiddle_name(customer.getMiddleName());
		beneDto.setDate_of_birth(customer.getDateOfBirth());
		beneDto.setEmail(customer.getEmail());
	}

}
