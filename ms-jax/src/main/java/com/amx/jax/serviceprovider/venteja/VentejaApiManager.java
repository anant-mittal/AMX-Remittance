package com.amx.jax.serviceprovider.venteja;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
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
		return response.getResult();

	}

	@Override
	public ServiceProviderCallRequestDto createValidateInputRequest(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();
		VentejaServiceProviderFlexField[] allFlexFields = VentejaServiceProviderFlexField.values();
		for (VentejaServiceProviderFlexField flexField : allFlexFields) {
			Map<String, FlexFieldDto> requestFlexFields = remittanceAdditionalBeneFieldModel.getFlexFieldDtoMap();
			FlexFieldDto value = requestFlexFields.get(flexField.name());
			if (value != null) {
				flexField.setValue(serviceProviderCallRequestDto, requestFlexFields);
			}else {
				log.debug("No value found for flexfield: {}", flexField.name());
			}
		}
		serviceProviderBeneDataManager.setBeneficiaryDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderCustomerDataManager.setCustomerDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderTransactionDataManager.setTransactionDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		return serviceProviderCallRequestDto;
	}

}
