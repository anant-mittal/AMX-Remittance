package com.amx.jax.client;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.AppConfig;
import com.amx.jax.rest.RestService;

@Component
public class JaxPushNotificationClient extends AbstractJaxServiceClient {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public ApiResponse<CustomerNotificationDTO> getJaxPushNotification(BigDecimal customerId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndpoint.JAX_CUSTOMER_NOTIFICATION)
					.queryParam("customerId", customerId).header(getHeader()).get()
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
