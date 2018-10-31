package com.amx.jax.controller;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.service.BlackMasterService;
import com.amx.jax.service.OnlineCustomerService;

/**
 * 
 * @author : Rabil
 * Date    : 17/11/2017
 *
 */

@RestController
@RequestMapping("/user")
public class MetaUserController {

	final static Logger logger = Logger.getLogger(MetaUserController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	OnlineCustomerService onlineCustomerService;
	
	@Autowired
	BlackMasterService blackMasterService;
		
	@RequestMapping(value = "/{countryId}/{userId}", method = RequestMethod.GET)
	public ApiResponse getCustomerDetailsResponse(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		return customerService.getCustomer(countryId, userId);
	}
	
	@RequestMapping(value = "/{countryId}/{companyId}/{customerId}", method = RequestMethod.GET)
	public ApiResponse getCustomerDetailsByCustomerIdResponse(@PathVariable("countryId") BigDecimal countryId
			,@PathVariable("companyId") BigDecimal companyId,@PathVariable("customerId") BigDecimal customerId){
		return customerService.getCustomerByCustomerId(countryId, companyId, customerId);
	}
	
	@RequestMapping(value = "/online/{countryId}/{userId}", method = RequestMethod.GET)
	public ApiResponse getOnlineCustomerResponse(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		return onlineCustomerService.getOnlineCustomerList(countryId, userId);
	}
	
	@RequestMapping(value = "/blist/{name}", method = RequestMethod.GET)
	public ApiResponse getBlackListResponse(@PathVariable("name") String name){
		return  blackMasterService.getBlackList(name);
	}
}
