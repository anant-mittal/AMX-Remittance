package com.amx.jax.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.amxlib.model.CustomerNotifyHubDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.service.CustomerNotifyHubService;
import com.amx.jax.dbmodel.customer.CustomerNotifyHubRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.UserService;

@RestController
public class CustomerNotifyHubController {

	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	CustomerNotifyHubService jaxPushNotificationService;

	@Autowired
	UserService userService;

	@Autowired
	MetaData metaData;

	@RequestMapping(value = ApiEndpoint.JAX_CUSTOMER_NOTIFICATION, method = RequestMethod.GET)
	public AmxApiResponse<CustomerNotifyHubDTO, ?> getNotificationForCustomer(
			@RequestParam(required = true, value = "customerId") BigDecimal customerId) {
		return jaxPushNotificationService.get(customerId);

	}

	@RequestMapping(value = ApiEndpoint.JAX_CUSTOMER_NOTIFICATION, method = RequestMethod.POST)
	public AmxApiResponse<Object, Object> saveJaxPushNotification(
			@RequestBody List<CustomerNotifyHubRecord> jaxPushNotifications) {
		logger.info("In SAVE Push Notification Controller ------ " + jaxPushNotifications.toString());
		return jaxPushNotificationService.save(jaxPushNotifications);
	}

}
