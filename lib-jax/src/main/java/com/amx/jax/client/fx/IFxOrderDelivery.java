package com.amx.jax.client.fx;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;

public interface IFxOrderDelivery extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/delivery";
		public static final String FX_DELIVERY_LIST_ORDER = PREFIX + "/list-orders/";
		public static final String FX_DELIVERY_GET_ORDER_DETAIL = PREFIX + "/get-order/";
	}

	public static class Params {
		public static final String DELIVERY_DETAIL_SEQID = "deliveryDetailSeqId";
	}

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> listOrders();

	@ApiJaxStatus(JaxError.JAX_FIELD_VALIDATION_FAILURE)
	AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(BigDecimal deliveryDetailSeqId);

}
