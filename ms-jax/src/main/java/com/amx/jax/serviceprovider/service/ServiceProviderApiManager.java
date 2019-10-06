package com.amx.jax.serviceprovider.service;

import java.math.BigDecimal;
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
import com.amx.jax.model.request.serviceprovider.ServiceProviderLogDTO;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.ServiceProviderDto;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants;
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
	@Autowired
	PartnerTransactionManager partnerTransactionManager;

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
	
	public Validate_Remittance_Inputs_Call_Response validateApiVentajaInput(DynamicRoutingPricingDto dynamicRoutingPricingDto,Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		return response.getResult();
	}
	
	protected ServiceProviderCallRequestDto createSaveInputRequest(Map<String, Object> remitRemitParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();

		
		return serviceProviderCallRequestDto;
	}

	public void setAdditionalFlexFieldParams(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			List<JaxFieldDto> requiredFlexFields) {

	}

	public Remittance_Call_Response sendRemittance(Map<String, Object> remitApplParametersMap) {
		Map<String, Object> inputs = new HashMap<>();
		inputs.putAll(remitApplParametersMap);
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(inputs);
		AmxApiResponse<Remittance_Call_Response, Object> response = serviceProviderClientWrapper.sendRemittance(serviceProviderCallRequestDto);
		return response.getResult();
	}

	protected void parseValidateResponseForError(ServiceProviderResponse result) {
		
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
	
	// store xml logs
	public void fetchServiceProviderLogs(ServiceProviderResponse srvPrvResp,ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		String requestXml = null;
		String responseXml = null;
		String referenceNo = null;
		BigDecimal customerReference = null;
		String partnerReference = null;
		String bankCode = null;
		String trnxType = PricerServiceConstants.SEND_TRNX;
		BigDecimal reqSeq = new BigDecimal(1);
		BigDecimal resSeq = new BigDecimal(2);
		if(serviceProviderCallRequestDto != null && srvPrvResp != null) {
			if(serviceProviderCallRequestDto.getCustomerDto() != null && serviceProviderCallRequestDto.getCustomerDto().getCustomer_reference() != null) {
				customerReference = new BigDecimal(serviceProviderCallRequestDto.getCustomerDto().getCustomer_reference());
			}
			if(serviceProviderCallRequestDto.getTransactionDto() != null &&  serviceProviderCallRequestDto.getTransactionDto().getRequest_sequence_id() != null) {
				referenceNo = serviceProviderCallRequestDto.getTransactionDto().getRequest_sequence_id();
			}
			if(serviceProviderCallRequestDto.getTransactionDto() != null) {
				if(serviceProviderCallRequestDto.getTransactionDto().getPartner_transaction_reference() != null) {
					partnerReference = serviceProviderCallRequestDto.getTransactionDto().getPartner_transaction_reference();
				}
				if(serviceProviderCallRequestDto.getTransactionDto().getRoutting_bank_code() != null) {
					bankCode = serviceProviderCallRequestDto.getTransactionDto().getRoutting_bank_code();
				}
			}
			if(srvPrvResp.getRequest_XML() != null) {
				requestXml = srvPrvResp.getRequest_XML();
				ServiceProviderLogDTO serviceProviderXmlLog = partnerTransactionManager.saveServiceProviderXMLlogData(PricerServiceConstants.VALIDATE_REQUEST, requestXml, referenceNo, reqSeq, PricerServiceConstants.REQUEST, trnxType, partnerReference, customerReference, bankCode);
				if(serviceProviderXmlLog != null) {
					partnerTransactionManager.saveServiceProviderXml(serviceProviderXmlLog);
				}
			}
			if(srvPrvResp.getResponse_XML() != null) {
				responseXml = srvPrvResp.getResponse_XML();
				ServiceProviderLogDTO serviceProviderXmlLog = partnerTransactionManager.saveServiceProviderXMLlogData(PricerServiceConstants.VALIDATE_RESPONSE, responseXml, referenceNo, resSeq, PricerServiceConstants.RESPONSE, trnxType, partnerReference, customerReference, bankCode);
				if(serviceProviderXmlLog != null) {
					partnerTransactionManager.saveServiceProviderXml(serviceProviderXmlLog);
				}
			}
		}
	}
	
	// setting service provider details
	public void fetchServiceProviderData(DynamicRoutingPricingDto dynamicRoutingPricingDto,ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		
		ServiceProviderDto serviceProviderDto = new ServiceProviderDto();
		
		if(serviceProviderCallRequestDto != null && serviceProviderCallRequestDto.getTransactionDto() != null && serviceProviderCallRequestDto.getTransactionDto().getRequest_sequence_id() != null) {
			String amgSessionId = serviceProviderCallRequestDto.getTransactionDto().getRequest_sequence_id();
			serviceProviderDto.setAmgSessionId(new BigDecimal(amgSessionId));
		}
		
		serviceProviderDto.setFixedCommInSettlCurr(null);
		serviceProviderDto.setIntialAmountInSettlCurr(null);
		serviceProviderDto.setOfferExpirationDate(null);
		serviceProviderDto.setOfferStartingDate(null);
		serviceProviderDto.setPartnerReferenceNo(null);
		serviceProviderDto.setPartnerSessionId(null);
		serviceProviderDto.setSettlementCurrency(null);
		serviceProviderDto.setTransactionMargin(null);
		serviceProviderDto.setVariableCommInSettlCurr(null);
		
		dynamicRoutingPricingDto.setServiceProviderDto(serviceProviderDto);
		
	}
}
