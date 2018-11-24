package com.amx.jax.client.fx;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceStateService.Params;
import com.amx.jax.client.IDeviceStateService.Path;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.response.DeviceStatusInfoDto;
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

}
