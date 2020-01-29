package com.amx.jax.branchremittance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author rabil
 * @date   28 April 2019
 */
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DynamicRoutingPricingService {
	
	static final Logger LOGGER = LoggerFactory.getLogger(DynamicRoutingPricingService.class);

//ExchangeRateAndRoutingResponse
}
