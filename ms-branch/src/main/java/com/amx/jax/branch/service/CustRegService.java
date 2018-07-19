package com.amx.jax.branch.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.ICustRegService;
import com.amx.jax.logger.LoggerService;

@Component
public class CustRegService implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegService.class);

}
