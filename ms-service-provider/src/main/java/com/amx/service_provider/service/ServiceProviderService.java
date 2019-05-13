package com.amx.service_provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

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

	public ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		// TODO: see if you need to get data from Repo to get the customer and bene
		// objects using incoming Ids only
		return serviceProviderGateWayManger.sendRemittance(sendRemittanceRequestDto);
	}

	

}
