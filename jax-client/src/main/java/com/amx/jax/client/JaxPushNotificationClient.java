package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_NOTIFICATION_ENDPOINT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class JaxPushNotificationClient extends AbstractJaxServiceClient {
	// private static final Logger LOGGER =
	// Logger.getLogger(JaxPushNotificationClient.class);

	@Autowired
	RestService restService;

	public ApiResponse<CustomerNotificationDTO> getJaxPushNotification(BigDecimal customerId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("?customerId=").append(customerId);

			String url = this.getBaseUrl() + JAX_NOTIFICATION_ENDPOINT + "/jax/notification/" + sb.toString();

			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerNotificationDTO>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {

			throw new JaxSystemError();
		}

	}
	
	
	public ApiResponse<CustomerNotificationDTO> saveCustomerPushNotification(CustomerNotificationDTO customerNotificationDTO) {
		try {
			HttpEntity<CustomerNotificationDTO> requestEntity = new HttpEntity<CustomerNotificationDTO>(customerNotificationDTO);
			String url = this.getBaseUrl() + JAX_NOTIFICATION_ENDPOINT + "/jax/notification/save";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerNotificationDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}
}
