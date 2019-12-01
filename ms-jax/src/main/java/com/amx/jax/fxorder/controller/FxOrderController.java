package com.amx.jax.fxorder.controller;
/**
 * Author : Radhika
 * Date   : 09/07/2019
 */

import static com.amx.amxlib.constant.ApiEndpoint.FX_ORDER_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.services.CustomerRatingService;

@RestController
@RequestMapping(FX_ORDER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class FxOrderController {
	
	@Autowired
	CustomerRatingService customerRatingService;

	
	


			

}
