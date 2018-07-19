package com.amx.jax.branch.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.ICustRegService;
import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;

@Component
public class CustRegService implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegService.class);

	@Override
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel regModeModel) {
		return null;
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return null;
	}

}
