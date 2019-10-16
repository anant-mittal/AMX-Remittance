package com.amx.amxlib.service;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

public interface IRemittanceServiceOnline {

	public static class Path {
		public static final String RATE_ENQUIRY = "/rate-enquiry/";
		public static final String APPLICATION_STATUS = "application/{application-id}/status";
	}

	public static class Params {
		public static final String APPLICATION_ID = "application-id";
	}

	AmxApiResponse<RemittanceTransactionResponsetModel, List<JaxConditionalFieldDto>> validateTransactionV2(
			RemittanceTransactionRequestModel model);

	AmxApiResponse<RemittanceTransactionStatusResponseModel, Object> getApplicationStatusByAppId(
			BigDecimal applicationId);
}
