package com.amx.service_provider.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.IServiceProviderService;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.service.ServiceProviderService;

@RestController
@RequestMapping(IServiceProviderService.Path.PREFIX)
public class ServiceProviderController implements IServiceProviderService
{
	@Autowired
	ServiceProviderService serviceProviderGateService;

	@RequestMapping(value = IServiceProviderService.Path.GET_QUATATION, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderResponse, Object> getQuatation(@Valid @RequestBody ServiceProviderCallRequestDto quatationRequestDto)
	{
		return AmxApiResponse.build(serviceProviderGateService.getQutation(quatationRequestDto));
	}
	
	@RequestMapping(value = IServiceProviderService.Path.VALIDATE_REMITTANCE_INPUTS, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderResponse, Object> validateRemittanceInputs(
			@Valid @RequestBody ServiceProviderCallRequestDto validateRemittanceInputsRequestDto)
	{
		return AmxApiResponse.build(serviceProviderGateService.validateRemittanceInputs(validateRemittanceInputsRequestDto));
	}

	@RequestMapping(value = IServiceProviderService.Path.SEND_REMITTANCE, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(
			@Valid @RequestBody ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		return AmxApiResponse.build(serviceProviderGateService.sendRemittance(sendRemittanceRequestDto));
	}

	@RequestMapping(value = IServiceProviderService.Path.GET_REMITTANCE_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceDetails(
			@Valid @RequestBody ServiceProviderCallRequestDto getRemittanceDetailsRequestDto)
	{
		return AmxApiResponse.build(serviceProviderGateService.getRemittanceDetails(getRemittanceDetailsRequestDto));
	}

	@RequestMapping(value = IServiceProviderService.Path.GET_REMITTANCE_STATUS, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceStatus(
			@Valid @RequestBody ServiceProviderCallRequestDto getRemittanceStatusRequestDto)
	{
		return AmxApiResponse.build(serviceProviderGateService.getRemittanceStatus(getRemittanceStatusRequestDto));
	}
}
