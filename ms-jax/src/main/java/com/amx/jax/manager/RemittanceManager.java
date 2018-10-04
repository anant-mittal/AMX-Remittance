package com.amx.jax.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.repository.IPlaceOrderDao;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceManager {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	IPlaceOrderDao placeOrderDao;

	/**
	 * method will be called after successful remittance
	 * @param remittanceTransaction 
	 * 
	 */
	@Async
	public void afterRemittanceSteps(RemittanceTransaction remittanceTransaction) {
		//if(condition for place order) {
		PlaceOrder po = placeOrderDao.getPlaceOrderForRemittanceTransactionId(remittanceTransaction.getRemittanceTransactionId());
		logger.info("sending email and push notification for place order");
		//}
	}
}
