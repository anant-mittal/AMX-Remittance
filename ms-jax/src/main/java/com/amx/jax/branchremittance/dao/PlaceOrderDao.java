package com.amx.jax.branchremittance.dao;

import java.util.HashMap;

import javax.transaction.Transactional;

/**
 * @author rabil
 * @date :05 Nov 2019
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.remittance.RatePlaceOrder;
import com.amx.jax.repository.IRatePlaceOrderRepository;

@Component
public class PlaceOrderDao {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	IRatePlaceOrderRepository ratePlaceOrderRepository;
	
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void savePlaceOrder(HashMap<String, Object> mapPlaceOrder) {
		RatePlaceOrder rateApplTrnx = (RatePlaceOrder) mapPlaceOrder.get("EX_RATE_PLACE_ORD");
		if(rateApplTrnx!=null) {
			ratePlaceOrderRepository.save(rateApplTrnx);
		}
	}
}
