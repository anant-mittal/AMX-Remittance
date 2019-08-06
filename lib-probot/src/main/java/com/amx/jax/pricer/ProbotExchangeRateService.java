package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;
import com.amx.jax.pricer.exception.PricerServiceError;

public interface ProbotExchangeRateService extends AbstractProbotInterface {

	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS,
			PricerServiceError.INVALID_CUSTOMER })
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO);

	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS })
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO);

	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS })
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO);
	
	@PricerApiStatus({ PricerServiceError.MISSING_AMOUNT, PricerServiceError.MISSING_ROUTING_BANK_IDS })
	public AmxApiResponse<ExchangeRateAndRoutingResponse, Object> fetchRemitRoutesAndPrices(ExchangeRateAndRoutingRequest dprRequestDTO);
	

}
