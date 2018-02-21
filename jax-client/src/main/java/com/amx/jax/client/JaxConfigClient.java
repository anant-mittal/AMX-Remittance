package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.config.OtpSettings;

@Component
public class JaxConfigClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(JaxConfigClient.class);

	public ApiResponse<BooleanResponse> createorUpdateOtpSettings(OtpSettings otpSettings) {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String url = this.getBaseUrl() + "/config/";
			HttpEntity<OtpSettings> requestEntity = new HttpEntity<OtpSettings>(otpSettings, getHeader());
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in createorUpdateOtpSettings : ",e);
            throw new JaxSystemError();
        } // end of try-catch
	}

}
