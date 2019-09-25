package com.amx.jax.client.serviceprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

@Component
public class ServiceProviderClientWrapper {

	@Autowired
	ServiceProviderClient serviceProviderClient;

	public AmxApiResponse<Quotation_Call_Response, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto) {
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.getQuatation(quatationRequestDto);
		Quotation_Call_Response result = (Quotation_Call_Response) response.getResult();
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<Remittance_Call_Response, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto) {
		AmxApiResponse<ServiceProviderResponse, Object> response = serviceProviderClient.sendRemittance(sendRemittanceRequestDto);
		Remittance_Call_Response result = (Remittance_Call_Response) response.getResult();
		return AmxApiResponse.build(result);
	}

	public AmxApiResponse<ServiceProviderResponse, Object> validateRemittanceInputs(
			ServiceProviderCallRequestDto validateRemittanceInputsRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}
}
