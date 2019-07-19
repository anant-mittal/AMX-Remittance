package com.amx.jax.client.insurance;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.insurance.OptInOutRequest;
import com.amx.jax.model.request.insurance.SaveInsuranceDetailRequest;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;
import com.amx.jax.rest.RestService;

@Component
public class GigInsuranceClient implements IGigInsuranceService {

	private static final Logger LOGGER = Logger.getLogger(GigInsuranceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<GigInsuranceDetail, Object> fetchInsuranceDetail() {
		try {
			LOGGER.debug("in fetchInsuranceDetail :");
			return restService.ajax(appConfig.getJaxURL() + Path.FETCH_INSURANCE_DETAIL).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<GigInsuranceDetail, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchInsuranceDetail : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveInsuranceDetail(SaveInsuranceDetailRequest request) {
		try {
			LOGGER.debug("in saveInsuranceDetail :");
			return restService.ajax(appConfig.getJaxURL() + Path.SAVE_INSURANCE_DETAIL).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveInsuranceDetail : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> optInOutInsurance(OptInOutRequest request) {
		try {
			LOGGER.debug("in optInOutInsurance :");
			return restService.ajax(appConfig.getJaxURL() + Path.OPT_IN_OUT_INSURANCE).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in optInOutInsurance : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
