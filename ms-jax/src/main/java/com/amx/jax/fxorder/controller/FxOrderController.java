package com.amx.jax.fxorder.controller;
/**
 * Author : Radhika
 * Date   : 09/07/2019
 */

import static com.amx.amxlib.constant.ApiEndpoint.FX_ORDER_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.IFxBranchOrderService.Path;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.services.CustomerRatingService;

@RestController
@RequestMapping(FX_ORDER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class FxOrderController {
	
	@Autowired
	CustomerRatingService customerRatingService;

	
	@RequestMapping(value = "/save-fxorder-customer-rating/", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRating, ?> saveFxorderCustomerRating(@RequestBody @Valid CustomerRatingDTO dto) {
		return customerRatingService.fxOrdersaveCustomerRating(dto);
	}


		@RequestMapping(value = Path.FC_CUSTOMER_RATING , method = RequestMethod.POST)
		public AmxApiResponse<CustomerRating, ?> inquireFxOrderCustomerRating(@RequestParam BigDecimal deliveryDetailSeqId,String product) {
			return customerRatingService.fxOrderinquireCustomerRating(deliveryDetailSeqId, product);

		}

	

}
