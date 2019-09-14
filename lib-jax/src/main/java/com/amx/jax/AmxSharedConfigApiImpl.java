package com.amx.jax;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.AmxSharedConfigApi;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class AmxSharedConfigApiImpl implements AmxSharedConfigApi {

	private static final Logger LOGGER = LoggerService.getLogger(AmxSharedConfigApiImpl.class);

	@Autowired
	private RestService restService;

	@Autowired
	private AppConfig appConfig;

	@Override
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(Path.COMM_PREFS)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CommunicationPrefs, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getCommunicationPrefs : ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch
	}

}
