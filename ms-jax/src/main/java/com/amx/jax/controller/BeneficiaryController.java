package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.service.BeneficiaryOnlineService;

/**
 * 
 * @author   :Rabil
 * Purpose   :Beneficiary related function  
 *
 */
@RestController
@RequestMapping(BENE_API_ENDPOINT)
public class BeneficiaryController {
	final static Logger logger = Logger.getLogger(BeneficiaryController.class);
	
	@Autowired
	BeneficiaryOnlineService beneOnlineService;
	

	
	@RequestMapping(value = "/beneonline/{userType}/{customerId}/{applicationCountryId}/{beneCountryId}", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryOnlineListResponse(@PathVariable("userType") String userType,@PathVariable("customerId") BigDecimal customerId,@PathVariable("applicationCountryId") BigDecimal applicationCountryId,
			@PathVariable("beneCountryId") BigDecimal beneCountryId) {
		logger.info("userType :"+userType+"\t customerId :"+customerId+"\t applicationCountryId :"+applicationCountryId+"\t beneCountryId :"+beneCountryId);
		ApiResponse response =null;
		if(userType!=null && userType.equalsIgnoreCase("BR")) {
			response = beneOnlineService.getBeneficiaryListForBranch(customerId, applicationCountryId,beneCountryId);
		}else {
			response = beneOnlineService.getBeneficiaryListForOnline(customerId, applicationCountryId,beneCountryId);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/benecountry/{userType}/{customerId}", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListResponse(@PathVariable("userType") String userType,@PathVariable("customerId") BigDecimal customerId) {
		logger.info("userType :"+userType+"\t customerId :"+customerId);
		ApiResponse response ;
		if(userType!=null && userType.equalsIgnoreCase("BR")) {
		 response = beneOnlineService.getBeneficiaryCountryListForBranch(customerId);
		}else {
			 response = beneOnlineService.getBeneficiaryCountryListForOnline(customerId);
		}
		return response;
	}
	
	
	
	
}
