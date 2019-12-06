package com.amx.jax.client.serviceprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Cancellation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Get_Rmittance_Details_Call_Response;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.model.response.serviceprovider.Status_Call_Response;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.utils.JsonUtil;

@Component
public class ServiceProviderClientWrapper {

	private static final Logger log = LoggerFactory.getLogger(ServiceProviderClientWrapper.class);

	@Autowired
	ServiceProviderClient serviceProviderClient;

	public AmxApiResponse<Quotation_Call_Response, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto) {
		log.debug("getQuatation request {}", JsonUtil.toJson(quatationRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.getQuatation(quatationRequestDto);
		Quotation_Call_Response result = (Quotation_Call_Response) response.getResult();
		log.debug("getQuatation response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<Remittance_Call_Response, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto) {
		log.info("sendRemittance request {}", JsonUtil.toJson(sendRemittanceRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.sendRemittance(sendRemittanceRequestDto);
		Remittance_Call_Response result = (Remittance_Call_Response) response.getResult();
		log.info("sendRemittance response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> validateRemittanceInputs(
			ServiceProviderCallRequestDto validateRemittanceInputsRequestDto) {
		log.debug("validateRemittanceInputs request {}", JsonUtil.toJson(validateRemittanceInputsRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.validateRemittanceInputs(validateRemittanceInputsRequestDto);
		Validate_Remittance_Inputs_Call_Response result = (Validate_Remittance_Inputs_Call_Response) response.getResult();
		log.debug("validateRemittanceInputs response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto) {
		log.debug("getRemittanceDetails request {}", JsonUtil.toJson(getRemittanceDetailsRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.getRemittanceDetails(getRemittanceDetailsRequestDto);
		Get_Rmittance_Details_Call_Response result = (Get_Rmittance_Details_Call_Response) response.getResult();
		log.debug("getRemittanceDetails response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto) {
		log.debug("getRemittanceStatus request {}", JsonUtil.toJson(getRemittanceStatusRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.getRemittanceStatus(getRemittanceStatusRequestDto);
		Status_Call_Response result = (Status_Call_Response) response.getResult();
		log.debug("getRemittanceStatus response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}
	
	public AmxApiResponse<ServiceProviderResponse, Object> cancelRemittance(ServiceProviderCallRequestDto cancelRemittanceRequestDto) {
		log.debug("cancelRemittance request {}", JsonUtil.toJson(cancelRemittanceRequestDto));
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.cancelRemittance(cancelRemittanceRequestDto);
		Cancellation_Call_Response result = (Cancellation_Call_Response) response.getResult();
		log.debug("cancelRemittance response {}", JsonUtil.toJson(result));
		return AmxApiResponse.build(result);
	}
}
