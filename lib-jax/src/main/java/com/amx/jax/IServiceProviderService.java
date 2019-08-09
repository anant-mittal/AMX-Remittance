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
		public static final String PREFIX = "/revenue-report";
		
		public static final String REVENUE_REPORT_PARTNER = "/revenue-report-partner";
		
		public static final String REVENUE_REPORT_UPLOAD_FILE="/revenue-report-upload-file";
		
		public static final String REVENUE_REPORT_CONFIRMATION="/revenue-report-confirmation";
		
		public static final String REVENUE_REPORT_DEFAULT_DATE = "/revenue-report-default-date";
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
