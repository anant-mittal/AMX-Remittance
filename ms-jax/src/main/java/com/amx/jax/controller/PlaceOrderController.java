package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.PLACE_ORDER_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.PlaceOrderService;

/**
 * @author Subodh Bhoir
 *
 */
@RestController
@RequestMapping(PLACE_ORDER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class PlaceOrderController {
	
	private Logger logger = Logger.getLogger(PlaceOrderController.class);
	
	@Autowired
	PlaceOrderService placeOrderService;
	
	@Autowired
	MetaData metaData;
	
	/**
	 *  saves place order
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ApiResponse handleUrlSave(@RequestBody PlaceOrderDTO dto) 
	{
		logger.info("In save with parameters" + dto.toString());
		BigDecimal customerId = metaData.getCustomerId();
		
		dto.setCustomerId(customerId);
		ApiResponse response = null;
		response = placeOrderService.savePlaceOrder(dto);
		return response;
	}

	/**
	 * get place order list
	 * @return
	 */
	@RequestMapping(value = "/get/placeOrder", method = RequestMethod.POST)
	public ApiResponse handleUrlgetPlaceOrder() {
		
		BigDecimal customerId = metaData.getCustomerId();
		logger.info("In /getPlaceOrder with customerId :" + customerId );
		
		ApiResponse response = null;
		response = placeOrderService.getPlaceOrder(customerId);
		return response;
		
	}

}
