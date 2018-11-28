package com.amx.jax.client.fx;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;

public interface IFxOrderDelivery extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/delivery";
		public static final String FX_DELIVERY_LIST_ORDER = PREFIX + "/list-orders/";
		public static final String FX_DELIVERY_GET_ORDER_DETAIL = PREFIX + "/get-order/";
		public static final String FX_DELIVERY_MARK_DELIVERED = PREFIX + "/mark-delivered/";
		public static final String FX_DELIVERY_MARK_NOT_DELIVERED = PREFIX + "/mark-not-delivered/";
		public static final String FX_DELIVERY_TRANSACTION_RECEIPT = PREFIX + "/update-trnx-receipt/";
		public static final String FX_DELIVERY_SEND_OTP = PREFIX + "/send-otp/";
		public static final String FX_DELIVERY_VERIFY_OTP = PREFIX + "/verify-otp/";
	}

	public static class Params {
		public static final String DELIVERY_DETAIL_SEQID = "deliveryDetailSeqId";
		public static final String DELIVERY_DETAIL_OTP = "otp";
	}

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> listOrders();

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND)
	AmxApiResponse<BoolRespModel, Object> markDelivered(
			FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest);

	@ApiJaxStatus(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND)
	AmxApiResponse<BoolRespModel, Object> markNotDelivered(
			FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest);

	@ApiJaxStatus(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND)
	AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest);

	@ApiJaxStatus(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND)
	AmxApiResponse<BoolRespModel, Object> sendOtp(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.MISSING_OTP, JaxError.INVALID_OTP })
	AmxApiResponse<BoolRespModel, Object> verifyOtp(BigDecimal deliveryDetailSeqId, BigDecimal mOtp);

}
