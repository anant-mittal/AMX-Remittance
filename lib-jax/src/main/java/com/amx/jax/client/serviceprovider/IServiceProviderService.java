package com.amx.jax.client.serviceprovider;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

public interface IServiceProviderService
{
	public static class Path
	{
		public static final String PREFIX = "";//"/service-provider";
		public static final String GET_QUATATION = PREFIX + "/getQuataion/";
		public static final String VALIDATE_REMITTANCE_INPUTS = PREFIX + "/validateRemittanceInputs/";
		public static final String SEND_REMITTANCE = PREFIX + "/sendRemittance/";
		public static final String GET_REMITTANCE_DETAILS = PREFIX + "/getRemittanceDetails/";
		public static final String GET_REMITTANCE_STATUS = PREFIX + "/getRemittanceStatus/";		
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> validateRemittanceInputs(ServiceProviderCallRequestDto validateRemittanceInputsRequestDto);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto);
}