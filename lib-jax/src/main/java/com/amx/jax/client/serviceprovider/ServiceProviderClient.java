package com.amx.jax.client.serviceprovider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.fx.IFxOrderService.Path;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.rest.RestService;

@Component
public class ServiceProviderClient implements IServiceProviderService
{

	private static final Logger LOGGER = Logger.getLogger(ServiceProviderClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> getQuatation(ServiceProviderCallRequestDto quatationRequestDto)
	{
		try
		{
		
			LOGGER.debug("in get quataion :");
			return restService.ajax(appConfig.getJaxURL() + Path.GET_QUATATION).meta(new JaxMetaInfo()).post(quatationRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		try
		{
			LOGGER.debug("in send remittance :");
			return restService.ajax(appConfig.getJaxURL() + Path.SEND_REMITTANCE).meta(new JaxMetaInfo()).post(sendRemittanceRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in getCurrencyByCountryId : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

}
