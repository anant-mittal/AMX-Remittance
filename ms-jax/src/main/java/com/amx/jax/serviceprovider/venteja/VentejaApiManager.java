package com.amx.jax.serviceprovider.venteja;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.BankConstants;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;

@Component(BankConstants.VINTJA_BANK_CODE)
public class VentejaApiManager extends ServiceProviderApiManager {

	private static final Logger log = LoggerFactory.getLogger(VentejaApiManager.class);

	@Override
	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		Map<String, Object> inputs = new HashMap<>();
		RemittanceTransactionDrRequestModel remittanceTransactionDrRequestModel = (RemittanceTransactionDrRequestModel) remittanceAdditionalBeneFieldModel;
		inputs.putAll(remitApplParametersMap);
		inputs.put("P_BENEFICIARY_RELASHIONSHIP_ID", remittanceAdditionalBeneFieldModel.getBeneId());
		inputs.put("flexFieldDtoMap", remittanceAdditionalBeneFieldModel.getFlexFieldDtoMap());
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(inputs);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		fetchServiceProviderLogs(response.getResult(), serviceProviderCallRequestDto);
		fetchServiceProviderData(remittanceTransactionDrRequestModel.getDynamicRroutingPricingBreakup(), serviceProviderCallRequestDto);
		parseValidateResponseForError(response.getResult());
		return response.getResult();
	}

	@Override
	public Validate_Remittance_Inputs_Call_Response validateApiVentajaInput(DynamicRoutingPricingDto dynamicRoutingPricingDto,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		fetchServiceProviderLogs(response.getResult(), serviceProviderCallRequestDto);
		fetchServiceProviderData(dynamicRoutingPricingDto, serviceProviderCallRequestDto);
		parseValidateResponseForError(response.getResult());
		return response.getResult();
	}

	public ServiceProviderCallRequestDto createValidateInputRequest(Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = super.createValidateInputRequest(remitApplParametersMap);
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

	@Override
	public Remittance_Call_Response sendRemittance(Map<String, Object> remitApplParametersMap) {
		Map<String, Object> inputs = new HashMap<>();
		inputs.putAll(remitApplParametersMap);
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(inputs);
		AmxApiResponse<Remittance_Call_Response, Object> response = serviceProviderClientWrapper.sendRemittance(serviceProviderCallRequestDto);
		fetchServiceProviderCommitLogs(response.getResult(), serviceProviderCallRequestDto);
		BigDecimal remittanceTransactionId = (BigDecimal) inputs.get("P_REMITTANCE_TRANSACTION_ID");
		updateServiceProviderDetails(response.getResult(), remittanceTransactionId);
		return response.getResult();
	}

}
