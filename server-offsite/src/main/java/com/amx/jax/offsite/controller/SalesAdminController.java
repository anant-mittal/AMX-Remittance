package com.amx.jax.offsite.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.AbstractProbotInterface.ApiEndPoints;
import com.amx.jax.pricer.dto.RoutingProductStatusDetails;
import com.amx.jax.pricer.dto.RoutingStatusUpdateRequestDto;

import io.swagger.annotations.Api;

/**
 * @author Anil
 *
 */

@RestController
@Api(value = "SalesAdmin  APIs")
public class SalesAdminController {
	
	private static final Logger LOGGER = LoggerService.getLogger(SalesAdminController.class);
	
	@Autowired
	private PricerServiceClient pricerServiceClient;
	
	
	
	@RequestMapping(value = ApiEndPoints.GET_ROUTING_PRODUCT_STATUS, method = RequestMethod.POST)
	public AmxApiResponse<RoutingProductStatusDetails, Object> getRoutingProductStatus(
			@RequestParam(required = true) BigDecimal countryId, @RequestParam(required = true) BigDecimal currencyId) {
		return pricerServiceClient.getRoutingProductStatus(countryId, currencyId);
	}

	
	@RequestMapping(value = ApiEndPoints.UPDATE_ROUTING_STATUS, method = RequestMethod.POST)
	public AmxApiResponse<Integer, Object> updateRoutingProductStatus(RoutingStatusUpdateRequestDto request) {
		return pricerServiceClient.updateRoutingProductStatus(request);
	}

}
