package com.amx.jax.client;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CustomerNotifyHubDTO;
import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class CustomerNotifyHubClient extends AbstractJaxServiceClient {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public AmxApiResponse<CustomerNotifyHubDTO, Object> get(BigDecimal customerId) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndpoint.JAX_CUSTOMER_NOTIFICATION)
					.queryParam("customerId", customerId).header(getHeader()).get()
					.asApiResponse(CustomerNotifyHubDTO.class);
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			throw new JaxSystemError();
		}

	}

	public AmxApiResponse<Object, Object> save(CustomerNotifyHubDTO customerNotificationDTO) {
		return save(Collections.singletonList(customerNotificationDTO));
	}

	public AmxApiResponse<Object, Object> save(List<CustomerNotifyHubDTO> customerNotificationDTOs) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndpoint.JAX_CUSTOMER_NOTIFICATION)
					.post(customerNotificationDTOs).asApiResponse();
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}
}
