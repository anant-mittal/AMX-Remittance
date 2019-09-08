package com.amx.service_provider.manger;

import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

public interface IServiceProvider
{
	ServiceProviderResponse getQutation(ServiceProviderCallRequestDto quatationRequestDto);
	ServiceProviderResponse validateRemittanceInputs(ServiceProviderCallRequestDto validateRemittanceInputsRequestDto);
	ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto);
	ServiceProviderResponse getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto);
	ServiceProviderResponse getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto);	
}
