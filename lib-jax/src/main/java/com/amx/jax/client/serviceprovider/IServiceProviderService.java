package com.amx.jax.client.serviceprovider;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;

public interface IServiceProviderService
{
	public static class Path
	{
		public static final String PREFIX = "";//"/service-provider";
		public static final String GET_QUATATION = PREFIX + "/getQuataion/";
		public static final String SEND_REMITTANCE = PREFIX + "/sendRemittance/";
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<Quotation_Call_Response, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<Remittance_Call_Response, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto);
}