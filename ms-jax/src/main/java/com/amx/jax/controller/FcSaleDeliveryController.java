package com.amx.jax.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.fx.IFxOrderDelivery;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.services.FcSaleDeliveryService;
import com.amx.jax.services.FcSaleService;

@RestController
public class FcSaleDeliveryController implements IFxOrderDelivery {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleDeliveryService fcSaleDeliveryService;
	@Autowired
	MetaData metaData;

	/**
	 * @return fx order delivery details for today for emp id in meta
	 * 
	 */
	@RequestMapping(value = Path.FX_DELIVERY_LIST_ORDER, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> listOrders() {
		List<FxDeliveryDetailDto> resultList = fcSaleDeliveryService.listOrders();
		return AmxApiResponse.buildList(resultList);
	}

	@RequestMapping(value = Path.FX_DELIVERY_GET_ORDER_DETAIL, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(@RequestParam BigDecimal deliveryDetailSeqId) {
		FxDeliveryDetailDto result = fcSaleDeliveryService.getDeliveryDetail(deliveryDetailSeqId);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_MARK_DELIVERED, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> markDelivered(
			@RequestBody FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		BoolRespModel result = fcSaleDeliveryService.markDelivered(fcSaleDeliveryMarkDeliveredRequest);
		return AmxApiResponse.build(result);
	}
	
	@RequestMapping(value = Path.FX_DELIVERY_MARK_NOT_DELIVERED, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> markNotDelivered(
			@RequestBody FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		BoolRespModel result = fcSaleDeliveryService.markNotDelivered(fcSaleDeliveryMarkNotDeliveredRequest);
		return AmxApiResponse.build(result);
	}
	
	@RequestMapping(value = Path.FX_DELIVERY_TRANSACTION_RECEIPT, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			@RequestBody FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {
		BoolRespModel result = fcSaleDeliveryService.updateTransactionReceipt(fcSaleDeliveryDetailUpdateReceiptRequest);
		return AmxApiResponse.build(result);
	}
	
}
