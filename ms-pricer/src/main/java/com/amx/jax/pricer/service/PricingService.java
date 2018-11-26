/**
 * 
 */
package com.amx.jax.pricer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;
import com.amx.jax.pricer.manager.CustomerDiscountManager;
import com.amx.jax.pricer.manager.RemitPriceManager;

/**
 * @author abhijeet
 *
 */
@Service
public class PricingService {

	@Autowired
	CustomerDao customerDao;

	@Autowired
	RemitPriceManager remitPriceManager;
	
	@Autowired
	CustomerDiscountManager customerDiscountManager;

	public PricingRespDTO fetchRemitPricesForCustomer(PricingReqDTO pricingReqDTO) {

		Customer customer = customerDao.getCustById(pricingReqDTO.getCustomerId());
		
		

		return null;
	}

}
