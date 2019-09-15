package com.amx.service_provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.manger.ServiceProviderManger;

@Service
public class ServiceProviderService 
{
	@Autowired
	ServiceProviderManger serviceProviderGateWayManger;

	public ServiceProviderResponse getQutation(ServiceProviderCallRequestDto quatationRequestDto)
	{
		// TODO: see if you need to get data from Repo to get the customer and bene
		// objects using incoming Ids only
		return serviceProviderGateWayManger.getQutation(quatationRequestDto);
	}
	
	public ServiceProviderResponse validateRemittanceInputs(ServiceProviderCallRequestDto validateRemittanceInputsRequestDto)
	{
		return serviceProviderGateWayManger.validateRemittanceInputs(validateRemittanceInputsRequestDto);
	}

	public ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		return serviceProviderGateWayManger.sendRemittance(sendRemittanceRequestDto);
	}
	
	public ServiceProviderResponse getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto)
	{
		return serviceProviderGateWayManger.getRemittanceDetails(getRemittanceDetailsRequestDto);
	}

	public ServiceProviderResponse getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto)
	{
		return serviceProviderGateWayManger.getRemittanceStatus(getRemittanceStatusRequestDto);
	}
}
