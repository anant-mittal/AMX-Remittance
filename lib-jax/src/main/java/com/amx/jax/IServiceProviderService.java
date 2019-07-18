package com.amx.jax;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.response.serviceprovider.ServiceProviderDefaultDateDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderPartnerDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderSummaryDTO;

public interface IServiceProviderService extends IJaxService {
	
	public static class ServiceProviderApiEndpoints {
		public static final String PREFIX = "/service-provider";
		
		public static final String SERVICE_PROVIDER_PARTNER = "/service-provider-partner";
		
		public static final String SERVICE_PROVIDER_UPLOAD_FILE="/service-provider-upload-file";
		
		public static final String SERVICE_PROVIDER_CONFIRMATION="/service-provider-confirmation";
		
		public static final String SERVICE_PROVIDER_DEFAULT_DATE = "/service-provider-default-date";
	}
	public static class Params {
		public static final String FILE_DATE="fileDate";
		public static final String TPC_CODE="tpcCode";
		public static final String FILE = "file";
	}
	
	
	AmxApiResponse<ServiceProviderPartnerDTO, Object> getServiceProviderPartner();
	AmxApiResponse<ServiceProviderSummaryDTO, Object> uploadServiceProviderFile(MultipartFile file, Date fileDate, String tpcCode)throws Exception; 
	AmxApiResponse<BoolRespModel, Object> serviceProviderConfirmation(Date fileDate, String tpcCode);
	AmxApiResponse<ServiceProviderDefaultDateDTO, Object> getServiceProviderDefaultDate(String tpcCode);
}
