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
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.OtpPrefixDto;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.services.FcSaleDeliveryService;

import io.swagger.annotations.ApiOperation;

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
	@ApiOperation("Lists order for driver employee present in metadata")
	public AmxApiResponse<FxDeliveryDetailDto, Object> listOrders() {
		List<FxDeliveryDetailDto> resultList = fcSaleDeliveryService.listOrders();
		return AmxApiResponse.buildList(resultList);
	}

	@RequestMapping(value = Path.FX_DELIVERY_GET_ORDER_DETAIL, method = RequestMethod.GET)
	@Override
	@ApiOperation("Fetch delivery detail of given delivery detail sequence id")
	public AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(@RequestParam BigDecimal deliveryDetailSeqId) {
		FxDeliveryDetailDto result = fcSaleDeliveryService.getDeliveryDetail(deliveryDetailSeqId);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_MARK_DELIVERED, method = RequestMethod.POST)
	@Override
	@ApiOperation("Marks the order status as delivered")
	public AmxApiResponse<BoolRespModel, Object> markDelivered(
			@RequestBody FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		logger.info("markDelivered requestemail started from: {}");
		BoolRespModel result = fcSaleDeliveryService.markDelivered(fcSaleDeliveryMarkDeliveredRequest);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_MARK_CANCELLED, method = RequestMethod.POST)
	@Override
	@ApiOperation("Marks the order status as not delivered")
	public AmxApiResponse<BoolRespModel, Object> markCancelled(
			@RequestBody FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		BoolRespModel result = fcSaleDeliveryService.markCancelled(fcSaleDeliveryMarkNotDeliveredRequest);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_TRANSACTION_RECEIPT, method = RequestMethod.POST)
	@Override
	@ApiOperation("Update receipt clob for given delivery details")
	public AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			@RequestBody FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {
		BoolRespModel result = fcSaleDeliveryService.updateTransactionReceipt(fcSaleDeliveryDetailUpdateReceiptRequest);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_SEND_OTP, method = RequestMethod.GET)
	@Override
	@ApiOperation("Send otp to the customer")
	public AmxApiResponse<OtpPrefixDto, Object> sendOtp(@RequestParam BigDecimal deliveryDetailSeqId) {
		OtpPrefixDto result = fcSaleDeliveryService.sendOtp(deliveryDetailSeqId, true);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_VERIFY_OTP, method = RequestMethod.POST)
	@Override
	@ApiOperation("Verify otp sent to customer")
	public AmxApiResponse<BoolRespModel, Object> verifyOtp(@RequestParam BigDecimal deliveryDetailSeqId,
			@RequestParam String mOtp) {
		BoolRespModel result = fcSaleDeliveryService.verifyOtp(deliveryDetailSeqId, mOtp);
		return AmxApiResponse.build(result);
	}

	@ApiOperation("Lists all active delivery remarks")
	@RequestMapping(value = Path.FX_DELIVERY_LIST_DELIVERY_REMARK, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<ResourceDTO, Object> listDeliveryRemark() {
		List<ResourceDTO> resultList = fcSaleDeliveryService.listDeliveryRemark();
		return AmxApiResponse.buildList(resultList);
	}

	@RequestMapping(value = Path.FX_DELIVERY_MARK_RETURNED, method = RequestMethod.POST)
	@Override
	@ApiOperation("Return the envolope")
	public AmxApiResponse<BoolRespModel, Object> markReturn(@RequestParam BigDecimal deliveryDetailSeqId) {
		BoolRespModel result = fcSaleDeliveryService.markReturn(deliveryDetailSeqId);
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.FX_DELIVERY_MARK_ACKNOWLEDGE, method = RequestMethod.POST)
	@Override
	@ApiOperation("Acknowledge the fx order")
	public AmxApiResponse<BoolRespModel, Object> markAcknowledged(@RequestParam BigDecimal deliveryDetailSeqId) {
		BoolRespModel result = fcSaleDeliveryService.markAcknowledged(deliveryDetailSeqId);
		return AmxApiResponse.build(result);
	}
	
	
	/**
	 * @return fx order delivery details for historical for emp id in meta
	 * 
	 */
	@RequestMapping(value = Path.FX_DELIVERY_HISTORICAL_LIST_ORDER, method = RequestMethod.GET)
	@Override
	@ApiOperation("Lists historical order for driver employee present in metadata")
	public AmxApiResponse<FxDeliveryDetailDto, Object> listHistoricalOrders() {
		List<FxDeliveryDetailDto> resultList = fcSaleDeliveryService.listHistoricalOrders();
		return AmxApiResponse.buildList(resultList);
	}
}
