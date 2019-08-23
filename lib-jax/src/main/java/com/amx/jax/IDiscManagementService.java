package com.amx.jax;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;

public interface IDiscManagementService {

	public static class ApiEndPoints {
		
		public static final String PREFIX = "/discount-management";
		
		public static final String GET_COUNTRY_BRANCH = PREFIX + "/country-branch-list";
		
		public static final String GET_DISCOUTN_RATE= PREFIX + "/get-discount-rate";
		
	}

	AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO);
}
