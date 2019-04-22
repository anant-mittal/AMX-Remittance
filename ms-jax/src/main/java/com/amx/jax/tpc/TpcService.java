package com.amx.jax.tpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.response.tpc.TpcGenerateClientSecretResponse;

@Service
public class TpcService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TpcService.class);

	@Autowired
	TpcValidationService tpcValidationService;
	@Autowired
	TpcManager tpcManager;

	public TpcGenerateClientSecretResponse generateSecret(String clientId, String actualClientSecret) {
		LOGGER.info("TPC: generating client secret : {}", clientId);

		tpcValidationService.validateGenerateSecretRequeset(clientId, actualClientSecret);
		LOGGER.debug("TPC: hashing : {}", clientId);
		String clientSecretHash = tpcManager.generateClientSecretHash(clientId, actualClientSecret);
		LOGGER.debug("TPC: encyrpt : {}", clientId);
		String clientSecret = tpcManager.encryptClientSecret(clientSecretHash);
		TpcGenerateClientSecretResponse response = new TpcGenerateClientSecretResponse();
		response.setClientSecret(clientSecret);
		return response;
	}
}
