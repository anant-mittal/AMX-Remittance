package com.amx.jax.client.serviceprovider;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

public interface IServiceProviderService
{
	public static class Path
	{
		public static final String PREFIX = "";//"/service-provider";
		public static final String GET_QUATATION = PREFIX + "/getQuataion/";
		public static final String SEND_REMITTANCE = PREFIX + "/sendRemittance/";
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto);
}