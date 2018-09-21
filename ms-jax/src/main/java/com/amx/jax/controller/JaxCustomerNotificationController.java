package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_NOTIFICATION_ENDPOINT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PushNotificationRecordDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PushNotificationRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.JaxPushNotificationService;
import com.amx.jax.userservice.service.UserService;

@RestController
@RequestMapping(JAX_NOTIFICATION_ENDPOINT)
@SuppressWarnings("rawtypes")
public class JaxCustomerNotificationController {

	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	JaxPushNotificationService jaxPushNotificationService;

	@Autowired
	UserService userService;

	@Autowired
	MetaData metaData;

	@RequestMapping(value = "/jax/notification/", method = RequestMethod.GET)
	public ApiResponse getNotificationForCustomer(
			@RequestParam(required = true, value = "customerId") BigDecimal customerId) {

		Customer customer = userService.getCustById(customerId);
		BigDecimal nationalityId = customer.getNationalityId();

		BigDecimal countryId = metaData.getCountryId();

		logger.info("In GET Jax Push Notification Controller ------ ");
		ApiResponse response = jaxPushNotificationService.getJaxNotification(customerId, nationalityId, countryId);

		return response;

	}

	@RequestMapping(value = "/jax/notification/save", method = RequestMethod.POST)
	public ApiResponse saveJaxPushNotification(@RequestBody PushNotificationRecord jaxPushNotification) {

		logger.info("In SAVE Push Notification Controller ------ " + jaxPushNotification.toString());

		ApiResponse response = jaxPushNotificationService.saveJaxPushNotification(jaxPushNotification);

		return response;

	}

}
