package com.amx.jax.branch.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.RevenueReportServiceClient;
import com.amx.jax.response.serviceprovider.ServiceProviderDefaultDateDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderPartnerDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderSummaryDTO;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Service Provider Api")
public class ServiceProviderBranchController {

	@Autowired
	RevenueReportServiceClient revenueReportServiceClient;

	@RequestMapping(value = "/api/service-provider/list", method = { RequestMethod.GET })
	public AmxApiResponse<ServiceProviderPartnerDTO, Object> getServiceProviderPartner() {
		return revenueReportServiceClient.getServiceProviderPartner();
	}

	@RequestMapping(value = "/api/service-provider/defaultdate", method = { RequestMethod.POST })
	public AmxApiResponse<ServiceProviderDefaultDateDTO, Object> getServiceProviderDefaultDate(
			@RequestParam(value = "tpcCode", required = true) String tpcCode) {
		return revenueReportServiceClient.getServiceProviderDefaultDate(tpcCode);
	}

	@RequestMapping(value = "/api/service-provider/confirm", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> serviceProviderConfirmation(@RequestParam Date fileDate,
			@RequestParam String tpcCode) {
		return revenueReportServiceClient.serviceProviderConfirmation(fileDate, tpcCode);
	}

	@RequestMapping(value = "/api/service-provider/fileupload", method = { RequestMethod.POST })
	public AmxApiResponse<ServiceProviderSummaryDTO, Object> uploadServiceProviderFile(@RequestParam MultipartFile file,
			@RequestParam Date fileDate, @RequestParam String tpcCode) throws Exception {
		return revenueReportServiceClient.uploadServiceProviderFile(file, fileDate, tpcCode);
	}

}
