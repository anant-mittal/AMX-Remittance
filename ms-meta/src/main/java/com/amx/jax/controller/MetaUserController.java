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

import com.amx.jax.model.Customer;
import com.amx.jax.model.CustomerOnlineRegistration;
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
	
	
	
	@RequestMapping(value = "/{countryId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomerDetails(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		List<Customer> customerDetailList = customerService.getCustomer(countryId, userId);
		if (customerDetailList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + customerDetailList.size() + " Employees 1");
		logger.debug(customerDetailList);
		logger.debug(Arrays.toString(customerDetailList.toArray()));
		return new ResponseEntity<List<Customer>>(customerDetailList, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/online/{countryId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<CustomerOnlineRegistration>> getOnlineCustomer(@PathVariable("countryId") BigDecimal countryId,@PathVariable("userId") String userId){
		List<CustomerOnlineRegistration> onlinecustomerDetailList = onlineCustomerService.getOnlineCustomerList(countryId, userId);
		if (onlinecustomerDetailList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CustomerOnlineRegistration>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + onlinecustomerDetailList.size() + " Employees 1");
		logger.debug(onlinecustomerDetailList);
		logger.debug(Arrays.toString(onlinecustomerDetailList.toArray()));
		return new ResponseEntity<List<CustomerOnlineRegistration>>(onlinecustomerDetailList, HttpStatus.OK);
		
	}

}
