package com.amx.jax.insurance;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.request.insurance.SaveInsuranceDetailRequest;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;

public interface IGigInsuranceService {

	public static class Path {

		public static final String FETCH_INSURANCE_DETAIL = "/fetch-insurance-detail";
		public static final String SAVE_INSURANCE_DETAIL = "/save-insurance-detail";
		public static final String OPT_IN_OUT_INSURANCE = "/opt-in-out-insurance";
	}

	public static class Params {

		public static final String IDENTITY_INT = "identityInt";
	}

	AmxApiResponse<GigInsuranceDetail, Object> fetchInsuranceDetail();

	AmxApiResponse<BoolRespModel, Object> saveInsuranceDetail(SaveInsuranceDetailRequest request);

	AmxApiResponse<BoolRespModel, Object> optInOutInsurance();
}
