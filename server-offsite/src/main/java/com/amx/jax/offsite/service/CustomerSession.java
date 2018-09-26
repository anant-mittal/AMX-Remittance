package com.amx.jax.offsite.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.logger.LoggerService;

/**
 * The Class OffsiteService.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerSession {

	/** The log. */
	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	private BigDecimal customerId;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public void clear() {
		this.customerId = new BigDecimal("2840");
	}

}
