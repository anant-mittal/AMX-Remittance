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

import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.LoyaltyPointModel;
import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.service.BlackMasterService;
import com.amx.jax.service.ContactDetailService;
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
	
	@Autowired
	ContactDetailService contactDetailService;
	
	
	
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
	
	
	

	
	
	@RequestMapping(value = "/{countryId}/{companyId}/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomerDetailsByCustomerId(@PathVariable("countryId") BigDecimal countryId
			,@PathVariable("companyId") BigDecimal companyId,@PathVariable("customerId") BigDecimal customerId){
		List<Customer> customerDetailList = customerService.getCustomerByCustomerId(countryId, companyId, customerId);
		if (customerDetailList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + customerDetailList.size() + " Employees 1");
		logger.debug(customerDetailList);
		logger.debug(Arrays.toString(customerDetailList.toArray()));
		return new ResponseEntity<List<Customer>>(customerDetailList, HttpStatus.OK);
		
	}
	
	
	/*
	@RequestMapping(value = "/lylpointU/{loyaltyPoint}/{countryId}/{companyId}/{customerId}", method = RequestMethod.GET)
	public void  updateCustomerLoyaltyPoint(@PathVariable("loyaltyPoint") BigDecimal loyaltyPoint,@PathVariable("countryId") BigDecimal countryId
			,@PathVariable("companyId") BigDecimal companyId,@PathVariable("customerId") BigDecimal customerId){
		logger.info("loyaltyPoint :"+loyaltyPoint+"\t countryId :"+countryId+"\t companyId :"+companyId+"\t customerId :"+customerId);
		customerService.updateLoyaltyPoint(loyaltyPoint, countryId, companyId, customerId);
		
	}*/
	
	
	
	
	
	
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
	
	
	
	@RequestMapping(value = "/onlinecheck/{companyId}/{countryId}/{civilid}", method = RequestMethod.GET)
	public ResponseEntity<List<ViewOnlineCustomerCheck>> civilIdStatusForOnlineFromView(@PathVariable("companyId") BigDecimal companyId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("civilid") String civilid){
		List<ViewOnlineCustomerCheck> onlinecustomerDetailList = customerOnlineServiceFromView.civilIdCheckForOnlineUser(companyId, countryId, civilid);
		if (onlinecustomerDetailList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<ViewOnlineCustomerCheck>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + onlinecustomerDetailList.size() + " Employees 1");
		logger.debug(onlinecustomerDetailList);
		logger.debug(Arrays.toString(onlinecustomerDetailList.toArray()));
		return new ResponseEntity<List<ViewOnlineCustomerCheck>>(onlinecustomerDetailList, HttpStatus.OK);
		
	}
	
	
	
	
	@RequestMapping(value = "/idproof/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<List<CustomerIdProof>> getOnlineCustomer(@PathVariable("customerId") BigDecimal customerId){
		List<CustomerIdProof> customerIdProofList = customerIdProofService.getCustomerIdProofByCustomerId(customerId);
		if (customerIdProofList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CustomerIdProof>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + customerIdProofList.size() + " Employees 1");
		logger.debug(customerIdProofList);
		logger.debug(Arrays.toString(customerIdProofList.toArray()));
		return new ResponseEntity<List<CustomerIdProof>>(customerIdProofList, HttpStatus.OK);
		
	}
	
	
	
	
	
	/** 
	 * 
	 * @param name
	 * @return Fetch Contact details
	 */
	
	
	
	@RequestMapping(value = "/contact/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<List<ContactDetail>> getCustomerContactDetails(@PathVariable("customerId") BigDecimal customerId){
		logger.info("getCustomerContactDetails customerId :"+customerId);
		List<ContactDetail> contactDetailList = contactDetailService.getContactDetail(customerId);
		if (contactDetailList.isEmpty()) {
			logger.debug("contactDetailList does not exists");
			return new ResponseEntity<List<ContactDetail>>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found " + contactDetailList.size() + " Employees 1");
		logger.debug(contactDetailList);
		logger.debug(Arrays.toString(contactDetailList.toArray()));
		return new ResponseEntity<List<ContactDetail>>(contactDetailList, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/contact/{customerId}/{contactId}", method = RequestMethod.GET)
	public ResponseEntity<List<ContactDetail>> getCustomerContactDetails(@PathVariable("customerId") BigDecimal customerId,@PathVariable("contactId") BigDecimal contactId){
		logger.info("getCustomerContactDetails customerId :"+customerId);
		List<ContactDetail> contactDetailList = contactDetailService.getContactDetailByCotactId(customerId, contactId);
		if (contactDetailList.isEmpty()) {
			logger.debug("contactDetailList does not exists");
			return new ResponseEntity<List<ContactDetail>>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found " + contactDetailList.size() + " Employees 1");
		logger.debug(contactDetailList);
		logger.debug(Arrays.toString(contactDetailList.toArray()));
		return new ResponseEntity<List<ContactDetail>>(contactDetailList, HttpStatus.OK);
		
	}
	
	
	
	@RequestMapping(value = "/blist/{name}", method = RequestMethod.GET)
	public ResponseEntity<List<BlackListModel>> getBlackList(@PathVariable("name") String name){
		logger.info("name :"+name);
		List<BlackListModel> blackList = blackMasterService.getBlackList(name);
		if (blackList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<BlackListModel>>(HttpStatus.OK);
		}
		logger.debug("Found " + blackList.size() + " Employees 1");
		logger.debug(blackList);
		logger.debug(Arrays.toString(blackList.toArray()));
		return new ResponseEntity<List<BlackListModel>>(blackList, HttpStatus.OK);
		
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
