package com.amx.jax.serviceprovider.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.ServiceProviderClientWrapper;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.serviceprovider.ServiceProviderBeneDataManager;
import com.amx.jax.serviceprovider.ServiceProviderCustomerDataManager;
import com.amx.jax.serviceprovider.ServiceProviderTransactionDataManager;
import com.amx.jax.services.BankService;

@Component
public class ServiceProviderApiManager {

	@Autowired
	protected ServiceProviderBeneDataManager serviceProviderBeneDataManager;
	@Autowired
	protected ServiceProviderCustomerDataManager serviceProviderCustomerDataManager;
	@Autowired
	protected ServiceProviderTransactionDataManager serviceProviderTransactionDataManager;
	@Autowired
	protected BankService bankService;
	@Autowired
	protected ServiceProviderClientWrapper serviceProviderClientWrapper;

	protected ServiceProviderCallRequestDto createValidateInputRequest(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();

		serviceProviderBeneDataManager.setBeneficiaryDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderCustomerDataManager.setCustomerDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderTransactionDataManager.setTransactionDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		return serviceProviderCallRequestDto;
	}

	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remittanceAdditionalBeneFieldModel,
				remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		return response.getResult();
	}

}
