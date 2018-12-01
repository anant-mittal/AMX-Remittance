package com.amx.jax.offsite.controller;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.fx.IFxOrderDelivery;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
@RequestMapping(value = "/api/fxodelivery", produces = { CommonMediaType.APPLICATION_JSON_VALUE,
		CommonMediaType.APPLICATION_V0_JSON_VALUE })
@ApiStatusService(IFxOrderDelivery.class)
public class FxDeliveryController {

	private Logger logger = Logger.getLogger(FxDeliveryController.class);

	@Autowired
	private IFxOrderDelivery fxOrderDelivery;

	@Autowired
	CustomerRegistrationClient customerRegistrationClient;

	@RequestMapping(value = "/order/list", method = { RequestMethod.GET })
	public AmxApiResponse<FxDeliveryDetailDto, Object> listOrders() {
		return fxOrderDelivery.listOrders();
	}

	@RequestMapping(value = "/order/details", method = { RequestMethod.GET })
	public AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(@RequestParam BigDecimal deliveryDetailSeqId) {
		return fxOrderDelivery.getDeliveryDetail(deliveryDetailSeqId);
	}

	@RequestMapping(value = "/customer/verify", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> verifyOtp(
			@RequestParam BigDecimal deliveryDetailSeqId, @RequestParam BigDecimal mOtp) {
		return fxOrderDelivery.verifyOtp(deliveryDetailSeqId, mOtp);
	}

	@RequestMapping(value = "/customer/resend", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> sendOtp(
			@RequestParam BigDecimal deliveryDetailSeqId) {
		return fxOrderDelivery.sendOtp(deliveryDetailSeqId);
	}

	@RequestMapping(value = "/order/delivered", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> markDelivered(
			@RequestBody FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		return fxOrderDelivery.markDelivered(fcSaleDeliveryMarkDeliveredRequest);
	}

	@RequestMapping(value = "/order/notdelivered", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> markNotDelivered(
			@RequestBody FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		return fxOrderDelivery.markNotDelivered(fcSaleDeliveryMarkDeliveredRequest);
	}

	@RequestMapping(value = "/order/reasons", method = { RequestMethod.GET })
	public AmxApiResponse<ResourceDTO, Object> listDeliveryRemark() {
		return fxOrderDelivery.listDeliveryRemark();
	}

	@RequestMapping(value = "/order/upload", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			@RequestBody FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryMarkDeliveredRequest) {
		return fxOrderDelivery.updateTransactionReceipt(fcSaleDeliveryMarkDeliveredRequest);
	}

}
