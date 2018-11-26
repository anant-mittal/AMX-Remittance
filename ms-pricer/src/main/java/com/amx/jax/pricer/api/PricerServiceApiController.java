package com.amx.jax.pricer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.IPricerService;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;
import com.amx.jax.pricer.service.PricingService;

@RestController
public class PricerServiceApiController implements IPricerService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PricerServiceApiController.class);

	@Autowired
	PricingService pricingService;

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<PricingRespDTO, Object> fetchPriceForCustomer(PricingReqDTO pricingReqDTO) {

		pricingService.fetchRemitPricesForCustomer(pricingReqDTO);

		return null;
	}

}
