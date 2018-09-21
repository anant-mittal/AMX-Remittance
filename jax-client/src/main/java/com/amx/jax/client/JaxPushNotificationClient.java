package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_NOTIFICATION_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.PushNotificationRecordDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.rest.RestService;

@Component
public class JaxPushNotificationClient extends AbstractJaxServiceClient {
	// private static final Logger LOGGER =
	// Logger.getLogger(JaxPushNotificationClient.class);
	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	@Autowired
	RestService restService;

	public ApiResponse<PushNotificationRecordDTO> getJaxPushNotification(String customerId) {
		try {
			StringBuilder sb = new StringBuilder();
				sb.append("?customerId=").append(customerId);
			
			String url = this.getBaseUrl() + JAX_NOTIFICATION_ENDPOINT + "/jax/notification/" + sb.toString();

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PushNotificationRecordDTO>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {

			throw new JaxSystemError();
		}

	}
}
