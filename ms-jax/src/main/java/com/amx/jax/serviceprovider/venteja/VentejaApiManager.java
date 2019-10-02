package com.amx.jax.serviceprovider.venteja;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.JaxFieldDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.BankConstants;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;

@Component(BankConstants.VINTJA_BANK_CODE)
public class VentejaApiManager extends ServiceProviderApiManager {

	private static final Logger log = LoggerFactory.getLogger(VentejaApiManager.class);

	@Override
	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		parseResponseForError(response.getResult());
		return response.getResult();

	}

	public ServiceProviderCallRequestDto createValidateInputRequest(Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = super.createValidateInputRequest(remitApplParametersMap);
		// TODO: hard coded 1 for testing purpse. modify and call Chiranjeevi;s method
		// once done from his side b ypsasing bene rel seq id
		serviceProviderCallRequestDto.getBeneficiaryDto().setPartner_beneficiary_type("1");
		VentejaServiceProviderFlexField[] allFlexFields = VentejaServiceProviderFlexField.values();
		for (VentejaServiceProviderFlexField flexField : allFlexFields) {
			Map<String, FlexFieldDto> requestFlexFields = (Map<String, FlexFieldDto>) remitApplParametersMap.get("flexFieldDtoMap");
			FlexFieldDto value = requestFlexFields.get(flexField.name());
			if (value != null) {
				flexField.setValue(serviceProviderCallRequestDto, requestFlexFields);
			} else {
				log.debug("No value found for flexfield: {}", flexField.name());
			}
		}
		return serviceProviderCallRequestDto;
	}

	@Override
	public void setAdditionalFlexFieldParams(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			List<JaxFieldDto> requiredFlexFields) {
		if (requiredFlexFields != null) {
			Map<String, JaxFieldDto> requiredFlexFieldsMap = requiredFlexFields.stream().map(i -> {
				if (i.getName().equals("PAYMENT PERIOD FROM DATE")) {
					i.setName("INDIC4");
				}
				if (i.getName().equals("PAYMENT PERIOD EXPIRY DATE")) {
					i.setName("INDIC5");
				}
				return i;
			}).collect(Collectors.toMap(i -> i.getName(), i -> i));
			VentejaServiceProviderFlexField[] ventajaFlexFields = VentejaServiceProviderFlexField.values();
			for (VentejaServiceProviderFlexField field : ventajaFlexFields) {
				JaxFieldDto jaxFieldDto = requiredFlexFieldsMap.get(field.name());
				if (jaxFieldDto != null) {
					field.setAdditionalParams(jaxFieldDto);
				}
			}
		}
	}

}
