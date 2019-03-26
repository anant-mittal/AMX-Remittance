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
public class ForexOutlookClient implements IForexOutlookService {

	private static final Logger LOGGER = LoggerService.getLogger(ForexOutlookClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<CurrencyPairDTO, Object> getCurrencyPairList() {
		// TODO Auto-generated method stub
		try {
			LOGGER.debug("in CurrencyPairInfo :");
			return restService.ajax(appConfig.getJaxURL() + Path.CURRENCY_PAIR_GET).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyPairDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in CurrencyPairInfo : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<ForexOutLookResponseDTO, Object> getCurpairHistory() {

		try {
			LOGGER.debug("in getCurpairHistory :");
			return restService.ajax(appConfig.getJaxURL() + Path.CURRENCY_PAIR_HISTORY_GET).meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<ForexOutLookResponseDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in CurrencyPairInfo : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveUpdateCurrencyPair(ForexOutLookRequest forexOutlookRequest) {
		try {
			LOGGER.info("in SaveOrUpdate :" + "msg :" + forexOutlookRequest.getMessage() + "pairId :"
					+ forexOutlookRequest.getPairId()+"appConfig.getJaxURL()"+appConfig.getJaxURL());
			return restService.ajax(appConfig.getJaxURL() + Path.CURRENCY_PAIR_SAVE_UPDATE).meta(new JaxMetaInfo())
					.post(forexOutlookRequest)

					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.info("Inside exception");
			LOGGER.error("exception in SaveOrUpdate : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deleteCurrencyPair(BigDecimal pairId) {
		try {
			LOGGER.debug("in deleteCurrencyPair :" + pairId);
			return restService.ajax(appConfig.getJaxURL() + Path.CURRENCY_PAIR_DELETE).meta(new JaxMetaInfo())
					.queryParam(Params.PAIR_ID, pairId).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in deleteCurrencyPair client : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

}
