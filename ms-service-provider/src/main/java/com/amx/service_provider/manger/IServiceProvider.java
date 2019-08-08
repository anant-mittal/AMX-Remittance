package com.amx.service_provider.manger;

import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

public interface IServiceProvider
{
	ServiceProviderResponse getQutation(ServiceProviderCallRequestDto quatationRequestDto);
	ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto);
}
