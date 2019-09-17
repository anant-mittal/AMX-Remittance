package com.amx.jax.client.serviceprovider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
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
	public AmxApiResponse<Quotation_Call_Response, Object> getQuatation(
			ServiceProviderCallRequestDto quatationRequestDto)
	{
		try
		{

			LOGGER.debug("in get quataion :");
			return restService.ajax(appConfig.getServiceProviderURL() + Path.GET_QUATATION).meta(new JaxMetaInfo())
					.post(quatationRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<Quotation_Call_Response, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in getQuatation : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> sendRemittance(
			ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		try
		{
			LOGGER.debug("in send remittance :");
			return restService.ajax(appConfig.getJaxURL() + Path.SEND_REMITTANCE).meta(new JaxMetaInfo())
					.post(sendRemittanceRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in sendRemittance : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> validateRemittanceInputs(
			ServiceProviderCallRequestDto validateRemittanceInputsRequestDto)
	{
		try
		{
			LOGGER.debug("in validateRemittanceInputs :");
			return restService.ajax(appConfig.getJaxURL() + Path.VALIDATE_REMITTANCE_INPUTS).meta(new JaxMetaInfo())
					.post(validateRemittanceInputsRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in validateRemittanceInputs : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceDetails(
			ServiceProviderCallRequestDto getRemittanceDetailsRequestDto)
	{
		try
		{
			LOGGER.debug("in getRemittanceDetails :");
			return restService.ajax(appConfig.getJaxURL() + Path.GET_REMITTANCE_DETAILS).meta(new JaxMetaInfo())
					.post(getRemittanceDetailsRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in getRemittanceDetails : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ServiceProviderResponse, Object> getRemittanceStatus(
			ServiceProviderCallRequestDto getRemittanceStatusRequestDto)
	{
		try
		{
			LOGGER.debug("in getRemittanceStatus :");
			return restService.ajax(appConfig.getJaxURL() + Path.GET_REMITTANCE_STATUS).meta(new JaxMetaInfo())
					.post(getRemittanceStatusRequestDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderResponse, Object>>()
					{
					});
		}
		catch (Exception e)
		{
			LOGGER.error("exception in getRemittanceStatus : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

}
