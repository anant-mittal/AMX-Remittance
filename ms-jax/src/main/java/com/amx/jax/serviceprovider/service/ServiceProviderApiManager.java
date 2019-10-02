package com.amx.jax.serviceprovider.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.ServiceProviderClientWrapper;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.serviceprovider.ServiceProviderBeneDataManager;
import com.amx.jax.serviceprovider.ServiceProviderCustomerDataManager;
import com.amx.jax.serviceprovider.ServiceProviderTransactionDataManager;

@Component
public class ServiceProviderApiManager {

	@Autowired
	protected ServiceProviderBeneDataManager serviceProviderBeneDataManager;
	@Autowired
	protected ServiceProviderCustomerDataManager serviceProviderCustomerDataManager;
	@Autowired
	protected ServiceProviderTransactionDataManager serviceProviderTransactionDataManager;
	@Autowired
	protected ServiceProviderClientWrapper serviceProviderClientWrapper;

	protected ServiceProviderCallRequestDto createValidateInputRequest(Map<String, Object> remitParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();

		serviceProviderBeneDataManager.setBeneficiaryDtoDbValues(remitParametersMap, serviceProviderCallRequestDto);
		serviceProviderCustomerDataManager.setCustomerDtoDbValues(remitParametersMap, serviceProviderCallRequestDto);
		serviceProviderTransactionDataManager.setTransactionDtoDbValues(remitParametersMap, serviceProviderCallRequestDto);
		return serviceProviderCallRequestDto;
	}

	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		Map<String, Object> inputs = new HashMap<>();
		inputs.putAll(remitApplParametersMap);
		inputs.put("P_BENEFICIARY_RELASHIONSHIP_ID", remittanceAdditionalBeneFieldModel.getBeneId());
		inputs.put("flexFieldDtoMap", remittanceAdditionalBeneFieldModel.getFlexFieldDtoMap());
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(inputs);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		return response.getResult();
	}

	public void setAdditionalFlexFieldParams(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			List<JaxFieldDto> requiredFlexFields) {

	}

	protected void parseResponseForError(ServiceProviderResponse result) {
		if (result.getAction_ind() == null) {
			throw new GlobalException(JaxError.TRANSACTION_VALIDATION_FAIL, "No action Indic returned by service provider api");
		}
		switch (result.getAction_ind()) {
		case "T":
		case "C":
		case "R":
		case "U":
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE,
					String.format("Api Validation failed. Action Ind %s Message: %s", result.getAction_ind(), result.getResponse_description()));
		}

	}
}
