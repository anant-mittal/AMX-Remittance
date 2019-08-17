package com.amx.jax.fxorder.controller;

import static com.amx.amxlib.constant.ApiEndpoint.FX_ORDER_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.services.CustomerRatingService;

@RestController
@RequestMapping(FX_ORDER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class FxOrderController {
	
	@Autowired
	CustomerRatingService customerRatingService;

	
	
	@RequestMapping(value = "/save-customer-rating/", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRating, ?> saveCustomerRating(@RequestBody @Valid CustomerRatingDTO dto) {
		return customerRatingService.fxOrdersaveCustomerRating(dto);
	}

	// radhika
	@RequestMapping(value = "/customer-trnx-rating/", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRating, ?> inquireCustomerRating(@RequestParam BigDecimal remittanceTrnxId,String product) {
		return customerRatingService.fxOrderinquireCustomerRating(remittanceTrnxId, product);

	}

}
