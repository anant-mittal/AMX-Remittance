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
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.OtpPrefixDto;
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
		
			LOGGER.debug("in listOrders");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_LIST_ORDER;
			return restService.ajax(url).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryDetailDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		
			LOGGER.debug("in getDeliveryDetail");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_GET_ORDER_DETAIL;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryDetailDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markDelivered(
			FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		
			LOGGER.debug("in markDelivered");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_DELIVERED;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryMarkDeliveredRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markCancelled(
			FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkCancelledRequest) {
		
			LOGGER.debug("in markCancelled");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_CANCELLED;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryMarkCancelledRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {
		
			LOGGER.debug("in updateTransactionReceipt");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_TRANSACTION_RECEIPT;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(fcSaleDeliveryDetailUpdateReceiptRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<OtpPrefixDto, Object> sendOtp(BigDecimal deliveryDetailSeqId) {
		
			LOGGER.debug("in sendOtp");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_SEND_OTP;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OtpPrefixDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> verifyOtp(BigDecimal deliveryDetailSeqId, String mOtp) {
		
			LOGGER.debug("in verifyOtp");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_VERIFY_OTP;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId).field(Params.DELIVERY_DETAIL_OTP, mOtp)
					.postForm().as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ResourceDTO, Object> listDeliveryRemark() {
		
			LOGGER.debug("in listDeliveryRemark");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_LIST_DELIVERY_REMARK;
			return restService.ajax(url).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markReturn(BigDecimal deliveryDetailSeqId) {
		
			LOGGER.debug("in markReturn");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_RETURNED;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> markAcknowledged(BigDecimal deliveryDetailSeqId) {
		
			LOGGER.debug("in markAcknowledged");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_MARK_ACKNOWLEDGE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(Params.DELIVERY_DETAIL_SEQID, deliveryDetailSeqId).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<FxDeliveryDetailDto, Object> listHistoricalOrders() {
		
			LOGGER.debug("in listHistoricalOrders");
			String url = appConfig.getJaxURL() + Path.FX_DELIVERY_HISTORICAL_LIST_ORDER;
			return restService.ajax(url).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryDetailDto, Object>>() {
					});
		
	}

}
