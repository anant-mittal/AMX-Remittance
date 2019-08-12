package com.amx.jax.branch.serviceprovider.controller;

import static com.amx.amxlib.constant.ApiEndpoint.REVENUE_REPORT_ENDPOINT;

import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.amxlib.constant.ApiEndpoint.ServiceProvider;
import com.amx.jax.IServiceProviderService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.response.serviceprovider.ServiceProviderDefaultDateDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderPartnerDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderSummaryDTO;
import com.amx.jax.serviceprovider.service.ServiceProviderService;
@RestController
@RequestMapping(REVENUE_REPORT_ENDPOINT)
@SuppressWarnings("rawtypes")
public class ServiceProviderController implements IServiceProviderService{

	Logger logger = Logger.getLogger(ServiceProviderController.class);
	
	@Autowired
	ServiceProviderService serviceProviderService;
	
	@RequestMapping(value = ServiceProvider.REVENUE_REPORT_PARTNER, method = RequestMethod.GET)
	public AmxApiResponse<ServiceProviderPartnerDTO, Object> getServiceProviderPartner() {
		List<ServiceProviderPartnerDTO> response = serviceProviderService.getServiceProviderPartner();
		return AmxApiResponse.buildList(response);

	}
	
	@RequestMapping(value= ServiceProvider.REVENUE_REPORT_UPLOAD_FILE, method = RequestMethod.POST)
	public AmxApiResponse<ServiceProviderSummaryDTO, Object> uploadServiceProviderFile(@RequestParam MultipartFile file, @RequestParam Date fileDate, @RequestParam String tpcCode) throws Exception{
		List<ServiceProviderSummaryDTO> response = serviceProviderService.uploadServiceProviderFile(file,fileDate,tpcCode);
		return AmxApiResponse.buildList(response);
	}
	
	@RequestMapping(value= ServiceProvider.REVENUE_REPORT_CONFIRMATION, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> serviceProviderConfirmation(@RequestParam Date fileDate, @RequestParam String tpcCode){
		BoolRespModel boolRespModel = serviceProviderService.serviceProviderConfirmation(fileDate,tpcCode);
		return AmxApiResponse.build(boolRespModel);
	}
	
	@RequestMapping(value=ServiceProvider.REVENUE_REPORT_DEFAULT_DATE, method=RequestMethod.POST)
	public AmxApiResponse<ServiceProviderDefaultDateDTO, Object> getServiceProviderDefaultDate(@RequestParam String tpcCode){
		ServiceProviderDefaultDateDTO defaultUploadDate = serviceProviderService.getServiceProviderDefaultDate(tpcCode);
		return AmxApiResponse.build(defaultUploadDate);
	}
	
	

}
