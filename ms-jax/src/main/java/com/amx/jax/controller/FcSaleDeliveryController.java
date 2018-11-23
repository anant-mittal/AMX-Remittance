package com.amx.jax.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IFxOrderDelivery;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.services.FcSaleService;

@RestController
public class FcSaleDeliveryController implements IFxOrderDelivery {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleService fcSaleService;

	@Autowired
	MetaData metaData;

	/**
	 * @return fx order delivery details for today for emp id in meta
	 * 
	 */
	@RequestMapping(value = Path.FX_DELIVERY_LIST_ORDER, method = RequestMethod.GET)
	public AmxApiResponse<FxDeliveryDetailDto, Object> listOrders() {
		return null;
	}

}
