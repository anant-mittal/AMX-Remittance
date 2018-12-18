package com.amx.jax.pricer.api;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.PricerService;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;
import com.amx.jax.pricer.service.PricingService;
import com.amx.jax.pricer.util.PricingRateDetailsDTO;

@RestController
public class PricerServiceApiController implements PricerService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PricerServiceApiController.class);

	@Resource
	PricingService pricingService;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public PricingRateDetailsDTO PricingRateDetailsDTO() {
		PricingRateDetailsDTO pricingRateDetailsDTO = new PricingRateDetailsDTO();
		return pricingRateDetailsDTO;
	}
	
	
	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<PricingRespDTO, Object> fetchPriceForCustomer(PricingReqDTO pricingReqDTO) {

		PricingRespDTO pricingRespDTO = pricingService.fetchRemitPricesForCustomer(pricingReqDTO);

		return AmxApiResponse.build(pricingRespDTO);

	}

}
