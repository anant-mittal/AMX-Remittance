package com.amx.jax.tpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TpcService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TpcService.class);

	@Autowired
	TpcValidationService tpcValidationService;
	@Autowired
	TpcManager tpcManager;

	public void generateSecret(String clientId, String actualSecret) {
		LOGGER.info("TPC: generating client secret : {}", clientId);
		tpcValidationService.validateGenerateSecretRequeset(clientId, actualSecret);
		tpcManager.generateSecret(clientId, actualSecret);
	}
}
