package com.amx.jax.branchremittance.manager;

import java.io.Serializable;

/**
 * @author rabil 
 * @date 10/29/2019
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.RatePlaceOrder;
import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class PlaceOrderManager implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//IRatePlaceOrderRepository
	
	
	public RatePlaceOrder createPlaceOrder(PlaceOrderRequestModel placeOrderRequestModel) {
		RatePlaceOrder placeOrderAppl = new RatePlaceOrder();
		//BranchRe
		//BenificiaryListView beneficaryDetails =getBeneDetails(placeOrderRequestModel);
		
		
		
		return placeOrderAppl;
	}

}
