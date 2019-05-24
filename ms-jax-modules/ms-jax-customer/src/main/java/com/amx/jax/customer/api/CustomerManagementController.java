package com.amx.jax.customer.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.response.customer.CreateCustomerRequest;

@RestController
public class CustomerManagementController {

	private static final Logger log = LoggerFactory.getLogger(CustomerManagementController.class);

	@RequestMapping(path = "/create")
	public AmxApiResponse<BoolRespModel, Object> createCustomer(
			@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
		log.debug("request received is {}", createCustomerRequest);
		return AmxApiResponse.build();

	}

}
