package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;
import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;
import com.amx.jax.pricer.exception.PricerServiceError;

public interface PricerService {

	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/pricer";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		/** The Constant INIT_AUTH. */
		public static final String FETCH_PRICE_CUSTOMER = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-price-customer";

	}

	@PricerApiStatus({ PricerServiceError.INVALID_USER_DETAILS, PricerServiceError.INCOMPATIBLE_DATA_TYPE })
	public AmxApiResponse<PricingRespDTO, Object> fetchPriceForCustomer(PricingReqDTO pricingReqDTO);

}
