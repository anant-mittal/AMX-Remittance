package com.amx.jax.client.fx;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.OtpPrefixDto;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;

public interface IFxOrderDelivery extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/delivery";
		public static final String FX_DELIVERY_LIST_ORDER = PREFIX + "/list-orders/";
		public static final String FX_DELIVERY_GET_ORDER_DETAIL = PREFIX + "/get-order/";
		public static final String FX_DELIVERY_MARK_DELIVERED = PREFIX + "/mark-delivered/";
		public static final String FX_DELIVERY_MARK_CANCELLED = PREFIX + "/mark-cancelled/";
		public static final String FX_DELIVERY_MARK_RETURNED = PREFIX + "/mark-returned/";
		public static final String FX_DELIVERY_MARK_ACKNOWLEDGE = PREFIX + "/acknowledge-order/";
		public static final String FX_DELIVERY_TRANSACTION_RECEIPT = PREFIX + "/update-trnx-receipt/";
		public static final String FX_DELIVERY_SEND_OTP = PREFIX + "/send-otp/";
		public static final String FX_DELIVERY_VERIFY_OTP = PREFIX + "/verify-otp/";
		public static final String FX_DELIVERY_LIST_DELIVERY_REMARK = PREFIX + "/list-del-remark/";
		public static final String FX_DELIVERY_HISTORICAL_LIST_ORDER = PREFIX + "/list-orders-historical/";
	}

	public static class Params {
		public static final String DELIVERY_DETAIL_SEQID = "deliveryDetailSeqId";
		public static final String DELIVERY_DETAIL_OTP = "mOtp";
	}

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> listOrders();

	@ApiJaxStatus({ JaxError.JAX_FIELD_VALIDATION_FAILURE })
	AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.INVALID_EMPLOYEE,
			JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS })
	AmxApiResponse<BoolRespModel, Object> markDelivered(
			FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.INVALID_EMPLOYEE,
			JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS })
	AmxApiResponse<BoolRespModel, Object> markCancelled(
			FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.INVALID_EMPLOYEE })
	AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.INVALID_EMPLOYEE })
	AmxApiResponse<OtpPrefixDto, Object> sendOtp(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.MISSING_OTP, JaxError.INVALID_OTP,
			JaxError.INVALID_EMPLOYEE })
	AmxApiResponse<BoolRespModel, Object> verifyOtp(BigDecimal deliveryDetailSeqId, String mOtp);

	AmxApiResponse<ResourceDTO, Object> listDeliveryRemark();

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.INVALID_EMPLOYEE,
			JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS })
	AmxApiResponse<BoolRespModel, Object> markReturn(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus({ JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS,
			JaxError.INVALID_EMPLOYEE, JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS })
	AmxApiResponse<BoolRespModel, Object> markAcknowledged(BigDecimal deliveryDetailSeqId);

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> listHistoricalOrders();

}
