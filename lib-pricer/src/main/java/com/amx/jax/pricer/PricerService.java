package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;
import com.amx.jax.pricer.exception.PricerServiceError;

public interface PricerService {

	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/pricer";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		public static final String FETCH_PRICE_CUSTOMER = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-price-customer";

		public static final String FETCH_BASE_PRICE = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-base-price";

	}

	@PricerApiStatus({ PricerServiceError.INVALID_USER_DETAILS, PricerServiceError.INCOMPATIBLE_DATA_TYPE })
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO);

	@PricerApiStatus({ PricerServiceError.INCOMPATIBLE_DATA_TYPE })
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO);

}
