package com.amx.jax.client.meta;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.request.fx.ForexOutLookRequest;
import com.amx.jax.model.response.fx.CurrencyPairDTO;
import com.amx.jax.model.response.fx.ForexOutLookResponseDTO;
import com.amx.jax.rest.RestService;

@Component
public class ForexOutlookClient implements  IForexOutlookService{
	
	private static final Logger LOGGER = LoggerService.getLogger(ForexOutlookClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;


	@Override
	public AmxApiResponse<CurrencyPairDTO, Object> getCurrencyPairList() {
		// TODO Auto-generated method stub
		try {
			LOGGER.debug("in CurrencyPairInfo");
			String url = appConfig.getJaxURL() + Path.CURRENCY_PAIR_GET;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyPairDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in CurrencyPairInfo : ", e);
			return JaxSystemError.evaluate(e);
		}
	}
	
	@Override
	public AmxApiResponse<ForexOutLookResponseDTO, Object> getCurpairHistory() {
		try {
			LOGGER.debug("in getCurpairHistory");
			String url = appConfig.getJaxURL() + Path.CURRENCY_PAIR_HISTORY_GET;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.as(new ParameterizedTypeReference<AmxApiResponse<ForexOutLookResponseDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getCurpairHistory : ", e);
			return JaxSystemError.evaluate(e);
		}
	}
	


	@Override
	public AmxApiResponse<BoolRespModel, Object> saveUpdateCurrencyPair(ForexOutLookRequest dto) {
		try {
			LOGGER.debug("in SaveOrUpdate");
			String url = appConfig.getJaxURL() + Path.CURRENCY_PAIR_SAVE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in SaveOrUpdate : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	

	@Override
	public AmxApiResponse<BoolRespModel, Object> deleteCurrencyPair(BigDecimal pairId) {
		// TODO Auto-generated method stub
		try {
		LOGGER.debug("in deleteCurrencyPair");
		String url = appConfig.getJaxURL() + Path.CURRENCY_PAIR_DELETE;
		return restService.ajax(url).meta(new JaxMetaInfo())
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	} catch (Exception e) {
		LOGGER.error("exception in deleteCurrencyPair : ", e);
		return JaxSystemError.evaluate(e);
	}
	}

}
