package com.amx.jax.serviceprovider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.services.BeneficiaryService;

@Component
public class ServiceProviderBeneDataManager {

	@Autowired
	BeneficiaryService beneficiaryService;

	public void setBeneficiaryDtoDbValues(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		Map<String, FlexFieldDto> flexFieldDtoMap = request.getFlexFieldDtoMap();
		BenificiaryListView beneficiary = beneficiaryService.getBeneByIdNo(request.getBeneId());
		Benificiary beneDto = serviceProviderCallRequestDto.getBeneficiaryDto();
	}

}
