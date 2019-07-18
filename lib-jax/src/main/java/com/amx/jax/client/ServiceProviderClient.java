package com.amx.jax.client;

import java.sql.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.AppConfig;
import com.amx.jax.IServiceProviderService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.response.serviceprovider.ServiceProviderDefaultDateDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderPartnerDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderSummaryDTO;
import com.amx.jax.rest.RestService;

@Component
public class ServiceProviderClient implements IServiceProviderService{
	private static final Logger LOGGER = Logger.getLogger(ServiceProviderClient.class);
	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;
	
	
	
	@Override
	public AmxApiResponse<ServiceProviderPartnerDTO, Object> getServiceProviderPartner() {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(ServiceProviderApiEndpoints.PREFIX + ServiceProviderApiEndpoints.SERVICE_PROVIDER_PARTNER).meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderPartnerDTO, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in get service partner : ", ae);
			return JaxSystemError.evaluate(ae);
		}
		
	}

	@Override
	public AmxApiResponse<ServiceProviderSummaryDTO, Object> uploadServiceProviderFile(MultipartFile file,
			Date fileDate, String tpcCode) throws Exception {
		
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(ServiceProviderApiEndpoints.PREFIX + ServiceProviderApiEndpoints.SERVICE_PROVIDER_UPLOAD_FILE)
					.queryParam(Params.FILE, file).queryParam(Params.FILE_DATE,fileDate).queryParam(Params.TPC_CODE,tpcCode).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderSummaryDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in upload service provider file: ", e);
			return JaxSystemError.evaluate(e);
		} 
	}
	

	@Override
	public AmxApiResponse<BoolRespModel, Object> serviceProviderConfirmation(Date fileDate, String tpcCode) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(ServiceProviderApiEndpoints.PREFIX + ServiceProviderApiEndpoints.SERVICE_PROVIDER_CONFIRMATION)
					.queryParam(Params.FILE_DATE,fileDate).queryParam(Params.TPC_CODE,tpcCode).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in upload service provider confirmation: ", e);
			return JaxSystemError.evaluate(e);
		} 
		
	}

	@Override
	public AmxApiResponse<ServiceProviderDefaultDateDTO, Object> getServiceProviderDefaultDate(String tpcCode) {
		
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(ServiceProviderApiEndpoints.PREFIX + ServiceProviderApiEndpoints.SERVICE_PROVIDER_DEFAULT_DATE)
					.queryParam(Params.TPC_CODE,tpcCode).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ServiceProviderDefaultDateDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in get service provider date: ", e);
			return JaxSystemError.evaluate(e);
		} 
	}

	
}
