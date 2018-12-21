package com.amx.jax.pricer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.PricerService;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;

/**
 * The Class PricerServiceApiTest.
 */
@RestController
@RequestMapping("test/")
public class PricerServiceApiTest implements PricerService {


	/** The rbaac service client. */
	@Autowired
	PricerServiceClient pricerServiceClient;

	@Override
	@RequestMapping(value = com.amx.jax.pricer.PricerService.ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.GET)
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);
	}

	@Override
	@RequestMapping(value = com.amx.jax.pricer.PricerService.ApiEndPoints.FETCH_BASE_PRICE, method = RequestMethod.GET)
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchBasePrice(pricingRequestDTO);
	}

}
