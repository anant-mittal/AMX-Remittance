package com.amx.jax.pricer.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;

@Service
public class PricerTestService {

	@Autowired
	PricerServiceClient pricerServiceClient;

	@Async(ExecutorConfig.EXECUTER_GOLD)
	public Future<AmxApiResponse<PricingResponseDTO, Object>> fetchPriceForCustomerAsynch(
			PricingRequestDTO pricingRequestDTO) {

		AmxApiResponse<PricingResponseDTO, Object> resp = pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);

		return new AsyncResult<AmxApiResponse<PricingResponseDTO, Object>>(resp);

	}

}
