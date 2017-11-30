package com.amx.jax.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dbmodel.LoyaltyPointModel;
import com.amx.jax.service.BlackMasterService;
import com.amx.jax.service.CustomerIdProofService;
import com.amx.jax.service.CustomerOnlineServiceFromView;
import com.amx.jax.service.CustomerService;
import com.amx.jax.service.LoyaltyPointService;
import com.amx.jax.service.OnlineCustomerService;

/**
 * 
 * @author : Rabil
 * Date    : 17/11/2017
 *
 */

@RestController
@RequestMapping("/user")
public class MetaUserController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7446679687593857144L;
	final static Logger logger = Logger.getLogger(MetaUserController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	OnlineCustomerService onlineCustomerService;
	
	@Autowired
	CustomerIdProofService customerIdProofService;
	
	@Autowired
	BlackMasterService blackMasterService;
	
	@Autowired
	LoyaltyPointService loyaltyPointService;
	
	@Autowired
	CustomerOnlineServiceFromView customerOnlineServiceFromView;
	
	@RequestMapping(value = "/{countryId}/{userId}", method = RequestMethod.GET)
	public ApiResponse getCustomerDetailsResponse(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		ApiResponse response = customerService.getCustomer(countryId, userId);
		return response;
	}
	
	@RequestMapping(value = "/{countryId}/{companyId}/{customerId}", method = RequestMethod.GET)
	public ApiResponse getCustomerDetailsByCustomerIdResponse(@PathVariable("countryId") BigDecimal countryId
			,@PathVariable("companyId") BigDecimal companyId,@PathVariable("customerId") BigDecimal customerId){
		ApiResponse response = customerService.getCustomerByCustomerId(countryId, companyId, customerId);
		return response;
	}
	
	

	
	
	/*
	@RequestMapping(value = "/lylpointU/{loyaltyPoint}/{countryId}/{companyId}/{customerId}", method = RequestMethod.GET)
	public void  updateCustomerLoyaltyPoint(@PathVariable("loyaltyPoint") BigDecimal loyaltyPoint,@PathVariable("countryId") BigDecimal countryId
			,@PathVariable("companyId") BigDecimal companyId,@PathVariable("customerId") BigDecimal customerId){
		logger.info("loyaltyPoint :"+loyaltyPoint+"\t countryId :"+countryId+"\t companyId :"+companyId+"\t customerId :"+customerId);
		customerService.updateLoyaltyPoint(loyaltyPoint, countryId, companyId, customerId);
		
	}*/
	
	
	
	
	@RequestMapping(value = "/online/{countryId}/{userId}", method = RequestMethod.GET)
	public ApiResponse getOnlineCustomerResponse(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		ApiResponse response = onlineCustomerService.getOnlineCustomerList(countryId, userId);
		return response;
	}
	
	
	@RequestMapping(value = "/onlinecheck/{companyId}/{countryId}/{civilid}", method = RequestMethod.GET)
	public ApiResponse civilIdStatusForOnlineFromViewResponse(@PathVariable("companyId") BigDecimal companyId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("civilid") String civilid){
		ApiResponse response = customerOnlineServiceFromView.civilIdCheckForOnlineUser(companyId, countryId, civilid);
		return response;
	}
	
	@RequestMapping(value = "/imageVal/{customerId}/{identityTypeId}", method = RequestMethod.GET)
	public ApiResponse getCustomerImageValidationResponse(@PathVariable("customerId") BigDecimal customerId,@PathVariable("identityTypeId") BigDecimal identityTypeId){
		ApiResponse response = customerIdProofService.getCustomerImageValidation(customerId, identityTypeId);
		return response;
	}
	
	@RequestMapping(value = "/blist/{name}", method = RequestMethod.GET)
	public ApiResponse getBlackListResponse(@PathVariable("name") String name){
		ApiResponse response =  blackMasterService.getBlackList(name);
		return response;
	}
	
	
	
	@RequestMapping(value = "/loyaltyPoint/{cusref}/{fYear}", method = RequestMethod.GET)
	public ResponseEntity<List<LoyaltyPointModel>> getLoyaltyFromOldTable(@PathVariable("cusref") BigDecimal cusref,@PathVariable("fYear") BigDecimal fYear){
		logger.info("getLoyaltyFromOldTable name :"+cusref+"\t Year :"+fYear);
		List<LoyaltyPointModel> loyaltyPointList = loyaltyPointService.getLoyaltyPointFromLoyaltyTable(cusref, fYear);
		if (loyaltyPointList.isEmpty()) {
			logger.debug("loyaltyPointList does not exists");
			return new ResponseEntity<List<LoyaltyPointModel>>(HttpStatus.OK);
		}
		logger.debug("Found " + loyaltyPointList.size() + " Employees 1");
		logger.debug(loyaltyPointList);
		logger.debug(Arrays.toString(loyaltyPointList.toArray()));
		return new ResponseEntity<List<LoyaltyPointModel>>(loyaltyPointList, HttpStatus.OK);
	}
	


}
