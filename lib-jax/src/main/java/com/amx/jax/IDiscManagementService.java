package com.amx.jax;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;

public interface IDiscManagementService {

	public static class ApiEndPoints {
		
		public static final String PREFIX = "/discount-management";
		
		public static final String GET_COUNTRY_BRANCH = PREFIX + "/country-branch-list";
		
		public static final String GET_DISCOUTN_RATE= PREFIX + "/get-discount-rate";
		
		public static final String GET_MARKUP_DETAILS=PREFIX + "/get-margin-markup";

		public static final String SAVE_MARKUP_DETAILS=PREFIX + "/save-margin-markup";
		
	}

	AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO);
	
	AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData( OnlineMarginMarkupReq onlineMarginMarkupReq);
	
	AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData( OnlineMarginMarkupInfo onlineMarginMarkupInfo );
}
