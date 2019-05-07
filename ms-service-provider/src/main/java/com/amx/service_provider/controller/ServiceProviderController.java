package com.amx.service_provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.IServiceProviderService;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.service.ServiceProviderService;

@RestController
@RequestMapping(IServiceProviderService.Path.PREFIX)
public class ServiceProviderController implements IServiceProviderService
{
	@Autowired
	ServiceProviderService serviceProviderGateService;

	@RequestMapping(value = IServiceProviderService.Path.GET_QUATATION, method = RequestMethod.GET)
	public AmxApiResponse<ServiceProviderResponse, Object> getQuatation(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data)
	{
		return AmxApiResponse.build(serviceProviderGateService.getQutation(txn_data, customer_data, bene_data));
	}

	@RequestMapping(value = IServiceProviderService.Path.SEND_REMITTANCE, method = RequestMethod.GET)
	public AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data)
	{
		return AmxApiResponse.build(serviceProviderGateService.sendRemittance(txn_data, customer_data, bene_data));
	}
}
