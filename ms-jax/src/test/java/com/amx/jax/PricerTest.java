package com.amx.jax;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PricerTest {

	@Autowired
	PricerServiceClient pricerServiceClient ; 
	
	public void testGetPrice() {
		PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();
		AmxApiResponse<PricingResponseDTO, Object> apiResponse = pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);
		List<PricingResponseDTO> prices = apiResponse.getResults();
		
	}
}
