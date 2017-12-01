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
import com.amx.jax.service.CustomerService;
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
	BlackMasterService blackMasterService;
	
	
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
	
	
	@RequestMapping(value = "/blist/{name}", method = RequestMethod.GET)
	public ApiResponse getBlackListResponse(@PathVariable("name") String name){
		ApiResponse response =  blackMasterService.getBlackList(name);
		return response;
	}


}
