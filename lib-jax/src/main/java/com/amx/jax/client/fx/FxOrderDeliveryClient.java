package com.amx.jax.client.fx;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.ResourceDto;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.rest.RestService;

@Component
public class FxOrderDeliveryClient implements IFxOrderDelivery {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> listOrders() {
		try {
			LOGGER.debug("in listOrders");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_LIST_ORDER;
			return restService.ajax(url).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryDetailDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listOrders : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		try {
			LOGGER.debug("in getDeliveryDetail");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_GET_ORDER_DETAIL;
			return restService.ajax(url).queryParam(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryDetailDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getDeliveryDetail : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markDelivered(
			FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		try {
			LOGGER.debug("in markDelivered");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_DELIVERED;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryMarkDeliveredRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in markDelivered : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markNotDelivered(
			FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		try {
			LOGGER.debug("in markNotDelivered");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_NOT_DELIVERED;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryMarkNotDeliveredRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in markNotDelivered : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {
		try {
			LOGGER.debug("in updateTransactionReceipt");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_TRANSACTION_RECEIPT;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryDetailUpdateReceiptRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in updateTransactionReceipt : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> sendOtp(BigDecimal deliveryDetailSeqId) {
		try {
			LOGGER.debug("in sendOtp");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_SEND_OTP;
			return restService.ajax(url).queryParam(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> verifyOtp(BigDecimal deliveryDetailSeqId, BigDecimal mOtp) {
		try {
			LOGGER.debug("in verifyOtp");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_VERIFY_OTP;
			return restService.ajax(url).field(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId)
					.field(Params.DELIVERY_DETAIL_OTP, mOtp).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in verifyOtp : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<ResourceDto, Object> listDeliveryRemark() {
		try {
			LOGGER.debug("in listDeliveryRemark");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_LIST_DELIVERY_REMARK;
			return restService.ajax(url).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listDeliveryRemark : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
