package com.amx.jax.meta.api;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AmxSharedConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
public class MetaConfigController implements AmxSharedConfig {

	private static final Logger LOGGER = LoggerService.getLogger(MetaConfigController.class);

	@Autowired
	private AmxSharedConfigDB amxSharedConfigDB;

	@RequestMapping(value = AmxSharedConfigApi.Path.COMM_PREFS, method = RequestMethod.GET)
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs() {
		return amxSharedConfigDB.getCommunicationPrefs();
	}

}
