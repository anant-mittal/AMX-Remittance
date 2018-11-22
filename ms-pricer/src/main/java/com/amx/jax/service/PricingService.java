/**
 * 
 */
package com.amx.jax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;

/**
 * @author abhijeet
 *
 */
@Service
public class PricingService {

	@Autowired
	CustomerDao customerDao;

	public PricingRespDTO fetchRemitPriceForCustomer(PricingReqDTO pricingReqDTO) {

		Customer customer = customerDao.getCustById(pricingReqDTO.getCustomerId());
		
		

		return null;
	}

}
