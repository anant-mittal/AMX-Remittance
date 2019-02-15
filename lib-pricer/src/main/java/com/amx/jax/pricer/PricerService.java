package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;
import com.amx.jax.pricer.exception.PricerServiceError;

public interface PricerService {

	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/probot";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		public static final String FETCH_PRICE_CUSTOMER = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-price-customer";

		public static final String FETCH_BASE_PRICE = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-base-price";
		
		public static final String PRICE_TEST = SERVICE_PREFIX + API_VERSION_V1 + "/price-test";

	}

	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS,
			PricerServiceError.INVALID_CUSTOMER })
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO);

	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS,
			PricerServiceError.INVALID_CUSTOMER })
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO);

}
