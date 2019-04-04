package com.amx.jax.controller;
import static com.amx.amxlib.constant.ApiEndpoint.EKYC_ENDPOINT;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.EKyc;
import com.amx.amxlib.model.EKycModel;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.userservice.service.EKycService;
@RestController
@RequestMapping(EKYC_ENDPOINT)
@SuppressWarnings("rawtypes")
public class EKycController {
	@Autowired
	EKycService eKycService;
	
	@RequestMapping(value = EKyc.EKYC_SAVE_CUSTOMER, method = RequestMethod.POST)
	public BoolRespModel eKycSaveCustomer(@RequestBody ImageSubmissionRequest imageSubmissionRequest) throws ParseException{
		return eKycService.eKycsaveCustomer(imageSubmissionRequest);
		
	}
	
}
