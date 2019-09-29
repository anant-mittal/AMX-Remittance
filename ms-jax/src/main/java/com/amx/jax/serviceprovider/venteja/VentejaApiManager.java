package com.amx.jax.serviceprovider.venteja;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;

@Component("VENTAJ")
public class VentejaApiManager extends ServiceProviderApiManager {

	private static final Logger log = LoggerFactory.getLogger(VentejaApiManager.class);

	@Override
	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remittanceAdditionalBeneFieldModel,
				remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		parseValidateRemittanceInputsResponse(response.getResult());
		return response.getResult();

	}

	private void parseValidateRemittanceInputsResponse(Validate_Remittance_Inputs_Call_Response result) {
		if (result.getAction_ind() != null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE,
					String.format("Api Validation failed. Action Ind %s Message: %s", result.getAction_ind(), result.getResponse_description()));
		}
	}

	@Override
	public ServiceProviderCallRequestDto createValidateInputRequest(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = super.createValidateInputRequest(remittanceAdditionalBeneFieldModel,
				remitApplParametersMap);
		VentejaServiceProviderFlexField[] allFlexFields = VentejaServiceProviderFlexField.values();
		for (VentejaServiceProviderFlexField flexField : allFlexFields) {
			Map<String, FlexFieldDto> requestFlexFields = remittanceAdditionalBeneFieldModel.getFlexFieldDtoMap();
			FlexFieldDto value = requestFlexFields.get(flexField.name());
			if (value != null) {
				flexField.setValue(serviceProviderCallRequestDto, requestFlexFields);
			} else {
				log.debug("No value found for flexfield: {}", flexField.name());
			}
		}
		return serviceProviderCallRequestDto;
	}

}
