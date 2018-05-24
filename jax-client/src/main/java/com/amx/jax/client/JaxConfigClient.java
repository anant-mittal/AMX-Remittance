package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.rest.RestService;

@Component
public class JaxConfigClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(JaxConfigClient.class);

	@Autowired
	private RestService restService;

	public ApiResponse<BooleanResponse> createorUpdateOtpSettings(OtpSettings otpSettings) {
		try {

			String url = this.getBaseUrl() + "/config/";
			HttpEntity<OtpSettings> requestEntity = new HttpEntity<OtpSettings>(otpSettings, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in createorUpdateOtpSettings : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

}
